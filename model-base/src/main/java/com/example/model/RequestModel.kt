package com.example.model

data class RequestModel(
    val url: String,
    val method: String,
    val headers: Map<String, List<String>>? = null,
    val body: RequestBodyModel? = null,
)
