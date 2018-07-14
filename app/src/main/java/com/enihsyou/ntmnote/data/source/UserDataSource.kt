package com.enihsyou.ntmnote.data.source

import com.enihsyou.ntmnote.data.Note
import com.enihsyou.ntmnote.data.User

interface UserDataSource {

    interface GetNoteCallback {
        fun onNoteLoaded(notes: List<Note>?)
    }

    interface RegisterCallback {
        fun onOperationDone(status:Boolean)
    }
    interface LoginCallback {
        fun onUserLogin(user: User?)
    }

    fun register(username: String, password: String, callback: RegisterCallback)
    fun login(username: String, password: String, callback: LoginCallback)
    fun getNotes(username: String, password: String, callback: GetNoteCallback)
    fun uploadNote(username: String, password: String, note: Note)
}
