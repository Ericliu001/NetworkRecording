package com.example.recorder

import okhttp3.Interceptor

abstract class BaseInterceptor : Interceptor {
   var networkRecorder: NetworkRecorder? = null
}