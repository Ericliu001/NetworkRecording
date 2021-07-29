package com.example.recorder

import com.example.recorder.utils.OkhttpDataConverter
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ReplayInterceptor(networkRecorder: NetworkRecorder) : BaseInterceptor(networkRecorder) {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        retrieveResponse(request)?.let {
            return it
        }

        return chain.proceed(request)
    }

    private fun retrieveResponse(request: Request): Response? {
        val retrievedResponses = networkRecorder?.retrieveResponse(request)
        if (retrievedResponses != null && retrievedResponses.isNotEmpty()) {
            return OkhttpDataConverter.toHttpResponseBuilder(retrievedResponses.first()).request(request).build()
        }
        return null
    }

}