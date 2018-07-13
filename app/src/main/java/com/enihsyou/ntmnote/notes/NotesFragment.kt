package com.enihsyou.ntmnote.notes

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.enihsyou.ntmnote.R
import com.enihsyou.ntmnote.data.Converters
import com.enihsyou.ntmnote.data.Note
import com.enihsyou.ntmnote.modifynote.ModifyNoteActivity
import com.enihsyou.ntmnote.notedetail.NoteDetailActivity
import com.enihsyou.ntmnote.utils.OnSwipeTouchListener
import com.enihsyou.ntmnote.utils.showSnackBar
import kotlinx.android.synthetic.main.activity_notes.*
import kotlinx.android.synthetic.main.fragment_notes.*
import kotlinx.android.synthetic.main.fragment_notes.view.*
import kotlinx.android.synthetic.main.note_item.view.*

class NotesFragment : Fragment(), NotesContract.View {

    override lateinit var presenter: NotesContract.Presenter

    private val itemListener = object : NoteItemListener {
        override fun onItemClick(clickedNote: Note) {
            presenter.openNoteDetail(clickedNote)
        }

        override fun onArchivedAction(archivedNote: Note) {
            presenter.archiveNote(archivedNote)
        }

        override fun onDeleteAction(deletedNote: Note) {
            presenter.deleteNote(deletedNote)
        }
    }

    private val listAdapter = NotesAdapter(listOf(), itemListener)

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_notes, container, false)

        with(root) {

            // Set up progress indicator
            refresh_layout.apply {
                setOnRefreshListener { presenter.loadNotes() }
            }

            // Set up list adapter
            notes_list.apply {
                adapter = listAdapter
                layoutManager = GridLayoutManager(context, 2)
            }
        }
        // Set up floating action button
        requireActivity().fab_add_note.apply {
            setOnClickListener { presenter.addNewNote() }
        }

        setHasOptionsMenu(true)
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.notes_fragment, menu)
    }

    override fun setLoadingIndicator(active: Boolean) {
        val root = view ?: return
        with(root.refresh_layout) {
            post { isRefreshing = active }
        }
    }

    override fun showLoadingError() {
        showSnackBar("载入失败")
    }

    override fun showNotes(notes: List<Note>) {
        listAdapter.notes = notes
        notes_view.visibility = View.VISIBLE
        no_notes_view.visibility = View.GONE


        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        for (note in notes) {
            note.alarmTime?.run {
                val intent = Intent(context, AlarmReceiver::class.java)
                intent.putExtra(AlarmReceiver.EXT_NOTIFY_TITLE, note.label)
                intent.putExtra(AlarmReceiver.EXT_NOTIFY_CONTENT, note.content)
                val pendingIntent = PendingIntent.getBroadcast(context, REQ_ALARM, intent, 0)
                alarmManager.set(AlarmManager.RTC_WAKEUP, this.time, pendingIntent)
            }
        }
    }

    override fun actionAddNote() {
        val intent = ModifyNoteActivity.newIntent(context, 0)
        startActivityForResult(intent, REQ_ADD_NOTE)
    }

    override fun actionNoteDetailsUi(noteId: Int) {
        startActivity(NoteDetailActivity.newIntent(context, noteId))
    }

    override fun showNoNotes() {
        notes_view.visibility = View.GONE
        no_notes_view.visibility = View.VISIBLE
    }

    override fun showNoteMarkedArchived(archivedNote: Note) {
        showSnackBar("已归档")
    }

    override fun showNoteMarkedDeleted(deletedNote: Note) {
        showSnackBar("已移入回收站")
    }

    override fun showSuccessfullySavedMessage() {
        showSnackBar("笔记已保存")
    }

    override fun showSuccessfullyUpdatedMessage() {
        showSnackBar("笔记已更新")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) return
        when (requestCode) {
            REQ_ADD_NOTE -> presenter.addNoteResult()
        }
    }

    private class NotesViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.title
        val content: TextView = view.content
        val time: TextView = view.last_modified_time

        fun bind(note: Note, itemListener: NoteItemListener) {
            title.text = note.label
            content.text = note.content
            time.text = Converters.getDateStringShort(note.lastModifiedTime)

            view.setOnTouchListener(object : OnSwipeTouchListener(view.context) {
                override fun onClick() {
                    itemListener.onItemClick(note)
                }

                override fun onSwipeRight() {
                    itemListener.onArchivedAction(note)
                }

                override fun onSwipeLeft() {
                    itemListener.onDeleteAction(note)
                }
            })
        }
    }

    private class NotesAdapter(
        notes: List<Note>,
        private val itemListener: NoteItemListener
    ) : RecyclerView.Adapter<NotesViewHolder>() {

        var notes = notes
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)

            return NotesViewHolder(view)
        }

        override fun getItemCount(): Int {
            return notes.size
        }

        override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
            holder.bind(notes[position], itemListener)
        }
    }

    interface NoteItemListener {

        fun onItemClick(clickedNote: Note)

        fun onArchivedAction(archivedNote: Note)

        fun onDeleteAction(deletedNote: Note)
    }

    companion object {

        private const val REQ_ADD_NOTE = 1
        private const val REQ_ALARM = 4

        @JvmStatic
        fun newInstance() =
            NotesFragment()
    }
}
