package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class RequestRecord(
    val url: String,
    val method: String,
    val headers: Map<String, List<String>>? = null,
    val body: RequestBodyRecord? = null,
)
