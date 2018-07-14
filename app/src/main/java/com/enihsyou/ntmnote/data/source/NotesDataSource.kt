package com.enihsyou.ntmnote.data.source

import com.enihsyou.ntmnote.data.Note

/**操作数据的主入口*/
interface NotesDataSource {

    interface LoadNotesCallback {

        fun onNotesLoaded(notes: List<Note>)
    }

    interface GetNoteCallback {

        fun onNoteLoaded(note: Note)
    }

    interface SourceErrorCallback {

        fun onDataNotAvailable()
    }

    fun getNotes(callback: LoadNotesCallback, errorCallback: SourceErrorCallback? = null)

    fun getNote(noteId: Int, callback: GetNoteCallback, errorCallback: SourceErrorCallback? = null)

    fun saveNote(note: Note)

    fun archiveNote(note: Note)
    fun archiveNote(noteId: Int)

    fun activateNote(note: Note)
    fun activateNote(noteId: Int)

    fun deleteNote(note: Note)
    fun deleteNote(noteId: Int)
}
