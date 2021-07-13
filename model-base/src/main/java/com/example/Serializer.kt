package com.example

import com.example.model.BaseRequest
import com.example.model.BaseResponse

interface Serializer {
    fun encodeToString(originalPair: Pair<BaseRequest, List<BaseResponse>>): String

    fun decodeFromString(encodedString: String): Pair<BaseRequest, MutableList<BaseResponse>>
}