package com.enihsyou.ntmnote.notedetail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.enihsyou.ntmnote.R
import com.enihsyou.ntmnote.modifynote.ModifyNoteActivity
import com.enihsyou.ntmnote.utils.showSnackBar
import kotlinx.android.synthetic.main.activity_detail_note.*
import kotlinx.android.synthetic.main.fragment_detail_note.*
import kotlinx.android.synthetic.main.fragment_detail_note.view.*

class NoteDetailFragment : Fragment(), NoteDetailContract.View {

    override lateinit var presenter: NoteDetailContract.Presenter

    private lateinit var label: TextView
    private lateinit var content: TextView
    private lateinit var createdTime: TextView
    private lateinit var modifiedTime: TextView
    private lateinit var alarmTime: TextView

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_detail_note, container, false)
        with(root) {
            label = note_label
            content = note_content
            createdTime = created_time
            modifiedTime = last_modified_time
            alarmTime = alarm_time
        }

        requireActivity().fab_modify_note.setOnClickListener {
            presenter.editNote()
        }

        setHasOptionsMenu(true)
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.note_detail_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_note  -> presenter.deleteNote()

            R.id.archive_note -> presenter.archiveNote()

            else              -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) return
        when (requestCode) {
            REQ_EDIT_NOTE -> activity?.finish()
        }
    }

    override fun actionEditNote(noteId: Int) {
        val intent = ModifyNoteActivity.newIntent(context, noteId)
        startActivityForResult(intent, REQ_EDIT_NOTE)
    }

    override fun setLabel(label: String) {
        this.label.text = label
    }

    override fun setContent(content: String) {
        this.content.text = content
    }

    override fun setCreatedTime(time: String) {
        this.createdTime.text = time
    }

    override fun setModifiedTime(time: String) {
        this.modifiedTime.text = time
    }

    override fun setAlarmTime(time: String?) {
        if (time == null) {
            this.linearLayout5.visibility = View.GONE
        } else {
            this.linearLayout5.visibility = View.VISIBLE
            this.alarmTime.text = time
        }
    }

    override fun showNoteArchived() {
        showSnackBar("Note marked Archived")
    }

    override fun showNoteDeleted() {
        showSnackBar("Note marked Deleted")
    }

    override fun showMissingNote() {
        showSnackBar("Note Empty")
    }

    companion object {

        private const val REQ_EDIT_NOTE = 2

        @JvmStatic
        fun newInstance() =
            NoteDetailFragment()
    }
}
