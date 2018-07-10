package com.enihsyou.ntmnote.domain

import java.time.LocalDateTime

class Note(
    private val id: Int,
    private var label: String,
    private var content: String,
    private var status: NoteStatus,
    private val createdTime: LocalDateTime,
    private var lastModifiedTime: LocalDateTime
) {

    fun isDeleted() = status == NoteStatus.TRASH
}

enum class NoteStatus(val raw: Int) {
    NORMAL(0x00),
    TRASH(0x00)
}
