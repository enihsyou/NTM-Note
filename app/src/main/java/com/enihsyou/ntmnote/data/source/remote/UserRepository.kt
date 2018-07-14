package com.enihsyou.ntmnote.data.source.remote

import com.enihsyou.ntmnote.data.Note
import com.enihsyou.ntmnote.data.User
import com.enihsyou.ntmnote.data.source.UserDataSource
import com.enihsyou.ntmnote.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(
    private val api: WebApi,
    private val appExecutors: AppExecutors
) : UserDataSource {

    override fun register(username: String, password: String, callback: UserDataSource.RegisterCallback) {
        appExecutors.networkIO.execute {
            api.register(username, password).enqueue(object : Callback<User?> {
                override fun onFailure(call: Call<User?>?, t: Throwable?) {
                    callback.onOperationDone(false)
                }

                override fun onResponse(call: Call<User?>?, response: Response<User?>?) {
                    callback.onOperationDone(true)
                }
            })
        }
    }

    override fun login(username: String, password: String, callback: UserDataSource.LoginCallback) {
        appExecutors.networkIO.execute {
            api.login(username, password).enqueue(object : Callback<User?> {
                override fun onFailure(call: Call<User?>?, t: Throwable?) {
                    callback.onUserLogin(null)
                }

                override fun onResponse(call: Call<User?>?, response: Response<User?>?) {
                    callback.onUserLogin(response?.body())
                }
            })
        }
    }

    override fun getNotes(username: String, password: String, callback: UserDataSource.GetNoteCallback) {
        appExecutors.networkIO.execute {
            api.syncDownload(username, password).enqueue(object : Callback<List<Note>?> {
                override fun onFailure(call: Call<List<Note>?>?, t: Throwable?) {
                    callback.onNoteLoaded(null)
                }

                override fun onResponse(call: Call<List<Note>?>?, response: Response<List<Note>?>?) {
                    callback.onNoteLoaded(response?.body())
                }
            })
        }
    }

    override fun uploadNote(username: String, password: String, note: Note) {
        appExecutors.networkIO.execute {
            api.syncUpload(note, username, password).execute()
        }
    }
}
