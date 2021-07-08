package com.example.recorder.data

import kotlinx.serialization.Serializable
import okhttp3.Protocol

@Serializable
data class RecordedResponse(
    val code: Int,
    val message: String,
    val headers: Map<String, List<String>>,
    val responseBody: RecordedResponseBody? = null,
    val protocol: Protocol = Protocol.HTTP_1_1
)
