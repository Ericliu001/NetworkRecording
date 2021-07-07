package com.example.recorder.data

import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.RequestBody

class RecordedRequest(
    val url: HttpUrl,
    val method: String,
    val headers: Headers? = null,
    val body: RequestBody? = null,
    val tags: Any? = null
)
