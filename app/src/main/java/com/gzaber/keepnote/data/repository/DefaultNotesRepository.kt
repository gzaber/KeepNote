package com.gzaber.keepnote.data.repository

import com.gzaber.keepnote.data.repository.model.Note
import com.gzaber.keepnote.data.source.NotesDataSource
import com.gzaber.keepnote.data.repository.model.toEntity
import com.gzaber.keepnote.data.repository.model.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class DefaultNotesRepository @Inject constructor(
    private val notesDataSource: NotesDataSource
) : NotesRepository {

    override fun getFirstLevelNotesFlow(): Flow<List<Note>> {
        return notesDataSource.getFirstLevelNotesFlow().map {
            it.map { noteEntity ->
                noteEntity.toModel()
            }
        }
    }

    override fun getSecondLevelNotesFlow(folderId: Int): Flow<List<Note>> {
        return notesDataSource.getSecondLevelNotesFlow(folderId).map {
            it.map { noteEntity ->
                noteEntity.toModel()
            }
        }
    }

    override fun getNoteByIdFlow(noteId: Int): Flow<Note> {
        return notesDataSource.getNoteByIdFlow(noteId).map { noteEntity ->
            noteEntity.toModel()
        }
    }

    override suspend fun createNote(note: Note) {
        notesDataSource.createNote(note.toEntity())
    }

    override suspend fun updateNote(note: Note) {
        notesDataSource.updateNote(note.toEntity())
    }

    override suspend fun deleteNote(noteId: Int) {
        notesDataSource.deleteNote(noteId)
    }
}