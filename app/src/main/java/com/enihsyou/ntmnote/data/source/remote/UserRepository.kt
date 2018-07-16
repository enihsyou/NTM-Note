package com.enihsyou.ntmnote.data.source.remote

import android.util.Log
import com.enihsyou.ntmnote.data.Message
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
            api.register(username, password).enqueue(object : Callback<Message<User>> {
                override fun onFailure(call: Call<Message<User>?>?, t: Throwable?) {
                    Log.e(TAG, "联网注册失败", t)
                }

                override fun onResponse(call: Call<Message<User>?>?, response: Response<Message<User>>) {
                    callback.onOperationDone(response.body()!!)
                }
            })
        }
    }

    override fun login(username: String, password: String, callback: UserDataSource.LoginCallback) {
        appExecutors.networkIO.execute {
            api.login(username, password).enqueue(object : Callback<Message<User>> {
                override fun onFailure(call: Call<Message<User>?>?, t: Throwable?) {
                    Log.e(TAG, "联网登录失败", t)
                }

                override fun onResponse(call: Call<Message<User>?>?, response: Response<Message<User>>) {
                    callback.onUserLogin(response.body()!!)
                }
            })
        }
    }

    override fun getNotes(username: String, password: String, callback: UserDataSource.GetNoteCallback) {
        appExecutors.networkIO.execute {
            api.syncDownload(username, password).enqueue(object : Callback<Message<User>> {
                override fun onFailure(call: Call<Message<User>?>?, t: Throwable) {
                    Log.e(TAG, "联网下载失败", t)
                }

                override fun onResponse(call: Call<Message<User>?>?, response: Response<Message<User>>) {
                    callback.onNoteLoaded(response.body()!!)
                }
            })
        }
    }

    override fun uploadNote(username: String, password: String, note: Note) {
        appExecutors.networkIO.execute {
            try {
                api.syncUpload(note, username, password).execute()
            } catch (e: Exception) {
                Log.e(TAG, "联网上传失败", e)
            }
        }
    }

    override fun modifyNote(username: String, password: String, note: Note) {
        appExecutors.networkIO.execute {
            try {
                api.modify(note.id, note, username, password).execute()
            } catch (e: Exception) {
                Log.e(TAG, "联网修改失败", e)
            }
        }
    }

    override fun archiveNote(username: String, password: String, noteId: Int) {
        appExecutors.networkIO.execute {
            try {
                api.archive(noteId, username, password).execute()
            } catch (e: Exception) {
                Log.e(TAG, "联网归档操作失败", e)
            }
        }
    }

    override fun deleteNote(username: String, password: String, noteId: Int) {
        appExecutors.networkIO.execute {
            try {
                api.trash(noteId, username, password).execute()
            } catch (e: Exception) {
                Log.e(TAG, "联网删除操作失败", e)
            }
        }
    }

    companion object {
        private const val TAG = "UserRepository"
    }
}
