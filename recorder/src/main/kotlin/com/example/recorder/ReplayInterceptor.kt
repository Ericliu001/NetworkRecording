package com.example.recorder

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ReplayInterceptor : BaseInterceptor() {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        retrieveResponse(request)?.let {
            return it
        }

        val response: Response = chain.proceed(request)
        return response
    }

    private fun retrieveResponse(request: Request): Response? {
        val retrievedResponses = networkRecorder?.retrieveResponse(request)
        if (retrievedResponses != null && retrievedResponses.isNotEmpty()) {
            return toHttpResponseBuilder(retrievedResponses.first()).request(request).build()
        }
        return null
    }

}