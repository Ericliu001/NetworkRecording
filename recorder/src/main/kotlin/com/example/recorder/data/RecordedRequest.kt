package com.example.recorder.data

import kotlinx.serialization.Serializable

@Serializable
data class RecordedRequest(
    val url: String,
    val method: String,
    val headers: Map<String, String>? = null,
    val body: RecordedRequestBody? = null,
)
