package com.gzaber.keepnote.data.repository

import com.gzaber.keepnote.data.repository.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun getFirstLevelNotesFlow(): Flow<List<Note>>
    fun getSecondLevelNotesFlow(folderId: Int): Flow<List<Note>>
    fun getNoteByIdFlow(noteId: Int): Flow<Note>
    suspend fun insertNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(note: Note)
}