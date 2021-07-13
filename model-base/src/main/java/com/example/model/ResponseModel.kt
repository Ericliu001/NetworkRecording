package com.example.model

import okhttp3.Protocol

data class ResponseModel(
    val code: Int,
    val message: String,
    val headers: Map<String, List<String>>,
    val responseBody: ResponseBodyModel? = null,
    val protocol: Protocol = Protocol.HTTP_1_1
)
