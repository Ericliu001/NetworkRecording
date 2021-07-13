package com.example.recorder.utils

import com.example.model.RequestBodyModel
import com.example.model.RequestModel
import com.example.model.ResponseBodyModel
import com.example.model.ResponseModel
import okhttp3.*
import okio.Buffer
import java.io.IOException

internal fun fromHttpRequest(okhttpRequest: okhttp3.Request): RequestModel {
    return RequestModel(
        okhttpRequest.url().encodedPath(),
        okhttpRequest.method(),
        okhttpRequest.headers().toMultimap(),
        fromHttpRequestBody(okhttpRequest.body())
    )
}

fun toHttpResponseBuilder(responseModel: ResponseModel): okhttp3.Response.Builder {
    val responseBody = responseModel.responseBody
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
    for ((name, values) in responseModel.headers) {
        for (value in values) {
            headersBuilder.add(name, value)
        }
    }

    return okhttp3.Response.Builder()
        .code(responseModel.code)
        .message(responseModel.message)
        .headers(headersBuilder.build())
        .body(okhttpResponseBody)
        .protocol(Protocol.get(responseModel.protocol.toString()))
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
fun fromHttpResponse(okhttpResponse: okhttp3.Response): ResponseModel {
    return ResponseModel(
        okhttpResponse.code(),
        okhttpResponse.message(),
        okhttpResponse.headers().toMultimap(),
        fromHttpResponesBody(okhttpResponse.peekBody(Long.MAX_VALUE)),
        okhttpResponse.protocol()
    )
}

private fun fromHttpRequestBody(okhttpRequestBody: RequestBody?): RequestBodyModel? {
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
        return RequestBodyModel(
            content,
            contentType
        )
    }

    return null
}

private fun fromHttpResponesBody(okhttpResponseBody: ResponseBody?): ResponseBodyModel? {
    okhttpResponseBody?.let { body ->
        return ResponseBodyModel(
            body.bytes().toTypedArray(),
            body.contentType()?.toString()
        )
    }
    return null
}