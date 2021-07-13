package com.example.model

import kotlinx.serialization.Serializable
import okhttp3.Protocol

@Serializable
data class KTSResponse(
    val code: Int,
    val message: String,
    val headers: Map<String, List<String>>,
    val responseBody: KTSResponseBody? = null,
    val protocol: Protocol = Protocol.HTTP_1_1
)
