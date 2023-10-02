package com.gzaber.keepnote.data.repository

import com.gzaber.keepnote.data.repository.model.Folder
import com.gzaber.keepnote.data.source.FoldersDataSource
import com.gzaber.keepnote.data.repository.model.toEntity
import com.gzaber.keepnote.data.repository.model.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class DefaultFoldersRepository @Inject constructor(
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

    override suspend fun createFolder(folder: Folder) {
        foldersDataSource.createFolder(folder.toEntity())
    }

    override suspend fun updateFolder(folder: Folder) {
        foldersDataSource.updateFolder(folder.toEntity())
    }

    override suspend fun deleteFolder(folderId: Int) {
        foldersDataSource.deleteFolder(folderId)
    }
}