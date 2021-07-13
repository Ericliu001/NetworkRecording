package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class KTSRequest(
    val url: String,
    val method: String,
    val headers: Map<String, List<String>>? = null,
    val requestBody: KTSRequestBody? = null,
)
