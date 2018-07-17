package com.enihsyou.ntmnote.utils

import android.content.Context
import android.preference.PreferenceManager
import com.enihsyou.ntmnote.data.source.NotesDataSource
import com.enihsyou.ntmnote.data.source.NotesRepository
import com.enihsyou.ntmnote.data.source.local.NotesDatabase
import com.enihsyou.ntmnote.data.source.local.NotesLocalDataSource
import com.enihsyou.ntmnote.data.source.remote.UserRepository
import com.enihsyou.ntmnote.data.source.remote.WebApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Injection {
    private val webApi: WebApi by lazy {
        val retrofit = Retrofit.Builder()
            //            .baseUrl("http://192.168.0.100:8999")
            .baseUrl("http://47.100.117.174:8701")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(WebApi::class.java)
    }

    fun provideTasksRepository(context: Context): NotesDataSource {
        val api = webApi
        val executors = AppExecutors()

        val database = NotesDatabase.getInstance(context)
        val localDataSource =
            NotesLocalDataSource.getInstance(executors, database.notesDao(), context)
        val remoteDataSource = UserRepository(api, executors)
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val username = preferences.getString("username", null)
        val password = preferences.getString("password", null)

        return NotesRepository(localDataSource, remoteDataSource, username, password)
    }

    fun provideUserRepository(): UserRepository {
        return UserRepository(webApi, AppExecutors())
    }
}
