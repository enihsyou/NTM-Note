package com.enihsyou.ntmnote.modifynote

import com.enihsyou.ntmnote.BasePresenter
import com.enihsyou.ntmnote.BaseView
import java.util.*

interface ModifyNoteContract {
    interface View : BaseView<Presenter> {
        fun setLabel(label: String)
        fun setContent(content: String)
        fun showEmptyError()
        fun showModifyActionSuccess()
        fun showDateTimePicker(alarmTime: Long?)
    }

    interface Presenter : BasePresenter {
        fun saveNote(label: String, content: String, alarm: Date?)
        fun populateNote()
        fun setCalendar()
    }
}
