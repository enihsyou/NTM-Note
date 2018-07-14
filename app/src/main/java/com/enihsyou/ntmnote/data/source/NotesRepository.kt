package com.enihsyou.ntmnote.data.source

import com.enihsyou.ntmnote.data.Note

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
        if (username != null && password != null) {
            remoteDataSource.getNotes(username, password, object : UserDataSource.GetNoteCallback {
                override fun onNoteLoaded(notes: List<Note>?) {
                    if (notes != null) {
                        callback.onNotesLoaded(notes)
                        if (force) {
                            localDataSource.getNotes(true, object : NotesDataSource.LoadNotesCallback {
                                override fun onNotesLoaded(localNotes: List<Note>) {
                                    notes.filter { it.id !in localNotes.map { it.id } }
                                        .forEach { localDataSource.saveNote(it) }
                                }
                            })
                        }
                    } else localDataSource.getNotes(force, callback, errorCallback)
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
            remoteDataSource.uploadNote(username, password, note)
        }
    }

    override fun archiveNote(note: Note) {
        localDataSource.archiveNote(note)
    }

    override fun archiveNote(noteId: Int) {
        localDataSource.archiveNote(noteId)
    }

    override fun activateNote(note: Note) {
        localDataSource.activateNote(note)
    }

    override fun activateNote(noteId: Int) {
        localDataSource.activateNote(noteId)
    }

    override fun deleteNote(note: Note) {
        localDataSource.deleteNote(note)
    }

    override fun deleteNote(noteId: Int) {
        localDataSource.deleteNote(noteId)
    }
}
