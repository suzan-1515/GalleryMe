package com.example.galleryme.core.di

import android.content.Context
import android.util.Log
import com.example.galleryme.core.config.AppConfig
import com.example.galleryme.core.helper.NetworkHelper
import com.example.galleryme.data.datasource.api.service.ImageApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okio.IOException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesNetworkHelper(@ApplicationContext context: Context): NetworkHelper {
        return NetworkHelper(context)
    }

    @Provides
    @Singleton
    fun providesLoggingInterceptor(networkHelper: NetworkHelper): Interceptor {
        val interceptor = Interceptor { chain ->
            if (!networkHelper.isNetworkConnected())
                throw IOException("No internet connection")
            val request = chain.request()

            val t1 = System.nanoTime()
            Log.i(
                "Retrofit",
                String.format(
                    "Sending request %s on %s%n%s",
                    request.url, chain.connection(), request.headers
                )
            )

            val response = chain.proceed(request)

            val t2 = System.nanoTime()
            Log.i(
                "Retrofit",
                String.format(
                    "Received response for %s in %.1fms%n%s",
                    response.request.url, (t2 - t1) / 1e6, response.headers
                )
            )
            response
        }
        return interceptor
    }


    @Provides
    @Singleton
    fun providesInterceptor(
        interceptor: Interceptor
    ): OkHttpClient {
        val httpBuilder = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(50, TimeUnit.SECONDS)

        return httpBuilder
            .build()
    }


    @Singleton
    @Provides
    fun providesRetrofit(
        client: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder().baseUrl(AppConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    fun providesImageApi(retrofit: Retrofit): ImageApiService {
        return retrofit.create(ImageApiService::class.java)
    }


}