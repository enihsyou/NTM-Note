package com.enihsyou.ntmnote.notes

import com.enihsyou.ntmnote.data.Note
import com.enihsyou.ntmnote.data.NoteStatus
import com.enihsyou.ntmnote.data.source.NotesDataSource

class NotesPresenter(
    private val notesDataSource: NotesDataSource,
    private val fragment: NotesContract.View
) : NotesContract.Presenter {

    init {
        fragment.presenter = this
    }

    override var currentFiltering: NotesFilterType = NotesFilterType.ALL_NOTES

    override fun changeFilterType(type: NotesFilterType) {
        currentFiltering = type
        loadNotes(false)
    }

    private var firstLoad = true

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
        loadNotes(true)
        firstLoad = false
    }

    private fun loadNotes(showLoadingUi: Boolean) {
        if (showLoadingUi) {
            fragment.setLoadingIndicator(true)
        }

        notesDataSource.getNotes(object : NotesDataSource.LoadNotesCallback {
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

                processNotes(notesToShow)
            }
        }, object : NotesDataSource.SourceErrorCallback {
            override fun onDataNotAvailable() {
                if (!firstLoad) fragment.showLoadingError()
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
    }

    override fun openNoteDetail(clickedNote: Note) {
        fragment.actionNoteDetailsUi(clickedNote.id)
    }

    override fun archiveNote(archivedNote: Note) {
        notesDataSource.archiveNote(archivedNote)
        fragment.showNoteMarkedArchived(archivedNote)
        loadNotes()
    }

    override fun deleteNote(deletedNote: Note) {
        notesDataSource.deleteNote(deletedNote)
        fragment.showNoteMarkedDeleted(deletedNote)
        loadNotes()
    }
}
