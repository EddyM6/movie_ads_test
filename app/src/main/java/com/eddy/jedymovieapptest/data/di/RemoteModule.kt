package com.eddy.jedymovieapptest.data.di

import com.eddy.jedymovieapptest.BuildConfig
import com.eddy.jedymovieapptest.data.remote.API_QUERY_KEY
import com.eddy.jedymovieapptest.data.remote.ApiService
import com.eddy.jedymovieapptest.data.remote.BASE_URL
import com.eddy.jedymovieapptest.data.remote.interceptor.ApiKeyInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


val networkModule = module {

    single<ApiService> {
        Retrofit.Builder()
            .client(getClient())
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

}

private fun getClient(): OkHttpClient {
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    return OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(ApiKeyInterceptor())
        .build()
}