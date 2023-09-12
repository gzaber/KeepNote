package com.gzaber.keepnote.data.repository

import com.gzaber.keepnote.data.repository.model.Folder
import com.gzaber.keepnote.data.source.FoldersDataSource
import com.gzaber.keepnote.data.source.room.FolderEntity
import com.gzaber.keepnote.data.utils.toEntity
import com.gzaber.keepnote.data.utils.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultFoldersRepository(
    private val foldersDataSource: FoldersDataSource
) : FoldersRepository {

    override fun getAllFoldersFlow(): Flow<List<Folder>> {
        return foldersDataSource.getAllFoldersFlow().map {
            it.map { folderEntity ->
                folderEntity.toModel()
            }
        }
    }

    override fun getFolderByIdFlow(folderId: Int): Flow<Folder> {
        return foldersDataSource.getFolderByIdFlow(folderId).map { folderEntity ->
            folderEntity.toModel()
        }
    }

    override suspend fun insertFolder(folder: Folder) {
        foldersDataSource.insertFolder(folder.toEntity())
    }

    override suspend fun updateFolder(folder: Folder) {
        foldersDataSource.updateFolder(folder.toEntity())
    }

    override suspend fun deleteFolder(folder: Folder) {
        foldersDataSource.deleteFolder(folder.toEntity())
    }
}