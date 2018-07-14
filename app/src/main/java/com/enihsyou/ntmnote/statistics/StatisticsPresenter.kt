package com.enihsyou.ntmnote.statistics

import com.enihsyou.ntmnote.data.Note
import com.enihsyou.ntmnote.data.NoteStatus
import com.enihsyou.ntmnote.data.source.NotesDataSource

class StatisticsPresenter(
    private val dataSource: NotesDataSource,
    private val fragment: StatisticsContract.View
) : StatisticsContract.Presenter {

    init {
        fragment.presenter = this
    }

    override fun start() {
        loadStatistics()
    }

    private fun loadStatistics() {
        dataSource.getNotes(object : NotesDataSource.LoadNotesCallback {
            override fun onNotesLoaded(notes: List<Note>) {
                val totalNumber = notes.count()
                val alarmNumber = notes.filter { it.alarmTime != null }.count()
                val archivedNumber = notes.filter { it.status == NoteStatus.ARCHIVED }.count()
                val deletedNumber = notes.filter { it.status == NoteStatus.TRASH }.count()

                val statistics = StatisticsContract.Statistics(totalNumber, alarmNumber, archivedNumber, deletedNumber)

                fragment.showStatistics(statistics)
            }
        })
    }
}
