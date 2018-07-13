package com.enihsyou.ntmnote.notes

import android.content.Context
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.enihsyou.ntmnote.R
import com.enihsyou.ntmnote.data.source.NotesDataSource
import com.enihsyou.ntmnote.data.source.local.NotesDatabase
import com.enihsyou.ntmnote.data.source.local.NotesLocalDataSource
import com.enihsyou.ntmnote.utils.AppExecutors
import com.enihsyou.ntmnote.utils.replaceFragmentInActivity
import com.enihsyou.ntmnote.utils.setupActionBar
import kotlinx.android.synthetic.main.activity_notes.*

class NotesActivity : AppCompatActivity() {

    private lateinit var notesPresenter: NotesPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        // Set up the toolbar.
        setupActionBar(R.id.toolbar) {
            setHomeAsUpIndicator(R.drawable.ic_menu)
            setDisplayHomeAsUpEnabled(true)
        }

        // Set up the navigation drawer.
        setUpDrawerContent(nav_view)

        val notesFragment = supportFragmentManager.findFragmentById(R.id.contentFrame) as? NotesFragment
            ?: NotesFragment.newInstance().also { replaceFragmentInActivity(it, R.id.contentFrame) }

        notesPresenter =
            NotesPresenter(
                NotesLocalDataSource.getInstance(
                    AppExecutors(),
                    NotesDatabase.getInstance(applicationContext).notesDao()
                ), notesFragment
            )
    }

    private fun setUpDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener {
            // Handle navigation view item clicks here.
            val fragment = when (it.itemId) {
                R.id.nav_note    -> {
                }

                R.id.nav_remind  -> {
                }

                R.id.nav_trash   -> {
                }

                R.id.nav_setting -> {

                }

                R.id.nav_about   -> {

                }

                else             -> {
                }
            }
            val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction().replace(R.id.contentFrame, Fragment()).commit()

            it.isChecked = true
            drawer_layout.closeDrawer(GravityCompat.START)
            true
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            // Open the navigation drawer when the home icon is selected from the toolbar.
            drawer_layout.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

object Injection {
    fun provideTasksRepository(context: Context): NotesDataSource {
        val database = NotesDatabase.getInstance(context)
        return NotesLocalDataSource.getInstance(AppExecutors(), database.notesDao())
    }
}