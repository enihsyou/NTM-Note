package com.enihsyou.ntmnote.data.source.local

import android.content.Context
import com.enihsyou.ntmnote.R
import com.enihsyou.ntmnote.data.Note
import com.enihsyou.ntmnote.data.source.NotesDataSource
import com.enihsyou.ntmnote.utils.AppExecutors
import java.util.*

class NotesLocalDataSource private constructor(
    private val notesDAO: NotesDAO,
    private val appExecutors: AppExecutors,
    content: Context
) : NotesDataSource {

    private val stringNoteLoadFail = content.getString(R.string.note_load_fail) ?: "Failed load string"

    override fun getNotes(
        force: Boolean,
        callback: NotesDataSource.LoadNotesCallback,
        errorCallback: NotesDataSource.SourceErrorCallback?
    ) {
        appExecutors.diskIO.execute {
            val notes = notesDAO.getNotes()
            appExecutors.mainThread.execute {
                callback.onNotesLoaded(notes)
//                errorCallback?.onDataNotAvailable(stringNoteLoadFail)
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
                    errorCallback?.onDataNotAvailable(stringNoteLoadFail)
                }
            }
        }
    }

    override fun saveNote(note: Note) {
        appExecutors.diskIO.execute {
            note.id = Random().nextInt()
            notesDAO.insertNote(note)
        }
    }

    override fun updateNote(note: Note) {
        appExecutors.diskIO.execute {
            notesDAO.updateNote(note)
        }
    }

    override fun archiveNote(noteId: Int) {
        appExecutors.diskIO.execute { notesDAO.updateArchivedNote(noteId) }
    }

    override fun activateNote(noteId: Int) {
        appExecutors.diskIO.execute { notesDAO.updateActivatedNote(noteId) }
    }

    override fun deleteNote(noteId: Int) {
        appExecutors.diskIO.execute { notesDAO.updateDeletedNote(noteId) }
    }

    companion object {
        private var INSTANCE: NotesLocalDataSource? = null

        @JvmStatic
        fun getInstance(appExecutors: AppExecutors, notesDAO: NotesDAO, content: Context): NotesLocalDataSource {
            if (INSTANCE == null) {
                synchronized(NotesLocalDataSource::javaClass) {
                    INSTANCE = NotesLocalDataSource(notesDAO, appExecutors, content)
                }
            }
            return INSTANCE!!
        }
    }
}

