package com.example.recorder

import com.example.recorder.data.RecordedRequest
import com.example.recorder.data.RecordedResponse
import com.example.recorder.data.RecordedResponseBody
import okhttp3.Response
import okhttp3.ResponseBody

fun fromHttpRequest(okhttpRequest: okhttp3.Request): RecordedRequest {
    return RecordedRequest(
        okhttpRequest.url(),
        okhttpRequest.method(),
        okhttpRequest.headers(),
        okhttpRequest.body(),
        okhttpRequest.tag()
    )
}

fun toHttpResponseBuilder(recordedResponse: RecordedResponse): Response.Builder {
    val responseBody = recordedResponse.responseBody
    var okhttpResponseBody: ResponseBody? = null

    responseBody?.let {
        okhttpResponseBody =
            ResponseBody.create(
                responseBody.contentType,
                responseBody.bytes.toByteArray()
            )
    }

    return okhttp3.Response.Builder()
        .code(recordedResponse.code)
        .message(recordedResponse.message)
        .headers(recordedResponse.headers)
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
        okhttpResponse.headers(),
        fromHttpResponesBody(okhttpResponse.peekBody(Long.MAX_VALUE)),
        okhttpResponse.protocol()
    )
}

private fun fromHttpResponesBody(okhttpResponseBody: ResponseBody?): RecordedResponseBody? {
    okhttpResponseBody?.let { body ->
        return RecordedResponseBody(
            body.bytes().toTypedArray(),
            body.contentType()
        )
    }
    return null
}