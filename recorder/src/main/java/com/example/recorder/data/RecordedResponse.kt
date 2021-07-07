package com.example.recorder.data

import okhttp3.Headers
import okhttp3.Protocol

data class RecordedResponse(
    val code: Int,
    val message: String,
    val headers: Headers,
    val responseBody: RecordedResponseBody? = null,
    val protocol: Protocol = Protocol.HTTP_1_1
)
