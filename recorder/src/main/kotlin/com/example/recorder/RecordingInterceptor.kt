package com.example.recorder

import okhttp3.Interceptor
import okhttp3.Response

class RecordingInterceptor() : Interceptor {
    private var repo: MemoryRepo? = null

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val recordedRequest = fromHttpRequest(request)

        // TODO: 7/7/21 specify read/write mode.
        repo?.let { repo ->
            val readResponse = repo.read(recordedRequest)
            if (readResponse.isNotEmpty()) {
                val first = readResponse.first()
                return toHttpResponseBuilder(first).request(request).build()
            }

        }

        val response: Response = chain.proceed(request)
        repo?.let {
            it.write(recordedRequest, fromHttpResponse(response))
        }

        return response
    }


    fun setResponseRepo(repo: MemoryRepo) {
        this.repo = repo
    }
}
