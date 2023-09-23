package com.gzaber.keepnote.data.repository.model

import com.gzaber.keepnote.data.repository.model.Folder
import com.gzaber.keepnote.data.repository.model.Note
import com.gzaber.keepnote.data.source.room.FolderEntity
import com.gzaber.keepnote.data.source.room.NoteEntity

fun Note.toEntity() = NoteEntity(
    id, folderId, title, content, color, date
)

fun NoteEntity.toModel() = Note(
    id, folderId, title, content, color, date
)

fun Folder.toEntity() = FolderEntity(
    id, name, color, date
)

fun FolderEntity.toModel() = Folder(
    id, name, color, date
)