package com.gzaber.keepnote.ui.util.model

import androidx.compose.ui.graphics.toArgb
import com.gzaber.keepnote.ui.theme.ColorSelectorColor1
import java.util.Date

data class Element(
    val id: Int?,
    val folderId: Int?,
    val isNote: Boolean,
    val name: String,
    val content: String,
    val color: Int,
    val date: Date,
) {
    companion object Factory {
        fun empty(): Element {
            return Element(
                id = null,
                folderId = null,
                isNote = true,
                name = "",
                content = "",
                color = ColorSelectorColor1.toArgb(),
                date = Date()
            )
        }
    }
}