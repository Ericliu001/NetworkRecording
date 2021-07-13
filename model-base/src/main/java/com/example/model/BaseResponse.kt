package com.example.model

import okhttp3.Protocol

data class BaseResponse(
    val code: Int,
    val message: String,
    val headers: Map<String, List<String>>,
    val body: BaseResponseBody? = null,
    val protocol: Protocol = Protocol.HTTP_1_1
)
