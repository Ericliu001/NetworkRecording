package com.example

import com.example.KTSModelConverter.fromKTSRequest
import com.example.KTSModelConverter.fromKTSResponse
import com.example.KTSModelConverter.toKTSRequest
import com.example.KTSModelConverter.toKTSResponse
import com.example.model.BaseRequest
import com.example.model.BaseResponse
import com.example.model.KTSRequest
import com.example.model.KTSResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.function.Consumer

class KTSSerializer : Serializer {
    override fun encodeToByteArray(originalPair: Pair<BaseRequest, List<BaseResponse>>): ByteArray {
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
        }.encodeToString(serializablePair).toByteArray()
    }

    override fun decodeFromByteArray(bytes: ByteArray): Pair<BaseRequest, MutableList<BaseResponse>> {
        val serializablePair: Pair<KTSRequest, List<KTSResponse>> =
            Json.decodeFromString(String(bytes))

        val baseRequest = fromKTSRequest(serializablePair.first)
        val baseResponses = serializablePair.second.map { ktsResponse -> fromKTSResponse(ktsResponse) }.toMutableList()

        return Pair(baseRequest, baseResponses)
    }
}