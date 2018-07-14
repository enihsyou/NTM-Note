package com.enihsyou.ntmnote.statistics

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.enihsyou.ntmnote.R
import com.enihsyou.ntmnote.ui.Injection
import com.enihsyou.ntmnote.utils.replaceFragmentInActivity
import com.enihsyou.ntmnote.utils.setupActionBar

class StatisticsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        setupActionBar(R.id.toolbar) {
            setTitle(R.string.statistics_activity_title)
            setHomeAsUpIndicator(R.drawable.ic_menu)
            setDisplayHomeAsUpEnabled(true)
        }

        val statisticsFragment = supportFragmentManager.findFragmentById(R.id.contentFrame) as? StatisticsFragment
            ?: StatisticsFragment.newInstance().also { replaceFragmentInActivity(it, R.id.contentFrame) }

        StatisticsPresenter(Injection.provideTasksRepository(applicationContext), statisticsFragment)
    }
}
