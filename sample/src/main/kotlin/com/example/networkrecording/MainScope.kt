package com.example.networkrecording

import com.example.networkrecording.network.GithubService
import com.example.networkrecording.network.LoggingInterceptor
import com.example.recorder.BaseInterceptor
import com.example.recorder.NetworkRecorder
import com.example.recorder.RecordingInterceptor
import com.example.recorder.ReplayInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

private const val API_BASE_URL = "https://api.github.com"

@motif.Scope
interface MainScope {

    fun networkRecorder(): NetworkRecorder

    fun githubService(): GithubService

    @motif.Objects
    abstract class Objects {
        fun mode(): Mode {
            return Mode.READING
        }

        fun recordingInterceptor(mode: Mode): BaseInterceptor {
            return when (mode) {
                Mode.READING -> ReplayInterceptor()
                Mode.WRITING -> RecordingInterceptor()
            }
        }

        fun networkRecorder(interceptor: BaseInterceptor): NetworkRecorder =
            NetworkRecorder(interceptor)

        fun okHttpClient(interceptor: BaseInterceptor): OkHttpClient =
            OkHttpClient.Builder()
//                .addInterceptor(LoggingInterceptor())
                .addInterceptor(interceptor)
                .build()

        @ExperimentalSerializationApi
        fun retrofit(okHttpClient: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                }
                    .asConverterFactory(MediaType.get("application/json")))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
        }

        fun githubService(retrofit: Retrofit): GithubService =
            retrofit.create(GithubService::class.java)

    }
}