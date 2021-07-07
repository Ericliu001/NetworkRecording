package com.example.networkrecording.network

import com.example.networkrecording.network.data.Repository
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubService {
    @GET("/users/{owner}/repos")
    fun repos(
        @Path("owner") owner: String
    ): Observable<Response<List<Repository>>>
}
