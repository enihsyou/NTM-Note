package com.enihsyou.ntmnote.notedetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.enihsyou.ntmnote.R
import com.enihsyou.ntmnote.utils.Injection
import com.enihsyou.ntmnote.utils.replaceFragmentInActivity
import com.enihsyou.ntmnote.utils.setupActionBar

class NoteDetailActivity : AppCompatActivity() {

    private lateinit var noteDetailPresenter: NoteDetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_note)

        val noteId = intent.getIntExtra(NoteDetailActivity.EXT_NOTE_ID, 0)

        setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "备忘详情"
        }

        val detailNoteFragment =
            supportFragmentManager.findFragmentById(R.id.contentFrame) as? NoteDetailFragment
                ?: NoteDetailFragment.newInstance().also { replaceFragmentInActivity(it, R.id.contentFrame) }

        noteDetailPresenter =
            NoteDetailPresenter(noteId, Injection.provideTasksRepository(applicationContext), detailNoteFragment)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {

        private const val EXT_NOTE_ID = "NOTE_ID"

        @JvmStatic
        fun newIntent(parent: Context?, noteId: Int): Intent =
            Intent(parent, NoteDetailActivity::class.java).apply {
                putExtra(EXT_NOTE_ID, noteId)
            }
    }
}
