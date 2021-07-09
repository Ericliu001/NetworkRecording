package com.example.recorder

import okhttp3.Interceptor
import okhttp3.Response

class RecordingInterceptor : BaseInterceptor() {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response: Response = chain.proceed(request)
        networkRecorder?.record(request, response)

        return response
    }
}
