package com.enihsyou.ntmnote.notedetail

import com.enihsyou.ntmnote.data.Converters
import com.enihsyou.ntmnote.data.Note
import com.enihsyou.ntmnote.data.source.NotesDataSource

class NoteDetailPresenter(
    private val noteId: Int,
    private val dataSource: NotesDataSource,
    private val fragment: NoteDetailContract.View
) : NoteDetailContract.Presenter {

    init {
        fragment.presenter = this
    }

    override fun start() {
        dataSource.getNote(noteId, object : NotesDataSource.GetNoteCallback {
            override fun onNoteLoaded(note: Note) {
                with(fragment) {
                    setLabel(note.label)
                    setContent(note.content)
                    setCreatedTime(Converters.getDateString(note.createdTime))
                    setModifiedTime(Converters.getDateString(note.lastModifiedTime))
                    setAlarmTime(note.alarmTime?.let { Converters.getDateString(it)} )
                }
            }
        }, object : NotesDataSource.SourceErrorCallback {
            override fun onDataNotAvailable() {
                fragment.showMissingNote()
            }
        })
    }

    override fun editNote() {
        fragment.actionEditNote(noteId)
    }

    override fun archiveNote() {
        dataSource.archiveNote(noteId)
        fragment.showNoteArchived()
    }

    override fun deleteNote() {
        dataSource.deleteNote(noteId)
        fragment.showNoteDeleted()
    }

    override fun activateNote() {
        dataSource.activateNote(noteId)
        fragment.showNoteActivated()
    }
}
