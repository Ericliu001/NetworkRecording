package com.example.model

import kotlinx.serialization.Serializable

@Serializable
internal data class KTSRequestBody(
    val bytes: ByteArray,
    val contentType: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BaseRequestBody) return false

        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        return bytes.contentHashCode()
    }
}
