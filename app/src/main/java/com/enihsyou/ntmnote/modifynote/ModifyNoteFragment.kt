package com.enihsyou.ntmnote.modifynote

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.enihsyou.ntmnote.R
import com.enihsyou.ntmnote.utils.showSnackBar
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.activity_modify_note.*
import kotlinx.android.synthetic.main.fragment_modify_note.view.*
import java.util.*

class ModifyNoteFragment : Fragment(), ModifyNoteContract.View {

    override lateinit var presenter: ModifyNoteContract.Presenter

    private lateinit var label: EditText
    private lateinit var content: EditText
    private var alarm: Date? = null

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_modify_note, container, false)
        with(root) {
            label = note_label
            content = note_content
        }

        setHasOptionsMenu(true)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity().fab_set_calendar.apply {
            setOnClickListener { presenter.setCalendar() }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_modify_note, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_done -> presenter.saveNote(label.text.toString(), content.text.toString(), alarm)

            else             -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun setLabel(label: String) {
        this.label.setText(label)
    }

    override fun setContent(content: String) {
        this.content.setText(content)
    }

    override fun showEmptyError() {
        showSnackBar("笔记不能是空的")
    }

    override fun showModifyActionSuccess() {
        requireActivity().apply {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    override fun showDateTimePicker(alarmTime: Long?) {
        val instance = Calendar.getInstance()
        alarm?.let { instance.time = alarm }

        var pickedYear = instance.get(Calendar.YEAR)
        var pickedMonth = instance.get(Calendar.MONTH)
        var pickedDay = instance.get(Calendar.DAY_OF_MONTH)
        var pickedHour = instance.get(Calendar.HOUR_OF_DAY)
        var pickedMinute = instance.get(Calendar.MINUTE)


        val timePickerDialog = TimePickerDialog.newInstance({ _, hourOfDay, minute, second ->
            pickedHour = hourOfDay
            pickedMinute = minute

            val calendar = Calendar.getInstance()

            calendar.set(pickedYear, pickedMonth, pickedDay, pickedHour, pickedMinute)
            alarm = calendar.time
        }, pickedHour, pickedMinute, true)

        val datePickerDialog = DatePickerDialog.newInstance({ _, year, monthOfYear, dayOfMonth ->
            pickedYear = year
            pickedMonth = monthOfYear
            pickedDay = dayOfMonth
            timePickerDialog.show(fragmentManager, "TimePickerDialog")
        }, pickedYear, pickedMonth, pickedDay)

        datePickerDialog.showNow(fragmentManager, "DatePickerDialog")
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ModifyNoteFragment()
    }
}

