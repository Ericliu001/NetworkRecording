package com.example.recorder

import com.example.model.RequestModel

interface MatchRule {
    fun isMatch(a: RequestModel, b: RequestModel): Boolean
}

class DefaultMatcheRule : MatchRule {
    override fun isMatch(a: RequestModel, b: RequestModel): Boolean {
        return a.url == b.url
    }

    companion object {
        val INSTANCE = DefaultMatcheRule()
    }
}