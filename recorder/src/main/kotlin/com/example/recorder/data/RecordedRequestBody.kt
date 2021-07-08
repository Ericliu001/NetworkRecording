package com.example.recorder.data

import kotlinx.serialization.Serializable

@Serializable
data class RecordedRequestBody(
    val bytes: Array<Byte>,
    val contentType: String? = null // TODO: 7/8/21 a default type?
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RecordedRequestBody) return false

        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        return bytes.contentHashCode()
    }
}
