package com.example

import com.example.model.BaseRequest
import com.example.model.BaseResponse

interface Serializer {
    fun encodeToByteArray(originalPair: Pair<BaseRequest, List<BaseResponse>>): ByteArray

    fun decodeFromByteArray(bytes: ByteArray): Pair<BaseRequest, MutableList<BaseResponse>>
}