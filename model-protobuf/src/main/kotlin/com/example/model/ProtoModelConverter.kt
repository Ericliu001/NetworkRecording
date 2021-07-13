package com.example.model

import com.google.protobuf.ByteString
import okhttp3.Protocol

internal object ProtoModelConverter {
    fun toProtoRequest(baseRequest: BaseRequest): ProtoModel.Request {
        return ProtoModel.Request.newBuilder()
            .setUrl(baseRequest.url)
            .setMethod(baseRequest.method)
            .putAllHeaders(toProtoHeaders(baseRequest.headers))
            .setBody(toProtoRequestBody(baseRequest.body))
            .build()
    }

    fun fromProtoRequest(protoRequest: ProtoModel.Request): BaseRequest {
        return BaseRequest(
            protoRequest.url,
            protoRequest.method,
            fromProtoHeaders(protoRequest.headersMap),
            fromProtoRequestBody(protoRequest.body)
        )
    }

    fun fromProtoResponse(protoResponse: ProtoModel.Response): BaseResponse {
        return BaseResponse(
            protoResponse.code,
            protoResponse.message,
            fromProtoHeaders(protoResponse.headersMap),
            fromProtoResponseBody(protoResponse.responseBody),
            fromProtoProtocol(protoResponse.protocal)
        )
    }

    fun toProtoResponse(baseResponse: BaseResponse): ProtoModel.Response {
        return ProtoModel.Response.newBuilder()
            .setCode(baseResponse.code)
            .setMessage(baseResponse.message)
            .putAllHeaders(toProtoHeaders(baseResponse.headers))
            .setResponseBody(toProtoResponseBody(baseResponse.body))
            .setProtocal(toProtoProtocol(baseResponse.protocol))
            .build()
    }

    private fun fromProtoResponseBody(protoResponseBody: ProtoModel.ResponseBody): BaseResponseBody {
        return BaseResponseBody(
            protoResponseBody.bytes.toByteArray(),
            protoResponseBody.contentType
        )
    }

    private fun toProtoResponseBody(baseResponseBody: BaseResponseBody?): ProtoModel.ResponseBody {
        if (baseResponseBody == null) {
            return ProtoModel.ResponseBody.getDefaultInstance()
        }

        return ProtoModel.ResponseBody.newBuilder()
            .setBytes(ByteString.copyFrom(baseResponseBody.bytes))
            .setContentType(baseResponseBody.contentType)
            .build()
    }

    private fun fromProtoRequestBody(protoRequestBody: ProtoModel.RequestBody): BaseRequestBody {
        return BaseRequestBody(
            protoRequestBody.bytes.toByteArray(),
            protoRequestBody.contentType
        )
    }

    private fun toProtoRequestBody(baseRequestBody: BaseRequestBody?): ProtoModel.RequestBody {
        if (baseRequestBody == null) {
            return ProtoModel.RequestBody.getDefaultInstance()
        }

        return ProtoModel.RequestBody.newBuilder()
            .setBytes(ByteString.copyFrom(baseRequestBody.bytes))
            .setContentType(baseRequestBody.contentType)
            .build()
    }

    private fun fromProtoProtocol(protoProtocol: ProtoModel.Protocol): Protocol {
        return when (protoProtocol) {
            ProtoModel.Protocol.HTTP_1_0 -> Protocol.HTTP_1_0
            ProtoModel.Protocol.HTTP_1_1 -> Protocol.HTTP_1_1
            ProtoModel.Protocol.SPDY_3 -> Protocol.SPDY_3
            ProtoModel.Protocol.HTTP_2 -> Protocol.HTTP_2
            ProtoModel.Protocol.H2_PRIOR_KNOWLEDGE -> Protocol.H2_PRIOR_KNOWLEDGE
            ProtoModel.Protocol.QUIC -> Protocol.QUIC
            else -> Protocol.HTTP_2
        }
    }

    private fun toProtoProtocol(okhttpProtocol: okhttp3.Protocol): ProtoModel.Protocol {
        return when (okhttpProtocol) {
            Protocol.HTTP_1_0 -> ProtoModel.Protocol.HTTP_1_0
            Protocol.HTTP_1_1 -> ProtoModel.Protocol.HTTP_1_1
            Protocol.SPDY_3 -> ProtoModel.Protocol.SPDY_3
            Protocol.HTTP_2 -> ProtoModel.Protocol.HTTP_2
            Protocol.H2_PRIOR_KNOWLEDGE -> ProtoModel.Protocol.H2_PRIOR_KNOWLEDGE
            Protocol.QUIC -> ProtoModel.Protocol.QUIC
            else -> ProtoModel.Protocol.HTTP_2
        }

    }

    private fun fromProtoHeaders(protoHeaders: Map<String, ProtoModel.ListOfStrings>):
            MutableMap<String, List<String>> {
        val baseHeaders: MutableMap<String, List<String>> = mutableMapOf()

        for ((name, values) in protoHeaders) {
            val valueList = values.strList
            baseHeaders[name] = valueList
        }

        return baseHeaders
    }

    private fun toProtoHeaders(baseHeaders: Map<String, List<String>>?): Map<String, ProtoModel.ListOfStrings> {
        val protoHeaders: MutableMap<String, ProtoModel.ListOfStrings> = mutableMapOf()
        baseHeaders?.let { headers ->
            for ((name, value) in headers) {
                protoHeaders[name] = ProtoModel.ListOfStrings.newBuilder().addAllStr(value).build()
            }
        }

        return protoHeaders
    }
}