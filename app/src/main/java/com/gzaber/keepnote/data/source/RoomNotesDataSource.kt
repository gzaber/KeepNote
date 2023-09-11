package com.gzaber.keepnote.data.source

import com.gzaber.keepnote.data.source.room.NoteDao
import com.gzaber.keepnote.data.source.room.NoteEntity
import kotlinx.coroutines.flow.Flow

class RoomNotesDataSource(
    private val noteDao: NoteDao
) : NotesDataSource {

    override fun getFirstLevelNotesFlow(): Flow<List<NoteEntity>> {
        return noteDao.observeFirstLevel()
    }

    override fun getSecondLevelNotesFlow(folderId: Int): Flow<List<NoteEntity>> {
        return noteDao.observeSecondLevel(folderId)
    }

    override fun getNoteByIdFlow(noteId: Int): Flow<NoteEntity> {
        return noteDao.observeById(noteId)
    }

    override suspend fun insertNote(note: NoteEntity) {
        noteDao.insert(note)
    }

    override suspend fun updateNote(note: NoteEntity) {
        noteDao.update(note)
    }

    override suspend fun deleteNote(note: NoteEntity) {
        noteDao.delete(note)
    }
}