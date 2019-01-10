package com.poc.weatherapp.api

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ServiceHandler {
    private var client = OkHttpClient.Builder()
    private const val BASE_URL: String = "https://api.darksky.net/forecast/900435862f097f4cc7a2021dd67b8c12/"

    private fun initService(baseUrl: String): Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())

        retrofit.client(client.build())
        client.addInterceptor(provideHttpLoggingInterceptor())
        return retrofit.build()
    }

    private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor =
            HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
                Log.d(ServiceHandler::class.java.simpleName, message)
            })
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    fun getApi(): RestAPI {
        return initService(BASE_URL).create(RestAPI::class.java)
    }
}
