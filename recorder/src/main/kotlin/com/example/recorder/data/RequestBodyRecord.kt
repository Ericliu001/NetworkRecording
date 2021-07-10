package com.example.recorder.data

import kotlinx.serialization.Serializable

@Serializable
data class RequestBodyRecord(
    val bytes: Array<Byte>,
    val contentType: String? = null
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RequestBodyRecord) return false

        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        return bytes.contentHashCode()
    }
}
