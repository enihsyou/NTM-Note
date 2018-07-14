package com.enihsyou.ntmnote.data.source.remote

import com.enihsyou.ntmnote.data.Note
import com.enihsyou.ntmnote.data.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WebApi {

    @POST("/users/register")
    fun register(@Query("username") username: String, @Query("password") password: String): Call<User>

    @POST("/users/login")
    fun login(@Query("username") username: String, @Query("password") password: String): Call<User>

    @GET("/notes")
    fun syncDownload(@Query("username") username: String, @Query("password") password: String): Call<List<Note>>

    @POST("/notes")
    fun syncUpload(@Body note: Note, @Query("username") username: String, @Query("password") password: String): Call<Void>
}

