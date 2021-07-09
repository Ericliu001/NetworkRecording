package com.example.recorder

import com.example.recorder.repo.MemoryRepo
import okhttp3.Interceptor
import okhttp3.Response

// TODO: 7/8/21 split the interceptor to 2: read interceptor and write interceptor.
class RecordingInterceptor() : Interceptor {
    private var repo: MemoryRepo? = null

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val recordedRequest = fromHttpRequest(request)

        // TODO: 7/7/21 specify read/write mode.
//        repo?.let { repo ->
//            val readResponse = repo.read(recordedRequest)
//            if (readResponse.isNotEmpty()) {
//                val first = readResponse.first()
//                return toHttpResponseBuilder(first).request(request).build()
//            }
//
//        }

        val response: Response = chain.proceed(request)
        repo?.let {
            it.write(recordedRequest, fromHttpResponse(response))
        }

        return response
    }


    internal fun setResponseRepo(repo: MemoryRepo) {
        this.repo = repo
    }
}
