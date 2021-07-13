package com.example.model

data class BaseRequest(
    val url: String,
    val method: String,
    val headers: Map<String, List<String>>? = null,
    val body: BaseRequestBody? = null,
)
