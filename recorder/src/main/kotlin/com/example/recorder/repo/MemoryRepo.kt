package com.example.recorder.repo

import com.example.recorder.DefaultMatcheRule
import com.example.recorder.MatchRule
import com.example.model.RequestModel
import com.example.model.ResponseModel

internal class MemoryRepo {
    private val records = mutableMapOf<RequestModel, MutableList<ResponseModel>>()

    fun read(
        requestModel: RequestModel,
        matchRule: MatchRule = DefaultMatcheRule.INSTANCE
    ): List<ResponseModel> {
        for ((recordedRequest, recordedResponses) in records) {
            if (matchRule.isMatch(recordedRequest, requestModel)) {
                return recordedResponses.toList()
            }
        }

        return listOf()
    }

    fun update(
        requestModel: RequestModel,
        responses: List<ResponseModel>,
        matchRule: MatchRule = DefaultMatcheRule.INSTANCE
    ) {
        for (key in records.keys) {
            if (matchRule.isMatch(key, requestModel)) {
                records[key] = responses.toMutableList()
            }
        }
    }

    fun write(requestModel: RequestModel, responseModel: ResponseModel) {
        val list = records.getOrDefault(requestModel, mutableListOf())
        list.add(responseModel)
        records[requestModel] = list
    }

    fun write(requestModel: RequestModel, responseRecords: MutableList<ResponseModel>) {
        records[requestModel] = responseRecords
    }


    fun getAllRecordings(): Map<RequestModel, List<ResponseModel>> {
        return records.toMap()
    }
}