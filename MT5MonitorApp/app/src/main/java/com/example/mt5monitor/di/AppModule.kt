package com.example.mt5monitor.di

import android.content.Context
import androidx.room.Room
import com.example.mt5monitor.data.api.MetaApiService
import com.example.mt5monitor.data.repository.TradingRepository
import com.example.mt5monitor.data.repository.TradingRepositoryImpl
import com.example.mt5monitor.data.local.AppDatabase
import com.example.mt5monitor.data.local.NotificationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.metaapi.cloud/")
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideMetaApiService(retrofit: Retrofit): MetaApiService =
        retrofit.create(MetaApiService::class.java)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "mt5-monitor.db").build()

    @Provides
    fun provideNotificationDao(database: AppDatabase): NotificationDao = database.notificationDao()

    @Provides
    @Singleton
    fun provideTradingRepository(apiService: MetaApiService): TradingRepository =
        TradingRepositoryImpl(apiService)
}
