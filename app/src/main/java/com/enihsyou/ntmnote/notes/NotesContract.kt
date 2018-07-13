package com.enihsyou.ntmnote.notes

import com.enihsyou.ntmnote.BasePresenter
import com.enihsyou.ntmnote.BaseView
import com.enihsyou.ntmnote.data.Note

interface NotesContract {

    interface View : BaseView<Presenter> {

        fun setLoadingIndicator(active: Boolean)

        fun showNotes(notes: List<Note>)

        fun actionAddNote()

        fun actionNoteDetailsUi(noteId: Int)

        fun showNoNotes()

        fun showNoteMarkedArchived(archivedNote: Note)

        fun showNoteMarkedDeleted(deletedNote: Note)

        fun showSuccessfullySavedMessage()

        fun showSuccessfullyUpdatedMessage()

        fun showLoadingError()
    }

    interface Presenter : BasePresenter {

        fun addNoteResult()

        fun modifyNoteResult()

        fun loadNotes()

        fun addNewNote()

        fun openNoteDetail(clickedNote: Note)

        fun archiveNote(archivedNote: Note)
        fun deleteNote(deletedNote: Note)
    }
}
