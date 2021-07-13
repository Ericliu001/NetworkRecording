package com.example.model

import com.example.Serializer
import com.example.model.ProtoModelConverter.fromProtoRequest
import com.example.model.ProtoModelConverter.fromProtoResponse
import com.example.model.ProtoModelConverter.toProtoRequest
import com.example.model.ProtoModelConverter.toProtoResponse
import java.util.function.Consumer

class ProtoSerializer : Serializer {
    override fun encodeToString(originalPair: Pair<BaseRequest, List<BaseResponse>>): String {
        val protoRequest = toProtoRequest(originalPair.first)
        val protoResponses = mutableListOf<ProtoModel.Response>()

        originalPair.second.forEach(Consumer { baseResponse ->
            protoResponses.add(toProtoResponse(baseResponse))
        })

        return ProtoModel.Entry.newBuilder()
            .setRequest(protoRequest)
            .addAllResponses(protoResponses)
            .build()
            .toByteString()
            .toStringUtf8()
    }

    override fun decodeFromString(encodedString: String): Pair<BaseRequest, MutableList<BaseResponse>> {
        val entry = ProtoModel.Entry.parseFrom(encodedString.toByteArray())

        val baseRequest: BaseRequest = fromProtoRequest(entry.request)
        val baseResponse: MutableList<BaseResponse> = entry.responsesList.map { response -> fromProtoResponse(response) }.toMutableList()

        return Pair(baseRequest, baseResponse)
    }
}