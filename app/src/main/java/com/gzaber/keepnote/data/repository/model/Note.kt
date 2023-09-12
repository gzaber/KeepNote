package com.gzaber.keepnote.data.repository.model

import java.util.Date

data class Note(
    val id: Int? = null,
    val folderId: Int? = null,
    val title: String,
    val content: String,
    val color: Int,
    val date: Date = Date()
)
