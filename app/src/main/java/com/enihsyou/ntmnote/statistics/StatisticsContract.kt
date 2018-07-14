package com.enihsyou.ntmnote.statistics

import com.enihsyou.ntmnote.BasePresenter
import com.enihsyou.ntmnote.BaseView

interface StatisticsContract {

    class Statistics(
        val numberOfNotes: Int,
        val numberOfAlarmNotes: Int,
        val numberOfArchivedNotes: Int,
        val numberOfDeletedNotes: Int
    )

    interface View : BaseView<Presenter> {

        fun showStatistics(statistics: Statistics)
    }

    interface Presenter : BasePresenter
}
