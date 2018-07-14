package com.enihsyou.ntmnote.ui

import android.content.Context
import android.preference.PreferenceManager
import com.enihsyou.ntmnote.data.source.NotesDataSource
import com.enihsyou.ntmnote.data.source.NotesRepository
import com.enihsyou.ntmnote.data.source.local.NotesDatabase
import com.enihsyou.ntmnote.data.source.local.NotesLocalDataSource
import com.enihsyou.ntmnote.data.source.remote.UserRepository
import com.enihsyou.ntmnote.data.source.remote.WebApi
import com.enihsyou.ntmnote.utils.AppExecutors
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Injection {
    fun provideTasksRepository(context: Context): NotesDataSource {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.100:8999")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(WebApi::class.java)
        val executors = AppExecutors()

        val database = NotesDatabase.getInstance(context)
        val localDataSource =
            NotesLocalDataSource.getInstance(
                executors,
                database.notesDao()
            )
        val remoteDataSource = UserRepository(api, executors)
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val username = preferences.getString("username", null)
        val password = preferences.getString("password", null)

        return NotesRepository(localDataSource, remoteDataSource, username, password)
    }
}
