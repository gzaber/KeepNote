package com.gzaber.keepnote.data.source

import com.gzaber.keepnote.data.source.room.FolderEntity
import kotlinx.coroutines.flow.Flow

interface FoldersDataSource {
    fun getAllFoldersFlow(): Flow<List<FolderEntity>>
    fun getFolderByIdFlow(folderId: Int): Flow<FolderEntity>
    suspend fun insertFolder(folder: FolderEntity)
    suspend fun updateFolder(folder: FolderEntity)
    suspend fun deleteFolder(folder: FolderEntity)
}