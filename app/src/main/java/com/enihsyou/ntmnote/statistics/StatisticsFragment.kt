package com.enihsyou.ntmnote.statistics

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.enihsyou.ntmnote.R
import kotlinx.android.synthetic.main.fragment_statistics.view.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [StatisticsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [StatisticsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class StatisticsFragment : Fragment(), StatisticsContract.View {

    override lateinit var presenter: StatisticsContract.Presenter

    private lateinit var textView: TextView

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_statistics, container, false)
        textView = root.statistics_text
        return root
    }

    override fun showStatistics(statistics: StatisticsContract.Statistics) {
        val displayString = """
            ${getString(R.string.statistics_total_note)}: ${statistics.numberOfNotes}
            ${getString(R.string.statistics_alarm_note)}: ${statistics.numberOfAlarmNotes}
            ${getString(R.string.statistics_archived_note)}: ${statistics.numberOfArchivedNotes}
            ${getString(R.string.statistics_deleted_note)}: ${statistics.numberOfDeletedNotes}
        """.trimIndent()
        textView.text = displayString
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            StatisticsFragment()
    }
}
