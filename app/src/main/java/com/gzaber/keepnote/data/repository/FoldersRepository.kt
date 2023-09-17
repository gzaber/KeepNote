package com.gzaber.keepnote.data.repository

import com.gzaber.keepnote.data.repository.model.Folder
import kotlinx.coroutines.flow.Flow

interface FoldersRepository {
    fun getAllFoldersFlow(): Flow<List<Folder>>
    fun getFolderByIdFlow(folderId: Int): Flow<Folder>
    suspend fun createFolder(folder: Folder)
    suspend fun updateFolder(folder: Folder)
    suspend fun deleteFolder(folderId: Int)
}