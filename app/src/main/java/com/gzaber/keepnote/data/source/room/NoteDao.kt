package com.gzaber.keepnote.data.source.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes WHERE folder_id = null")
    fun observeFirstLevel(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE folder_id = :folderId")
    fun observeSecondLevel(folderId: Int): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :noteId")
    fun observeById(noteId: Int): Flow<NoteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: NoteEntity)

    @Update
    fun update(note: NoteEntity)

    @Delete
    fun delete(note: NoteEntity)
}