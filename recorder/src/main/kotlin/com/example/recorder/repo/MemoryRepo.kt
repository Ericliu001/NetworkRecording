package com.example.recorder.repo

import com.example.recorder.DefaultMatcheRule
import com.example.recorder.MatchRule
import com.example.recorder.data.RecordedRequest
import com.example.recorder.data.RecordedResponse

internal class MemoryRepo {
    private val records = mutableMapOf<RecordedRequest, MutableList<RecordedResponse>>()

    fun read(
        request: RecordedRequest,
        matchRule: MatchRule = DefaultMatcheRule.INSTANCE
    ): List<RecordedResponse> {
        for ((recordedRequest, recordedResponses) in records) {
            if (matchRule.isMatch(recordedRequest, request)) {
                return recordedResponses.toList()
            }
        }

        return listOf()
    }

    fun update(
        request: RecordedRequest,
        responses: List<RecordedResponse>,
        matchRule: MatchRule = DefaultMatcheRule.INSTANCE
    ) {
        for (key in records.keys) {
            if (matchRule.isMatch(key, request)) {
                records[key] = responses.toMutableList()
            }
        }
    }

    fun write(request: RecordedRequest, response: RecordedResponse) {
        val list = records.getOrDefault(request, mutableListOf())
        list.add(response)
        records[request] = list
    }

    fun getAllRecordings(): Map<RecordedRequest, List<RecordedResponse>> {
        return records.toMap()
    }
}