package com.gzaber.keepnote.data.source.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {

    @Query("SELECT * FROM folders")
    fun observeAll(): Flow<List<FolderEntity>>

    @Query("SELECT * FROM folders WHERE id = :folderId")
    fun observeById(folderId: Int): Flow<FolderEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(folder: FolderEntity)

    @Update
    fun update(folder: FolderEntity)

    @Delete
    fun delete(folder: FolderEntity)
}