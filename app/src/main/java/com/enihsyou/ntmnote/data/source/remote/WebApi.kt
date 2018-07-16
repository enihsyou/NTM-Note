package com.enihsyou.ntmnote.data.source.remote

import com.enihsyou.ntmnote.data.Message
import com.enihsyou.ntmnote.data.Note
import com.enihsyou.ntmnote.data.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface WebApi {

    @POST("/users/register")
    fun register(@Query("username") username: String, @Query("password") password: String): Call<Message<User>>

    @POST("/users/login")
    fun login(@Query("username") username: String, @Query("password") password: String): Call<Message<User>>

    @GET("/notes")
    fun syncDownload(@Query("username") username: String, @Query("password") password: String): Call<Message<User>>

    @POST("/notes")
    fun syncUpload(@Body note: Note, @Query("username") username: String, @Query("password") password: String): Call<Message<User>>

    @PATCH("/notes")
    fun modify(@Query("noteId") noteId: Int, @Body note: Note, @Query("username") username: String, @Query("password") password: String): Call<Message<Note>>

    @PATCH("/notes/archive")
    fun archive(@Query("noteId") noteId: Int, @Query("username") username: String, @Query("password") password: String): Call<Message<Note>>

    @PATCH("/notes/trash")
    fun trash(@Query("noteId") noteId: Int, @Query("username") username: String, @Query("password") password: String): Call<Message<Note>>
}

