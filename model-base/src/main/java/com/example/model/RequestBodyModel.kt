package com.example.model

data class RequestBodyModel(
    val bytes: Array<Byte>,
    val contentType: String? = null
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RequestBodyModel) return false

        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        return bytes.contentHashCode()
    }
}
