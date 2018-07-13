package com.enihsyou.ntmnote.notedetail

import com.enihsyou.ntmnote.BasePresenter
import com.enihsyou.ntmnote.BaseView

interface NoteDetailContract {
    interface View : BaseView<Presenter> {
        fun actionEditNote(noteId: Int)
        fun setLabel(label: String)
        fun setContent(content: String)
        fun setCreatedTime(time:String)
        fun setModifiedTime(time:String)
        fun setAlarmTime(time:String?)
        fun showNoteArchived()
        fun showNoteDeleted()
        fun showMissingNote()
    }

    interface Presenter : BasePresenter {
        fun editNote()
        fun archiveNote()
        fun deleteNote()
    }
}

