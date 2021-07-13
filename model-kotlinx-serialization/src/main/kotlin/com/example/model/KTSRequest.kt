package com.example.model

import kotlinx.serialization.Serializable

@Serializable
internal data class KTSRequest(
    val url: String,
    val method: String,
    val headers: Map<String, List<String>>? = null,
    val body: KTSRequestBody? = null,
)
