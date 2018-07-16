package com.enihsyou.ntmnote.data.source

import com.enihsyou.ntmnote.data.Message
import com.enihsyou.ntmnote.data.Note
import com.enihsyou.ntmnote.data.User

interface UserDataSource {

    interface GetNoteCallback {
        fun onNoteLoaded(response: Message<User>)
    }

    interface RegisterCallback {
        fun onOperationDone(response:Message<User>)
    }
    interface LoginCallback {
        fun onUserLogin(response: Message<User>)
    }

    fun register(username: String, password: String, callback: RegisterCallback)
    fun login(username: String, password: String, callback: LoginCallback)
    fun getNotes(username: String, password: String, callback: GetNoteCallback)
    fun uploadNote(username: String, password: String, note: Note)
    fun modifyNote(username: String, password: String, note: Note)
    fun archiveNote(username: String, password: String, noteId: Int)
    fun deleteNote(username: String, password: String, noteId: Int)
}
