package com.example.recorder.repo

import com.example.recorder.DefaultMatcheRule
import com.example.recorder.MatchRule
import com.example.recorder.data.RequestRecord
import com.example.recorder.data.ResponseRecord

internal class MemoryRepo {
    private val records = mutableMapOf<RequestRecord, MutableList<ResponseRecord>>()

    fun read(
        requestRecord: RequestRecord,
        matchRule: MatchRule = DefaultMatcheRule.INSTANCE
    ): List<ResponseRecord> {
        for ((recordedRequest, recordedResponses) in records) {
            if (matchRule.isMatch(recordedRequest, requestRecord)) {
                return recordedResponses.toList()
            }
        }

        return listOf()
    }

    fun update(
        requestRecord: RequestRecord,
        responses: List<ResponseRecord>,
        matchRule: MatchRule = DefaultMatcheRule.INSTANCE
    ) {
        for (key in records.keys) {
            if (matchRule.isMatch(key, requestRecord)) {
                records[key] = responses.toMutableList()
            }
        }
    }

    fun write(requestRecord: RequestRecord, responseRecord: ResponseRecord) {
        val list = records.getOrDefault(requestRecord, mutableListOf())
        list.add(responseRecord)
        records[requestRecord] = list
    }

    fun getAllRecordings(): Map<RequestRecord, List<ResponseRecord>> {
        return records.toMap()
    }
}