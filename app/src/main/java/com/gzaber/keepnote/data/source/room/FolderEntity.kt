package com.gzaber.keepnote.data.source.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "folders")
data class FolderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val name: String,
    val color: Int,
    val date: Date
)
