package com.enihsyou.ntmnote.data.source

import com.enihsyou.ntmnote.data.Message
import com.enihsyou.ntmnote.data.Note
import com.enihsyou.ntmnote.data.User

class NotesRepository(
    private val localDataSource: NotesDataSource,
    private val remoteDataSource: UserDataSource,
    private val username: String?,
    private val password: String?
) : NotesDataSource {


    override fun getNotes(
        force: Boolean,
        callback: NotesDataSource.LoadNotesCallback,
        errorCallback: NotesDataSource.SourceErrorCallback?
    ) {
        if (force && username != null && password != null) {
            remoteDataSource.getNotes(username, password, object : UserDataSource.GetNoteCallback {
                override fun onNoteLoaded(response: Message<User>) {
                    if (response.msg != null) {
                        errorCallback?.onDataNotAvailable(response.msg)
                    } else {
                        val notes = response.body?.notes ?: listOf<Note>()
                        callback.onNotesLoaded(notes)

                        localDataSource.getNotes(force, object : NotesDataSource.LoadNotesCallback {
                            override fun onNotesLoaded(localNotes: List<Note>) {
                                notes.filter { it.id !in localNotes.map { it.id } }
                                    .forEach { localDataSource.saveNote(it) }
                            }
                        })
                    }
                }
            })
        } else {
            localDataSource.getNotes(force, callback, errorCallback)
        }
    }

    override fun getNote(
        noteId: Int,
        callback: NotesDataSource.GetNoteCallback,
        errorCallback: NotesDataSource.SourceErrorCallback?
    ) {
        localDataSource.getNote(noteId, callback, errorCallback)
    }

    override fun saveNote(note: Note) {
        localDataSource.saveNote(note)
        if (username != null && password != null) {
            if (note.id != 0) {
                remoteDataSource.modifyNote(username, password, note)
            } else {
                remoteDataSource.uploadNote(username, password, note)
            }
        }
    }

    override fun updateNote(note: Note) {
        localDataSource.updateNote(note)
        if (username != null && password != null) {
            remoteDataSource.uploadNote(username, password, note)
        }
    }

    override fun archiveNote(noteId: Int) {
        localDataSource.archiveNote(noteId)
        if (username != null && password != null) {
            remoteDataSource.archiveNote(username, password, noteId)
        }
    }

    override fun activateNote(noteId: Int) {
        localDataSource.activateNote(noteId)
        if (username != null && password != null) {
//            remoteDataSource.activeNote(username, password, noteId)
        }
    }

    override fun deleteNote(noteId: Int) {
        localDataSource.deleteNote(noteId)
        if (username != null && password != null) {
            remoteDataSource.deleteNote(username, password, noteId)
        }
    }
}
