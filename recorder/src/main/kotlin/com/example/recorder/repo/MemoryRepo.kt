package com.example.recorder.repo

import com.example.recorder.DefaultMatcheRule
import com.example.recorder.MatchRule
import com.example.model.BaseRequest
import com.example.model.BaseResponse

internal class MemoryRepo {
    private val records = mutableMapOf<BaseRequest, MutableList<BaseResponse>>()

    fun read(
        baseRequest: BaseRequest,
        matchRule: MatchRule = DefaultMatcheRule.INSTANCE
    ): List<BaseResponse> {
        for ((recordedRequest, recordedResponses) in records) {
            if (matchRule.isMatch(recordedRequest, baseRequest)) {
                return recordedResponses.toList()
            }
        }

        return listOf()
    }

    fun update(
        baseRequest: BaseRequest,
        responses: List<BaseResponse>,
        matchRule: MatchRule = DefaultMatcheRule.INSTANCE
    ) {
        for (key in records.keys) {
            if (matchRule.isMatch(key, baseRequest)) {
                records[key] = responses.toMutableList()
            }
        }
    }

    fun write(baseRequest: BaseRequest, baseResponse: BaseResponse) {
        val list = records.getOrDefault(baseRequest, mutableListOf())
        list.add(baseResponse)
        records[baseRequest] = list
    }

    fun write(baseRequest: BaseRequest, baseResponseRecords: MutableList<BaseResponse>) {
        records[baseRequest] = baseResponseRecords
    }


    fun getAllRecordings(): Map<BaseRequest, List<BaseResponse>> {
        return records.toMap()
    }
}