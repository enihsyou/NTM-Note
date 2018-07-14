package com.enihsyou.ntmnote.notes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.enihsyou.ntmnote.R
import com.enihsyou.ntmnote.ui.AboutActivity
import com.enihsyou.ntmnote.ui.Injection
import com.enihsyou.ntmnote.utils.replaceFragmentInActivity
import com.enihsyou.ntmnote.utils.setupActionBar
import kotlinx.android.synthetic.main.activity_notes.*
import kotlinx.android.synthetic.main.nav_header.view.*

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
        drawer_layout?.setStatusBarBackground(R.color.colorPrimaryDark)
        setUpDrawerContent(nav_view)

        val notesFragment = supportFragmentManager.findFragmentById(R.id.contentFrame) as? NotesFragment
            ?: NotesFragment.newInstance().also { replaceFragmentInActivity(it, R.id.contentFrame) }

        notesPresenter = NotesPresenter(Injection.provideTasksRepository(applicationContext), notesFragment)
    }

    private fun setUpDrawerContent(navigationView: NavigationView) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)

        navigationView.getHeaderView(0).apply {
            setOnClickListener {
                val username = preferences.getString("username", null)
                if (username == null)
                    startActivityForResult(Intent(this@NotesActivity, LoginActivity::class.java), REQ_LOGIN)
                else {
                    preferences.edit().clear().apply()
                    Toast.makeText(applicationContext, "注销成功", Toast.LENGTH_SHORT).show()
                    nav_username.text = "未登录"
                }
            }
            val username = preferences.getString("username", null)
            nav_username.text = username ?: "未登录"
        }

        navigationView.setNavigationItemSelectedListener {
            // Handle navigation view item clicks here.
            when (it.itemId) {
                R.id.nav_note     -> notesPresenter.changeFilterType(NotesFilterType.ALL_NOTES)
                R.id.nav_remind   -> notesPresenter.changeFilterType(NotesFilterType.ALARM_NOTES)
                R.id.nav_archived -> notesPresenter.changeFilterType(NotesFilterType.ARCHIVED_NOTES)
                R.id.nav_trash    -> notesPresenter.changeFilterType(NotesFilterType.DELETED_NOTES)
                R.id.nav_about    -> startActivity(Intent(this, AboutActivity::class.java))
            }

            it.isChecked = true
            drawer_layout.closeDrawer(GravityCompat.START)
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQ_LOGIN -> if (resultCode == Activity.RESULT_OK) showLoginSuccessful()
            else      -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun showLoginSuccessful() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val username = preferences.getString("username", "未登录")
        nav_view.getHeaderView(0).nav_username.text = username
        Snackbar.make(contentFrame, "登录成功", Snackbar.LENGTH_SHORT).show()
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

    companion object {
        private const val REQ_LOGIN = 4
    }
}

