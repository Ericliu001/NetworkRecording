package com.example.model

import com.example.Serializer
import com.example.model.ProtoModelConverter.fromProtoRequest
import com.example.model.ProtoModelConverter.fromProtoResponse
import com.example.model.ProtoModelConverter.toProtoRequest
import com.example.model.ProtoModelConverter.toProtoResponse

class ProtoSerializer : Serializer {
    override fun encodeToByteArray(originalPair: Pair<BaseRequest, List<BaseResponse>>): ByteArray {
        val protoRequest = toProtoRequest(originalPair.first)
        val protoResponses = originalPair.second.map { baseResponse -> toProtoResponse(baseResponse) }.toMutableList()

        return ProtoModel.Entry.newBuilder()
            .setRequest(protoRequest)
            .addAllResponses(protoResponses)
            .build()
            .toByteArray()
    }

    override fun decodeFromByteArray(bytes: ByteArray): Pair<BaseRequest, MutableList<BaseResponse>> {
        val entry = ProtoModel.Entry.parseFrom(bytes)

        val baseRequest: BaseRequest = fromProtoRequest(entry.request)
        val baseResponse: MutableList<BaseResponse> = entry.responsesList.map { response -> fromProtoResponse(response) }.toMutableList()

        return Pair(baseRequest, baseResponse)
    }


}