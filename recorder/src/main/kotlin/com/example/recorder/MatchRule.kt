package com.example.recorder

import com.example.model.RequestRecord

interface MatchRule {
    fun isMatch(a: RequestRecord, b: RequestRecord): Boolean
}

class DefaultMatcheRule : MatchRule {
    override fun isMatch(a: RequestRecord, b: RequestRecord): Boolean {
        return a.url == b.url
    }

    companion object {
        val INSTANCE = DefaultMatcheRule()
    }
}