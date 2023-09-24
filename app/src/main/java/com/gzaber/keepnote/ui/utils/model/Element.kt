package com.gzaber.keepnote.ui.utils.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import java.util.Date

data class Element(
    val id: Int? = null,
    val folderId: Int? = null,
    val isNote: Boolean,
    val name: String,
    val content: String,
    val color: Int,
    val date: Date = Date()
) {
    companion object Factory {
        fun empty(): Element {
            return Element(
                isNote = true,
                name = "",
                content = "",
                color = Color.Red.toArgb()
            )
        }
    }
}