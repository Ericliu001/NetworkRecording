package com.example.recorder.data

import okhttp3.MediaType


data class RecordedResponseBody(
    val bytes: Array<Byte>,
    val contentType: MediaType? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RecordedResponseBody) return false

        if (!bytes.contentEquals(other.bytes)) return false
        if (contentType != other.contentType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bytes.contentHashCode()
        result = 31 * result + (contentType?.hashCode() ?: 0)
        return result
    }
}