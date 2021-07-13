package com.example.networkrecording.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class LoggingInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response: Response = chain.proceed(request)

        Log.d("recorded", "Request: $request")
        Log.d("recorded", "Response: $response")
        return response
    }
}