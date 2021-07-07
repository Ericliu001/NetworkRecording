package com.example.recorder

import com.example.recorder.data.RecordedRequest

interface MatchRule {
    fun isMatch(a: RecordedRequest, b: RecordedRequest): Boolean
}

class DefaultMatcheRule : MatchRule {
    override fun isMatch(a: RecordedRequest, b: RecordedRequest): Boolean {
        return a.url.encodedPath() == b.url.encodedPath()
    }

    companion object {
        val INSTANCE = DefaultMatcheRule()
    }
}