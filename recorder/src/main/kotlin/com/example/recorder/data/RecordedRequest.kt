package com.example.recorder.data

import kotlinx.serialization.Serializable

@Serializable
data class RecordedRequest(
    val url: String,
    val method: String,
    val headers: Map<String, List<String>>? = null,
    val body: RecordedRequestBody? = null,
)
