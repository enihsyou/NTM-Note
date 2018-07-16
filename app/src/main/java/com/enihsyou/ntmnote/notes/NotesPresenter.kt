package com.enihsyou.ntmnote.notes

import com.enihsyou.ntmnote.data.Note
import com.enihsyou.ntmnote.data.NoteStatus
import com.enihsyou.ntmnote.data.source.NotesDataSource

class NotesPresenter(
    private val dataSource: NotesDataSource,
    private val fragment: NotesContract.View
) : NotesContract.Presenter {

    init {
        fragment.presenter = this
    }

    override var currentFiltering: NotesFilterType = NotesFilterType.ALL_NOTES

    override fun changeFilterType(type: NotesFilterType) {
        currentFiltering = type
        loadNotes(false, false)
    }

    override fun addNoteResult() {
        fragment.showSuccessfullySavedMessage()
    }

    override fun modifyNoteResult() {
        fragment.showSuccessfullyUpdatedMessage()
    }

    override fun start() {
        loadNotes()
    }

    override fun loadNotes() {
        loadNotes(true, false)
    }

    private fun loadNotes(showLoadingUi: Boolean, force: Boolean = false) {
        if (showLoadingUi) {
            fragment.setLoadingIndicator(true)
        }

        dataSource.getNotes(force, object : NotesDataSource.LoadNotesCallback {
            override fun onNotesLoaded(notes: List<Note>) {
                val notesToShow = mutableListOf<Note>()

                for (note in notes) {
                    when (currentFiltering) {
                        NotesFilterType.ALL_NOTES      -> if (note.status == NoteStatus.NORMAL) notesToShow.add(note)
                        NotesFilterType.ALARM_NOTES    -> if (note.alarmTime != null) notesToShow.add(note)
                        NotesFilterType.ARCHIVED_NOTES -> if (note.status == NoteStatus.ARCHIVED) notesToShow.add(note)
                        NotesFilterType.DELETED_NOTES  -> if (note.status == NoteStatus.TRASH) notesToShow.add(note)
                    }
                }
                if (force) fragment.showLoadingSuccess()
                processNotes(notesToShow)
            }
        }, object : NotesDataSource.SourceErrorCallback {
            override fun onDataNotAvailable(msg: String) {
                if (force) fragment.showLoadingError(msg)
            }
        })

        if (showLoadingUi) {
            fragment.setLoadingIndicator(false)
        }
    }

    private fun processNotes(notes: List<Note>) {
        if (notes.isEmpty()) {
            processEmptyNotes()
        } else {
            showFilterLabel()
            fragment.showNotes(notes)
        }
    }

    private fun showFilterLabel() {
        when (currentFiltering) {
            NotesFilterType.ALL_NOTES      -> fragment.showNonFilterLabel()
            NotesFilterType.ALARM_NOTES    -> fragment.showAlarmFilterLabel()
            NotesFilterType.ARCHIVED_NOTES -> fragment.showArchivedFilterLabel()
            NotesFilterType.DELETED_NOTES  -> fragment.showDeletedFilterLabel()
        }
    }

    private fun processEmptyNotes() {
        when (currentFiltering) {
            NotesFilterType.ALL_NOTES      -> fragment.showNoNotes()
            NotesFilterType.ALARM_NOTES    -> fragment.showNoAlarmNotes()
            NotesFilterType.ARCHIVED_NOTES -> fragment.showNoArchivedNotes()
            NotesFilterType.DELETED_NOTES  -> fragment.showNoDeletedNotes()
        }
    }

    override fun addNewNote() {
        fragment.actionAddNote()
        loadNotes(false, false)
    }

    override fun openNoteDetail(clickedNote: Note) {
        fragment.actionNoteDetailsUi(clickedNote.id)
    }

    override fun archiveNote(archivedNote: Note) {
        dataSource.archiveNote(archivedNote.id)
        fragment.showNoteMarkedArchived(archivedNote)
        loadNotes(false, false)
    }

    override fun deleteNote(deletedNote: Note) {
        dataSource.deleteNote(deletedNote.id)
        fragment.showNoteMarkedDeleted(deletedNote)
        loadNotes(false, false)
    }

    override fun sync() {
        loadNotes(true, true)
    }
}
