package com.enihsyou.ntmnote.data.source.local

import com.enihsyou.ntmnote.data.Note
import com.enihsyou.ntmnote.data.source.NotesDataSource
import com.enihsyou.ntmnote.utils.AppExecutors
import java.util.*

class NotesLocalDataSource private constructor(
    private val notesDAO: NotesDAO,
    private val appExecutors: AppExecutors
) : NotesDataSource {

    override fun getNotes(
        force: Boolean,
        callback: NotesDataSource.LoadNotesCallback,
        errorCallback: NotesDataSource.SourceErrorCallback?
    ) {
        appExecutors.diskIO.execute {
            val notes = notesDAO.getNotes()
            appExecutors.mainThread.execute {
                if (notes.isNotEmpty()) {
                    callback.onNotesLoaded(notes)
                } else {
                    errorCallback?.onDataNotAvailable()
                }
            }
        }
    }

    override fun getNote(
        noteId: Int,
        callback: NotesDataSource.GetNoteCallback,
        errorCallback: NotesDataSource.SourceErrorCallback?
    ) {
        appExecutors.diskIO.execute {
            if (noteId == 0) return@execute
            val note = notesDAO.getNote(noteId)
            appExecutors.mainThread.execute {
                if (note != null) {
                    callback.onNoteLoaded(note)
                } else {
                    errorCallback?.onDataNotAvailable()
                }
            }
        }
    }

    override fun saveNote(note: Note) {
        appExecutors.diskIO.execute {
            if (note.id == 0) {
                note.id = Random().nextInt()
                notesDAO.insertNote(note)
            } else {
                notesDAO.updateNote(note)
            }
        }
    }

    override fun archiveNote(note: Note) {
        archiveNote(note.id)
    }

    override fun archiveNote(noteId: Int) {
        appExecutors.diskIO.execute { notesDAO.updateArchivedNote(noteId) }
    }

    override fun activateNote(note: Note) {
        activateNote(note.id)
    }

    override fun activateNote(noteId: Int) {
        appExecutors.diskIO.execute { notesDAO.updateActivatedNote(noteId) }
    }

    override fun deleteNote(note: Note) {
        deleteNote(note.id)
    }

    override fun deleteNote(noteId: Int) {
        appExecutors.diskIO.execute { notesDAO.updateDeletedNote(noteId) }
    }

    companion object {
        private var INSTANCE: NotesLocalDataSource? = null

        @JvmStatic
        fun getInstance(appExecutors: AppExecutors, notesDAO: NotesDAO): NotesLocalDataSource {
            if (INSTANCE == null) {
                synchronized(NotesLocalDataSource::javaClass) {
                    INSTANCE = NotesLocalDataSource(notesDAO, appExecutors)
                }
            }
            return INSTANCE!!
        }
    }
}

