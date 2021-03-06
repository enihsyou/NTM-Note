package com.enihsyou.ntmnote.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity
class Note(
    @ColumnInfo(name = "label") var label: String,

    @ColumnInfo(name = "content") var content: String,

    @ColumnInfo(name = "notify_time") var alarmTime: Long? = null,

    @ColumnInfo(name = "status") var status: NoteStatus = NoteStatus.NORMAL,

    @ColumnInfo(name = "created_time") val createdTime: Long = Date().time,

    @ColumnInfo(name = "last_modified_time") var lastModifiedTime: Long = createdTime
) {

    @PrimaryKey @ColumnInfo(name = "note_id") var id: Int = 0
}

enum class NoteStatus(val raw: Int) {
    NORMAL(0x00),
    ARCHIVED(0x01),
    TRASH(0x02);
}

