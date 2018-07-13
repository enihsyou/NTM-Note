package com.enihsyou.ntmnote.modifynote

import com.enihsyou.ntmnote.data.Note
import com.enihsyou.ntmnote.data.source.NotesDataSource
import java.util.*

class ModifyNotePresenter(
    private val noteId: Int,
    private val notesRepository: NotesDataSource,
    private val fragment: ModifyNoteContract.View
) : ModifyNoteContract.Presenter {

    private var note: Note? = null

    init {
        fragment.presenter = this
    }

    override fun start() {
        populateNote()
    }

    override fun saveNote(label: String, content: String, alarm: Date?) {
        if (noteId == 0) {
            createNote(label, content, alarm)
        } else {
            updateNote(label, content, alarm)
        }
    }

    private fun createNote(label: String, content: String, alarm: Date?) {
        val newNote = Note(label, content, alarm)
        if (label.isBlank()) {
            fragment.showEmptyError()
        } else {
            notesRepository.saveNote(newNote)
            fragment.showModifyActionSuccess()
        }
    }

    private fun updateNote(label: String, content: String, alarm: Date?) {
        notesRepository.getNote(noteId, object : NotesDataSource.GetNoteCallback {
            override fun onNoteLoaded(note: Note) {
                note.apply {
                    this.label = label
                    this.content = content
                    this.alarmTime = alarm
                    this.lastModifiedTime = Date()
                }

                if (label.isBlank()) {
                    fragment.showEmptyError()
                } else {
                    notesRepository.saveNote(note)
                    fragment.showModifyActionSuccess()
                }
            }
        }, object : NotesDataSource.SourceErrorCallback {
            override fun onDataNotAvailable() {
                throw IllegalStateException()
            }
        })
    }

    override fun setCalendar() {
        fragment.showDateTimePicker(note?.alarmTime)
    }

    override fun populateNote() {
        notesRepository.getNote(noteId, object : NotesDataSource.GetNoteCallback {
            override fun onNoteLoaded(note: Note) {
                fragment.setLabel(note.label)
                fragment.setContent(note.content)
                this@ModifyNotePresenter.note = note
            }
        }, object : NotesDataSource.SourceErrorCallback {
            override fun onDataNotAvailable() {
                throw IllegalStateException()
            }
        })
    }
}
