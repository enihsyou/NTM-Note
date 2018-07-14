package com.enihsyou.ntmnote.data

class User(
    val username: String,
    val password: String,
    val notes: MutableList<Note>
)
