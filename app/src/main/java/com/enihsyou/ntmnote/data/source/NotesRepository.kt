package com.enihsyou.ntmnote.data.source

import com.enihsyou.ntmnote.data.Note

class NotesRepository(
    val notesLocalDataSource: NotesDataSource
) : NotesDataSource {
    override fun getNotes(
        callback: NotesDataSource.LoadNotesCallback,
        errorCallback: NotesDataSource.SourceErrorCallback?
    ) {
        TODO("not implemented") // todo
    }

    override fun getNote(
        noteId: Int,
        callback: NotesDataSource.GetNoteCallback,
        errorCallback: NotesDataSource.SourceErrorCallback?
    ) {
        TODO("not implemented") // todo
    }

    override fun saveNote(note: Note) {
        TODO("not implemented") // todo
    }

    override fun archiveNote(note: Note) {
        TODO("not implemented") // todo
    }

    override fun archiveNote(noteId: Int) {
        TODO("not implemented") // todo
    }

    override fun activateNote(note: Note) {
        TODO("not implemented") // todo
    }

    override fun activateNote(noteId: Int) {
        TODO("not implemented") // todo
    }

    override fun deleteNote(note: Note) {
        TODO("not implemented") // todo
    }

    override fun deleteNote(noteId: Int) {
        TODO("not implemented") // todo
    }
}
