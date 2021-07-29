package com.example.recorder

import okhttp3.Interceptor

abstract class BaseInterceptor(val networkRecorder: NetworkRecorder) : Interceptor