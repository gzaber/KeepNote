package com.gzaber.keepnote.data.source

import com.gzaber.keepnote.data.source.room.FolderDao
import com.gzaber.keepnote.data.source.room.FolderEntity
import kotlinx.coroutines.flow.Flow

class RoomFoldersDataSource(
    private val folderDao: FolderDao
) : FoldersDataSource {

    override fun getAllFoldersFlow(): Flow<List<FolderEntity>> {
        return folderDao.observeAll()
    }

    override fun getFolderByIdFlow(folderId: Int): Flow<FolderEntity> {
        return folderDao.observeById(folderId)
    }

    override suspend fun insertFolder(folder: FolderEntity) {
        folderDao.insert(folder)
    }

    override suspend fun updateFolder(folder: FolderEntity) {
        folderDao.update(folder)
    }

    override suspend fun deleteFolder(folder: FolderEntity) {
        folderDao.delete(folder)
    }
}