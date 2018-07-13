package com.enihsyou.ntmnote.notes

import com.enihsyou.ntmnote.data.Note
import com.enihsyou.ntmnote.data.source.NotesDataSource

class NotesPresenter(
    private val notesDataSource: NotesDataSource,
    private val notesView: NotesContract.View
) : NotesContract.Presenter {

    init {
        notesView.presenter = this
    }

    private var firstLoad = true

    override fun addNoteResult() {
        notesView.showSuccessfullySavedMessage()
    }

    override fun modifyNoteResult() {
        notesView.showSuccessfullyUpdatedMessage()
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
            notesView.setLoadingIndicator(true)
        }

        notesDataSource.getNotes(object : NotesDataSource.LoadNotesCallback {
            override fun onNotesLoaded(notes: List<Note>) {
                processNotes(notes)
            }
        }, object : NotesDataSource.SourceErrorCallback {
            override fun onDataNotAvailable() {
                if (!firstLoad) notesView.showLoadingError()
            }
        })

        if (showLoadingUi) {
            notesView.setLoadingIndicator(false)
        }
    }

    private fun processNotes(notes: List<Note>) {
        if (notes.isEmpty()) {
            processEmptyNotes()
        } else {
            notesView.showNotes(notes)
        }
    }

    private fun processEmptyNotes() {
        notesView.showNoNotes()
    }

    override fun addNewNote() {
        notesView.actionAddNote()
    }

    override fun openNoteDetail(clickedNote: Note) {
        notesView.actionNoteDetailsUi(clickedNote.id)
    }

    override fun archiveNote(archivedNote: Note) {
        notesDataSource.archiveNote(archivedNote)
        notesView.showNoteMarkedArchived(archivedNote)
        loadNotes()
    }

    override fun deleteNote(deletedNote: Note) {
        notesDataSource.deleteNote(deletedNote)
        notesView.showNoteMarkedDeleted(deletedNote)
        loadNotes()
    }
}
