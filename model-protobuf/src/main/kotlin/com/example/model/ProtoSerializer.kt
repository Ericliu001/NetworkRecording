//package com.example.model
//
//import com.example.Serializer
//import com.example.model.ProtoModelConverter.fromProtoRequest
//import com.example.model.ProtoModelConverter.fromProtoResponse
//import com.example.model.ProtoModelConverter.toProtoRequest
//import com.example.model.ProtoModelConverter.toProtoResponse
//import java.io.InputStream
//import java.util.function.Consumer
//
//class ProtoSerializer : Serializer {
//    override fun encodeToString(originalPair: Pair<BaseRequest, List<BaseResponse>>): ByteArray? {
//        val protoRequest = toProtoRequest(originalPair.first)
//        val protoResponses = mutableListOf<ProtoModel.Response>()
//
//        originalPair.second.forEach(Consumer { baseResponse ->
//            protoResponses.add(toProtoResponse(baseResponse))
//        })
//
//        return ProtoModel.Entry.newBuilder()
//            .setRequest(protoRequest)
//            .addAllResponses(protoResponses)
//            .build()
//            .toByteArray()
//    }
//
//    override fun decodeFromString(inputStream: InputStream): Pair<BaseRequest, MutableList<BaseResponse>> {
//        val entry = ProtoModel.Entry.parseFrom(inputStream)
//
//        val baseRequest: BaseRequest = fromProtoRequest(entry.request)
//        val baseResponse: MutableList<BaseResponse> = entry.responsesList.map { response -> fromProtoResponse(response) }.toMutableList()
//
//        return Pair(baseRequest, baseResponse)
//    }
//}