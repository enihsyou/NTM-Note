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
        fun showNoAlarmNotes()
        fun showNoArchivedNotes()
        fun showNoDeletedNotes()

        fun showNonFilterLabel()
        fun showAlarmFilterLabel()
        fun showArchivedFilterLabel()
        fun showDeletedFilterLabel()

        fun showNoteMarkedArchived(archivedNote: Note)

        fun showNoteMarkedDeleted(deletedNote: Note)

        fun showSuccessfullySavedMessage()

        fun showSuccessfullyUpdatedMessage()

        fun showLoadingSuccess()
        fun showLoadingError(msg: String)
    }

    interface Presenter : BasePresenter {

        var currentFiltering: NotesFilterType

        fun changeFilterType(type: NotesFilterType)

        fun addNoteResult()

        fun modifyNoteResult()

        fun loadNotes()

        fun addNewNote()

        fun openNoteDetail(clickedNote: Note)

        fun archiveNote(archivedNote: Note)
        fun deleteNote(deletedNote: Note)

        fun sync()
    }
}
