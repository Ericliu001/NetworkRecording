package com.example.recorder.utils

import com.example.model.BaseRequest
import com.example.model.BaseRequestBody
import com.example.model.BaseResponse
import com.example.model.BaseResponseBody
import okhttp3.*
import okio.Buffer
import java.io.IOException

object OkhttpDataConverter {
    fun fromHttpRequest(okhttpRequest: okhttp3.Request): BaseRequest {
        return BaseRequest(
            okhttpRequest.url().encodedPath(),
            okhttpRequest.method(),
            okhttpRequest.headers().toMultimap(),
            fromHttpRequestBody(okhttpRequest.body())
        )
    }

    fun fromHttpResponse(okhttpResponse: okhttp3.Response): BaseResponse {
        return BaseResponse(
            okhttpResponse.code(),
            okhttpResponse.message(),
            okhttpResponse.headers().toMultimap(),
            // use okhttp3.Response.peekBody instead okhttp3.Response.body of  so the response is not closed.
            fromHttpResponseBody(okhttpResponse.peekBody(Long.MAX_VALUE)),
            okhttpResponse.protocol()
        )
    }

    fun toHttpResponseBuilder(baseResponse: BaseResponse): okhttp3.Response.Builder {
        val responseBody = baseResponse.body
        var okhttpResponseBody: ResponseBody? = null
        val mediaType = MediaType.parse(responseBody?.contentType ?: "")
        responseBody?.let {
            okhttpResponseBody =
                ResponseBody.create(
                    mediaType,
                    responseBody.bytes
                )
        }

        val headersBuilder = Headers.Builder()
        for ((name, values) in baseResponse.headers) {
            for (value in values) {
                headersBuilder.add(name, value)
            }
        }

        return okhttp3.Response.Builder()
            .code(baseResponse.code)
            .message(baseResponse.message)
            .headers(headersBuilder.build())
            .body(okhttpResponseBody)
            .protocol(Protocol.get(baseResponse.protocol.toString()))
    }

    private fun fromHttpRequestBody(okhttpRequestBody: RequestBody?): BaseRequestBody? {
        okhttpRequestBody?.let { body ->
            val content: ByteArray
            val contentType: String?

            try {
                val buffer = Buffer()
                body.writeTo(buffer)
                content = buffer.readByteArray()
                contentType = body.contentType().toString()
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
            return BaseRequestBody(
                content,
                contentType
            )
        }

        return null
    }

    private fun fromHttpResponseBody(okhttpResponseBody: ResponseBody?): BaseResponseBody? {
        okhttpResponseBody?.let { body ->
            return BaseResponseBody(
                body.bytes(),
                body.contentType()?.toString()
            )
        }
        return null
    }
}