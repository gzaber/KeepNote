package com.gzaber.keepnote.data.source

import com.gzaber.keepnote.data.source.room.FolderEntity
import kotlinx.coroutines.flow.Flow

interface FoldersDataSource {
    fun getAllFoldersFlow(): Flow<List<FolderEntity>>
    fun getFolderByIdFlow(folderId: Int): Flow<FolderEntity>
    suspend fun createFolder(folder: FolderEntity): Long
    suspend fun updateFolder(folder: FolderEntity)
    suspend fun deleteFolder(folderId: Int)
}