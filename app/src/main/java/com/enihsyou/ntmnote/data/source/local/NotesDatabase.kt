package com.enihsyou.ntmnote.data.source.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.enihsyou.ntmnote.data.Converters
import com.enihsyou.ntmnote.data.Note

@Database(entities = [Note::class], version = 1)
@TypeConverters(Converters::class)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun notesDao(): NotesDAO

    companion object {

        private var INSTANCE: NotesDatabase? = null

        private val lock = Any()

        fun getInstance(context: Context): NotesDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        NotesDatabase::class.java, "Notes.db"
                    ).build()
                }
                return INSTANCE!!
            }
        }
    }
}
