package com.gzaber.keepnote.ui.utils.model

import com.gzaber.keepnote.data.repository.model.Folder
import com.gzaber.keepnote.data.repository.model.Note


fun Element.toNote(): Note {
    return Note(
        id = id,
        folderId = folderId,
        title = name,
        content = content,
        color = color,
        date = date
    )
}

fun Note.toElement(): Element {
    return Element(
        id = id,
        folderId = folderId,
        isNote = true,
        name = title,
        content = content,
        color = color,
        date = date
    )
}

fun Element.toFolder(): Folder {
    return Folder(
        id = id,
        name = name,
        color = color,
        date = date
    )
}

fun Folder.toElement(): Element {
    return Element(
        id = id,
        isNote = false,
        name = name,
        content = "",
        color = color,
        date = date
    )
}