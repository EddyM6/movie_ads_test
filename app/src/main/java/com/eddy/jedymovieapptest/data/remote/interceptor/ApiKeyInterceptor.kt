package com.eddy.jedymovieapptest.data.remote.interceptor

import android.util.Log
import com.eddy.jedymovieapptest.BuildConfig
import com.eddy.jedymovieapptest.data.remote.API_QUERY_KEY
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d("tirara", "intercept: qaq")
        val currentUrl = chain.request().url
        val apiKey = BuildConfig.API_KEY
        val newUrl = currentUrl.newBuilder().addQueryParameter(API_QUERY_KEY, apiKey).build()
        val currentRequest = chain.request().newBuilder()
        val newRequest = currentRequest.url(newUrl).build()
        return chain.proceed(newRequest)
    }
}