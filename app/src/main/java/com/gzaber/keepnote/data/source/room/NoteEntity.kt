package com.gzaber.keepnote.data.source.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "notes",
    foreignKeys = [
        ForeignKey(
            entity = FolderEntity::class,
            childColumns = ["folder_id"],
            parentColumns = ["id"]
        )
    ]
)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "folder_id") val folderId: Int?,
    val title: String,
    val content: String,
    val color: Int,
    val date: Date = Date()
)
