package com.enihsyou.ntmnote.notes

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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

    private lateinit var titleText: TextView

    private lateinit var currentLayoutManager: RecyclerView.LayoutManager

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

        currentLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        with(root) {

            // Set up progress indicator
            refresh_layout.apply {
                setOnRefreshListener { presenter.loadNotes() }
            }

            // Set up list adapter
            notes_list.apply {
                adapter = listAdapter
                layoutManager = currentLayoutManager
            }

            titleText = root.filteringLabel
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_action_change_layout -> toggleLayoutManager()
            else                           -> return super.onOptionsItemSelected(item)
        }
        return true
    }


    private fun toggleLayoutManager() {
        notes_list?.layoutManager = currentLayoutManager
        currentLayoutManager = if (currentLayoutManager::class == GridLayoutManager::class) {
            LinearLayoutManager(context)
        } else if (currentLayoutManager::class == LinearLayoutManager::class) {
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        } else {
            GridLayoutManager(context, 2)
        }
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

    override fun showNoNotes() {
        showNoNotesView(getString(R.string.no_notes_main), R.drawable.ic_fab_main_plus)
    }

    override fun showNoAlarmNotes() {
        showNoNotesView(getString(R.string.no_alarm_notes_main), R.drawable.ic_timelapse)
    }

    override fun showNoArchivedNotes() {
        showNoNotesView(getString(R.string.no_archived_notes_main), R.drawable.ic_tag_faces, false)
    }

    override fun showNoDeletedNotes() {
        showNoNotesView(getString(R.string.no_deleted_notes_main), R.drawable.ic_thumb_up, false)
    }

    override fun showNonFilterLabel() {
        titleText.text = getString(R.string.all_notes_filter_label)
    }

    override fun showAlarmFilterLabel() {
        titleText.text = getString(R.string.alarm_notes_filter_label)
    }

    override fun showArchivedFilterLabel() {
        titleText.text = getString(R.string.archived_notes_filter_label)
    }

    override fun showDeletedFilterLabel() {
        titleText.text = getString(R.string.deleted_notes_filter_label)
    }

    private fun showNoNotesView(mainText: String, @DrawableRes iconRes: Int, showAddView: Boolean = true) {
        notes_view?.visibility = View.GONE
        no_notes_view?.visibility = View.VISIBLE

        noNotesMain?.text = mainText
        noNotesIcon?.setImageResource(iconRes)
        noNotesSub?.visibility = if (showAddView) View.VISIBLE else View.GONE
    }

    override fun actionAddNote() {
        val intent = ModifyNoteActivity.newIntent(context, 0)
        startActivityForResult(intent, REQ_ADD_NOTE)
    }

    override fun actionNoteDetailsUi(noteId: Int) {
        startActivity(NoteDetailActivity.newIntent(context, noteId))
    }

    override fun showNoteMarkedArchived(archivedNote: Note) {
        showSnackBar(getString(R.string.action_archive))
    }

    override fun showNoteMarkedDeleted(deletedNote: Note) {
        showSnackBar(getString(R.string.action_delete))
    }

    override fun showSuccessfullySavedMessage() {
        showSnackBar(getString(R.string.action_note_saved))
    }

    override fun showSuccessfullyUpdatedMessage() {
        showSnackBar(getString(R.string.action_note_updated))
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
