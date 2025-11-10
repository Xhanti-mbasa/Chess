package com.example.isoscout

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.isoscout.theme.IsoScoutTheme
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IsoScoutTheme {
                IsoScoutApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IsoScoutApp(viewModel: MainViewModel) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments(),
    ) { uris ->
        if (uris.isNotEmpty()) {
            viewModel.analyzeUris(context, uris)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = context.getString(R.string.app_name)) })
        },
    ) { padding ->
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(onClick = {
                    launcher.launch(arrayOf("application/octet-stream", "video/*"))
                }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = context.getString(R.string.pick_files))
                }

                if (state.items.isEmpty()) {
                    Text(text = context.getString(R.string.empty_state))
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(state.items) { item ->
                            FileAnalysisRow(item = item)
                            Divider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FileAnalysisRow(item: FileAnalysis) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = item.displayName, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
        val subtitle = when (item.status) {
            AnalysisStatus.Pending -> R.string.analysis_pending
            is AnalysisStatus.Error -> null
            is AnalysisStatus.Complete -> null
        }
        subtitle?.let {
            Text(text = context.getString(it), style = MaterialTheme.typography.bodyMedium)
        }

        when (val status = item.status) {
            AnalysisStatus.Pending -> {
                CircularProgressIndicator(modifier = Modifier.padding(top = 8.dp))
            }

            is AnalysisStatus.Error -> {
                Text(
                    text = "Error: ${status.message}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            is AnalysisStatus.Complete -> {
                Text(
                    text = status.summary,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

class MainViewModel : ViewModel() {
    private val _state = MutableStateFlow(MainUiState())
    val state: StateFlow<MainUiState> = _state

    fun analyzeUris(context: Context, uris: List<Uri>) {
        val resolver = context.contentResolver
        val newItems = uris.map { uri ->
            val displayName = resolver.resolveDisplayName(uri) ?: uri.lastPathSegment ?: "Unknown file"
            FileAnalysis(
                uri = uri,
                displayName = displayName,
                status = AnalysisStatus.Pending
            )
        }
        _state.value = MainUiState(items = _state.value.items + newItems)

        newItems.forEach { item ->
            viewModelScope.launch {
                val summary = runCatching {
                    GeminiClient.analyzeFile(context, item.displayName)
                }.fold(
                    onSuccess = { AnalysisStatus.Complete(it) },
                    onFailure = { AnalysisStatus.Error(it.message ?: "Unknown error") }
                )

                _state.value = _state.value.updateItem(item.uri) { old ->
                    old.copy(status = summary)
                }
            }
        }
    }
}

data class MainUiState(val items: List<FileAnalysis> = emptyList()) {
    fun updateItem(uri: Uri, transform: (FileAnalysis) -> FileAnalysis): MainUiState {
        val updatedItems = items.map { if (it.uri == uri) transform(it) else it }
        return copy(items = updatedItems)
    }
}

data class FileAnalysis(
    val uri: Uri,
    val displayName: String,
    val status: AnalysisStatus
)

sealed class AnalysisStatus {
    data object Pending : AnalysisStatus()
    data class Complete(val summary: String) : AnalysisStatus()
    data class Error(val message: String) : AnalysisStatus()
}

private fun ContentResolver.resolveDisplayName(uri: Uri): String? {
    return query(uri, null, null, null, null)?.use { cursor ->
        val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (index != -1 && cursor.moveToFirst()) {
            cursor.getString(index)
        } else {
            null
        }
    }
}

private object GeminiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent"

    suspend fun analyzeFile(context: Context, displayName: String): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY.ifEmpty {
            throw IllegalStateException("Gemini API key is not configured. Add GEMINI_API_KEY to your gradle.properties or environment.")
        }

        val prompt = buildString {
            append("Identify the type of this file and provide a friendly summary.\n")
            append("File name: ${displayName}.\n")
            append("If it is an ISO, include likely platform and release year. If it is a video, indicate whether it appears to be a movie, series, or anime based on naming conventions.\n")
            append("Respond in two concise sentences.")
        }

        val payload = MoshiProvider.moshi.adapter(GenerateContentRequest::class.java).toJson(
            GenerateContentRequest(
                contents = listOf(
                    Content(
                        parts = listOf(Part(text = prompt))
                    )
                )
            )
        )

        val request = okhttp3.Request.Builder()
            .url("${BASE_URL}?key=${apiKey}")
            .post(payload.toRequestBody(JSON_MEDIA_TYPE))
            .build()

        okhttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw IllegalStateException("Gemini API error: ${response.code}")
            }

            val body = response.body?.string() ?: throw IllegalStateException("Empty response from Gemini API")
            val parsed = MoshiProvider.moshi.adapter(GenerateContentResponse::class.java).fromJson(body)
                ?: throw IllegalStateException("Unable to parse Gemini response")

            parsed.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: throw IllegalStateException("No content returned from Gemini")
        }
    }

    private val okhttpClient: okhttp3.OkHttpClient by lazy {
        okhttp3.OkHttpClient.Builder().build()
    }

    private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()
}

private object MoshiProvider {
    val moshi: com.squareup.moshi.Moshi by lazy {
        com.squareup.moshi.Moshi.Builder().build()
    }
}

data class GenerateContentRequest(val contents: List<Content>)

data class Content(val parts: List<Part>)

data class Part(val text: String)

data class GenerateContentResponse(val candidates: List<Candidate>?)

data class Candidate(val content: Content?)
