package com.example

import com.example.KTSModelTypeConverter.fromKTSRequest
import com.example.KTSModelTypeConverter.fromKTSResponse
import com.example.KTSModelTypeConverter.toKTSRequest
import com.example.KTSModelTypeConverter.toKTSResponse
import com.example.model.BaseRequest
import com.example.model.BaseResponse
import com.example.model.KTSRequest
import com.example.model.KTSResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.function.Consumer

class KTSSerializer : Serializer {
    override fun encodeToString(originalPair: Pair<BaseRequest, List<BaseResponse>>): String {
        val ktsRequest = toKTSRequest(originalPair.first)
        val ktsResponses = mutableListOf<KTSResponse>()

        originalPair.second.forEach(Consumer { baseResponse ->
            ktsResponses.add(
                toKTSResponse(
                    baseResponse
                )
            )
        })

        val serializablePair: Pair<KTSRequest, List<KTSResponse>> = Pair(ktsRequest, ktsResponses)
        return Json {
            allowStructuredMapKeys = true
        }.encodeToString(serializablePair)
    }

    override fun decodeFromString(encodedString: String): Pair<BaseRequest, MutableList<BaseResponse>> {
        val serializablePair: Pair<KTSRequest, List<KTSResponse>> =
            Json.decodeFromString(encodedString)

        val baseRequest = fromKTSRequest(serializablePair.first)
        val baseResponses = mutableListOf<BaseResponse>()

        serializablePair.second.forEach(Consumer { ktsResponse ->
            baseResponses.add(fromKTSResponse(ktsResponse))
        })

        return Pair(baseRequest, baseResponses)
    }
}