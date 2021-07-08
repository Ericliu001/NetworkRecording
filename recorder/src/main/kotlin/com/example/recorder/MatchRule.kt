package com.example.recorder

import com.example.recorder.data.RecordedRequest

interface MatchRule {
    fun isMatch(a: RecordedRequest, b: RecordedRequest): Boolean
}

class DefaultMatcheRule : MatchRule {
    override fun isMatch(a: RecordedRequest, b: RecordedRequest): Boolean {
        return a.url == b.url
    }

    companion object {
        val INSTANCE = DefaultMatcheRule()
    }
}