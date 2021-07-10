package com.example.model

import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody

class OkHttpDataTypeConverter {
    fun fromHttpRequest(okhttpRequest: Request): Models.Request {
        // TODO: 7/10/21
        throw UnsupportedOperationException()
    }

    fun toHttpResponseBuilder(response: Models.Response): Response.Builder {
        // TODO: 7/10/21
        throw UnsupportedOperationException()
    }

    fun fromHttpResponse(okhttpResponse: Response): Models.Response {
        // TODO: 7/10/21
        throw UnsupportedOperationException()
    }

    private fun fromHttpRequestBody(okhttpRequestBody: RequestBody?): Models.RequestBody? {
        // TODO: 7/10/21
        throw UnsupportedOperationException()
    }

    private fun fromHttpResponesBody(okhttpResponseBody: ResponseBody?): Models
    .ResponseBody? {
// TODO: 7/10/21
        throw UnsupportedOperationException()
    }
}