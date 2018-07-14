package com.enihsyou.ntmnote.modifynote

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.enihsyou.ntmnote.R
import com.enihsyou.ntmnote.ui.Injection
import com.enihsyou.ntmnote.utils.replaceFragmentInActivity
import com.enihsyou.ntmnote.utils.setupActionBar
import kotlinx.android.synthetic.main.activity_modify_note.*

class ModifyNoteActivity : AppCompatActivity() {

    private lateinit var modifyNotePresenter: ModifyNotePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_note)
        setSupportActionBar(toolbar)

        val noteId = intent.getIntExtra(EXT_NOTE_ID, 0)

        setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = if (taskId == 0) "新建笔记" else "编辑笔记"
        }

        val modifyNoteFragment =
            supportFragmentManager.findFragmentById(R.id.contentFrame) as? ModifyNoteFragment
                ?: ModifyNoteFragment.newInstance().also { replaceFragmentInActivity(it, R.id.contentFrame) }

        modifyNotePresenter =
            ModifyNotePresenter(noteId, Injection.provideTasksRepository(applicationContext), modifyNoteFragment)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {

        private const val EXT_NOTE_ID = "NOTE_ID"

        @JvmStatic
        fun newIntent(parent: Context?, noteId: Int): Intent =
            Intent(parent, ModifyNoteActivity::class.java).apply {
                putExtra(EXT_NOTE_ID, noteId)
            }
    }
}
