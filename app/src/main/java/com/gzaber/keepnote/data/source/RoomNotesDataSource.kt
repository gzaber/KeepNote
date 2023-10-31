package com.gzaber.keepnote.data.source

import com.gzaber.keepnote.data.source.room.NoteDao
import com.gzaber.keepnote.data.source.room.NoteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomNotesDataSource @Inject constructor(
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

    override suspend fun createNote(note: NoteEntity): Long {
        return noteDao.create(note)
    }

    override suspend fun updateNote(note: NoteEntity) {
        noteDao.update(note)
    }

    override suspend fun deleteNote(noteId: Int) {
        noteDao.delete(noteId)
    }
}