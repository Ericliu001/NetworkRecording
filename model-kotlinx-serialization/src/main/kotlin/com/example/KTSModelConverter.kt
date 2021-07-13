package com.example

import com.example.model.*

internal object KTSModelConverter {

    fun toKTSRequest(baseRequest: BaseRequest): KTSRequest {
        return KTSRequest(
            baseRequest.url,
            baseRequest.method,
            baseRequest.headers,
            toKTSRequestBody(baseRequest.body)
        )
    }

    fun fromKTSRequest(ktsRequest: KTSRequest): BaseRequest {
        return BaseRequest(
            ktsRequest.url,
            ktsRequest.method,
            ktsRequest.headers,
            fromKTSRequestBody(ktsRequest.body)
        )
    }

    fun fromKTSResponse(ktsResponse: KTSResponse): BaseResponse {
        return BaseResponse(
            ktsResponse.code,
            ktsResponse.message,
            ktsResponse.headers,
            fromKTSResponseBody(ktsResponse.body),
            ktsResponse.protocol
        )
    }

    fun toKTSResponse(baseResponse: BaseResponse): KTSResponse {
        return KTSResponse(
            baseResponse.code,
            baseResponse.message,
            baseResponse.headers,
            toKTSResponseBody(baseResponse.body),
            baseResponse.protocol
        )
    }

    private fun fromKTSResponseBody(ktsResponseBody: KTSResponseBody?): BaseResponseBody? {
        if (ktsResponseBody == null) {
            return null
        }

        return BaseResponseBody(
            ktsResponseBody.bytes,
            ktsResponseBody.contentType
        )
    }

    private fun toKTSResponseBody(baseResponseBody: BaseResponseBody?): KTSResponseBody? {
        if (baseResponseBody == null) {
            return null
        }

        return KTSResponseBody(
            baseResponseBody.bytes,
            baseResponseBody.contentType
        )
    }

    private fun fromKTSRequestBody(ktsRequestBody: KTSRequestBody?): BaseRequestBody? {
        if (ktsRequestBody == null) {
            return null
        }

        return BaseRequestBody(
            ktsRequestBody.bytes,
            ktsRequestBody.contentType
        )
    }

    private fun toKTSRequestBody(baseRequestBody: BaseRequestBody?): KTSRequestBody? {
        if (baseRequestBody == null) {
            return null
        }

        return KTSRequestBody(
            baseRequestBody.bytes,
            baseRequestBody.contentType
        )
    }
}