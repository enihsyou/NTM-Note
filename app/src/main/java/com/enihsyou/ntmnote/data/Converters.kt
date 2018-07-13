package com.enihsyou.ntmnote.data

import android.arch.persistence.room.TypeConverter
import android.text.format.DateFormat
import java.util.*

object Converters {
    @TypeConverter
    @JvmStatic
    fun fromStatus(status: NoteStatus): Int {
        return status.raw
    }

    @TypeConverter
    @JvmStatic
    fun toStatus(raw: Int): NoteStatus {
        return NoteStatus.values().first { it.raw == raw }
    }

    @TypeConverter
    @JvmStatic
    fun fromTimestamp(value: Long): Date? {
        return if (value != 0L) Date(value) else null
    }

    @TypeConverter
    @JvmStatic
    fun ToTimestamp(date: Date?): Long {
        return date?.time ?: 0L
    }

    fun getDateString(date: Date): String {
        return DateFormat.format("yyyy-MM-dd hh:mm:ss", date).toString()
    }

    fun getDateStringShort(date: Date): String {
        return DateFormat.format("yyyy-MM-dd", date).toString()
    }
}
