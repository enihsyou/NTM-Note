package com.enihsyou.ntmnote.data.source.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.enihsyou.ntmnote.data.Note

@Dao
interface NotesDAO {

    @Insert
    fun insertNote(note: Note)

    @Query("select * from Note")
    fun getNotes(): List<Note>

    @Query("select * from Note where note_id = :noteId")
    fun getNote(noteId: Int): Note?

    @Update
    fun updateNote(note: Note)

    @Query("update Note set status = 0 where note_id = :noteId")
    fun updateActivatedNote(noteId: Int)

    @Query("update Note set status = 1 where note_id = :noteId")
    fun updateArchivedNote(noteId: Int)

    @Query("update Note set status = 2 where note_id = :noteId")
    fun updateDeletedNote(noteId: Int)

    @Delete
    fun deleteNote(note: Note)
}
