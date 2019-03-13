package com.softwarefactory.retrofittest

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("/photos")
    fun getPosts():Call<List<Post>>

}