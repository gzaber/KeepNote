package com.gzaber.keepnote.data.repository.model

import java.util.Date

data class Folder(
    val id: Int? = null,
    val name: String,
    val color: Int,
    val date: Date
)
