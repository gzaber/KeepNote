package com.gzaber.keepnote.data.source

import com.gzaber.keepnote.data.source.room.NoteEntity
import kotlinx.coroutines.flow.Flow

interface NotesDataSource {
    fun getFirstLevelNotesFlow(): Flow<List<NoteEntity>>
    fun getSecondLevelNotesFlow(folderId: Int): Flow<List<NoteEntity>>
    fun getNoteByIdFlow(noteId: Int): Flow<NoteEntity>
    suspend fun createNote(note: NoteEntity)
    suspend fun updateNote(note: NoteEntity)
    suspend fun deleteNote(noteId: Int)
}