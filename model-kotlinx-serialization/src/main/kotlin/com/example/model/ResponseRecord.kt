package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class ResponseRecord(
    val code: Int,
    val message: String,
    val headers: Map<String, List<String>>,
    val responseBody: ResponseBodyRecord? = null,
    val protocol: Protocol = Protocol.HTTP_1_1
)