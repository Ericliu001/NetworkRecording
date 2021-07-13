package com.example.recorder

import com.example.model.BaseRequest

interface MatchRule {
    fun isMatch(a: BaseRequest, b: BaseRequest): Boolean
}

class DefaultMatcheRule : MatchRule {
    override fun isMatch(a: BaseRequest, b: BaseRequest): Boolean {
        return a.url == b.url
    }

    companion object {
        val INSTANCE = DefaultMatcheRule()
    }
}