package com.example.mt5monitor.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import com.example.mt5monitor.domain.model.ChartPoint

@Composable
fun MiniSignalChart(points: List<ChartPoint>, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, tonalElevation = 6.dp, shape = MaterialTheme.shapes.medium) {
        if (points.isEmpty()) {
            Text(
                text = "No chart data",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
            return@Surface
        }
        Canvas(modifier = Modifier.fillMaxWidth().height(200.dp)) {
            val maxPrice = points.maxOf { it.price }
            val minPrice = points.minOf { it.price }
            val priceRange = (maxPrice - minPrice).coerceAtLeast(0.0001)
            val stepX = size.width / (points.size - 1)
            val pricePath = Path()
            points.forEachIndexed { index, point ->
                val x = stepX * index
                val y = size.height - ((point.price - minPrice) / priceRange * size.height).toFloat()
                if (index == 0) pricePath.moveTo(x, y) else pricePath.lineTo(x, y)
            }
            drawPath(pricePath, color = MaterialTheme.colorScheme.primary, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f))

            val maxRsi = 100f
            val minRsi = 0f
            val rsiPath = Path()
            points.forEachIndexed { index, point ->
                val x = stepX * index
                val y = size.height - (((point.rsi - minRsi) / (maxRsi - minRsi)) * size.height).toFloat()
                if (index == 0) rsiPath.moveTo(x, y) else rsiPath.lineTo(x, y)
            }
            drawPath(rsiPath, color = Color.Magenta.copy(alpha = 0.7f), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f))

            drawLine(Color(0xFF16FF96), Offset(0f, size.height * (1f - 8f / 100f)), Offset(size.width, size.height * (1f - 8f / 100f)))
            drawLine(Color(0xFF16FF96), Offset(0f, size.height * (1f - 10f / 100f)), Offset(size.width, size.height * (1f - 10f / 100f)))
            drawLine(Color(0xFFFF5A5F), Offset(0f, size.height * (1f - 91f / 100f)), Offset(size.width, size.height * (1f - 91f / 100f)))
            drawLine(Color(0xFFFF5A5F), Offset(0f, size.height * (1f - 92f / 100f)), Offset(size.width, size.height * (1f - 92f / 100f)))
        }
    }
}
