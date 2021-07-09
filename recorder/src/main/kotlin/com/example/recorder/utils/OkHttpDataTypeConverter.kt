package com.example.recorder

import com.example.recorder.data.RecordedRequest
import com.example.recorder.data.RecordedRequestBody
import com.example.recorder.data.RecordedResponse
import com.example.recorder.data.RecordedResponseBody
import okhttp3.Headers
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.Buffer
import java.io.IOException

internal fun fromHttpRequest(okhttpRequest: okhttp3.Request): RecordedRequest {
    return RecordedRequest(
        okhttpRequest.url().encodedPath(),
        okhttpRequest.method(),
        okhttpRequest.headers().toMultimap(),
        fromHttpRequestBody(okhttpRequest.body())
    )
}

fun toHttpResponseBuilder(recordedResponse: RecordedResponse): okhttp3.Response.Builder {
    val responseBody = recordedResponse.responseBody
    var okhttpResponseBody: ResponseBody? = null
    val mediaType = MediaType.parse(responseBody?.contentType ?: "")
    responseBody?.let {
        okhttpResponseBody =
            ResponseBody.create(
                mediaType,
                responseBody.bytes.toByteArray()
            )
    }

    val headersBuilder = Headers.Builder()
    for ((name, values) in recordedResponse.headers) {
        for (value in values) {
            headersBuilder.add(name, value)
        }
    }

    return okhttp3.Response.Builder()
        .code(recordedResponse.code)
        .message(recordedResponse.message)
        .headers(headersBuilder.build())
        .body(okhttpResponseBody)
        .protocol(recordedResponse.protocol)
}

/**
 *
 *
 *
 *
 *
 * (https://github.com/square/retrofit/issues/3336)
Response.body().string()
I was doing because above code was helping me to find out that if there is any error than what kind of error it is....

if it is AUTH_ERROR, I have to generate new token and append it to request header.

According to retrofit document, if we call any of below method then response will be closed, which means it's not available to consume by the normal Retrofit internals.

Response.close()
Response.body().close()
Response.body().source().close()
Response.body().charStream().close()
Response.body().byteStream().close()
Response.body().bytes()
Response.body().string()
So to read data, I will use

response.peekBody(2048).string()
instead of

response.body().string(),
so it will not close response.
 *
 *
 */
fun fromHttpResponse(okhttpResponse: okhttp3.Response): RecordedResponse {
    return RecordedResponse(
        okhttpResponse.code(),
        okhttpResponse.message(),
        okhttpResponse.headers().toMultimap(),
        fromHttpResponesBody(okhttpResponse.peekBody(Long.MAX_VALUE)),
        okhttpResponse.protocol()
    )
}

private fun fromHttpRequestBody(okhttpRequestBody: RequestBody?): RecordedRequestBody? {
    okhttpRequestBody?.let { body ->
        val content: Array<Byte>
        val contentType: String?

        try {
            val buffer = Buffer()
            body.writeTo(buffer)
            content = buffer.readByteArray().toTypedArray()
            contentType = body.contentType().toString()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        return RecordedRequestBody(
            content,
            contentType
        )
    }

    return null
}

private fun fromHttpResponesBody(okhttpResponseBody: ResponseBody?): RecordedResponseBody? {
    okhttpResponseBody?.let { body ->
        return RecordedResponseBody(
            body.bytes().toTypedArray(),
            body.contentType()?.toString()
        )
    }
    return null
}