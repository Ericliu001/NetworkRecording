package com.example.recorder

import okhttp3.Response
import okhttp3.ResponseBody
import com.example.recorder.data.RecordedRequest
import com.example.recorder.data.RecordedResponse
import com.example.recorder.data.RecordedResponseBody

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