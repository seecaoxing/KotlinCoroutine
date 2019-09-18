package com.example.kotlincoroutine

import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubServiceApi {

    @GET("users/{login}")
    fun getUser(@Path("login") login: String): Call<User>


    @GET("users/{login}")
    fun getUser2(@Path("login") login: String): Deferred<User>

}