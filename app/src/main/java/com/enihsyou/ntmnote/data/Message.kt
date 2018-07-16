package com.enihsyou.ntmnote.data

data class Message<T>(
    val msg: String?,

    val body: T?
)
