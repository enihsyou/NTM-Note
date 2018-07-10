package com.enihsyou.ntmnote

import android.app.Application
import android.util.Log

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initDatabase()
    }

    private fun initDatabase() {
        Log.v(TAG, "初始化数据库")
    }

    companion object {
        private const val TAG = "NTM Note Application"
    }
}
