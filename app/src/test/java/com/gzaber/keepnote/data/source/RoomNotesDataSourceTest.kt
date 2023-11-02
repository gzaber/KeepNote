package com.gzaber.keepnote.data.source

import com.gzaber.keepnote.data.source.room.NoteDao
import com.gzaber.keepnote.data.source.room.NoteEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.util.Date

@RunWith(MockitoJUnitRunner::class)
class RoomNotesDataSourceTest {

    private lateinit var dataSource: NotesDataSource
    private val note1 = NoteEntity(
        id = 1,
        folderId = null,
        title = "title1",
        content = "content1",
        color = 111111,
        date = Date()
    )
    private val note2 = NoteEntity(
        id = 2,
        folderId = null,
        title = "title2",
        content = "content2",
        color = 22222222,
        date = Date()
    )

    @Mock
    private lateinit var mockNoteDao: NoteDao

    @Before
    fun setUp() {
        dataSource = RoomNotesDataSource(mockNoteDao)
    }

    @Test
    fun getFirstLevelNotesFlow_emitsListOfFirstLevelNoteEntities() = runTest {
        `when`(mockNoteDao.observeFirstLevel())
            .thenReturn(flow { emit(listOf(note1, note2)) })

        val result = dataSource.getFirstLevelNotesFlow().first()

        assertEquals(listOf(note1, note2), result)
        verify(mockNoteDao).observeFirstLevel()
    }

    @Test
    fun getFirstLevelNotesFlow_emitsEmptyList() = runTest {
        `when`(mockNoteDao.observeFirstLevel()).thenReturn(flow { emit(emptyList()) })

        val result = dataSource.getFirstLevelNotesFlow().first()

        assertEquals(emptyList<NoteEntity>(), result)
        verify(mockNoteDao).observeFirstLevel()
    }

    @Test
    fun getSecondLevelNotesFlow_emitsListOfSecondLevelNoteEntities() = runTest {
        `when`(mockNoteDao.observeSecondLevel(anyInt())).thenReturn(flow {
            emit(
                listOf(
                    note1.copy(folderId = 11),
                    note2.copy(folderId = 11)
                )
            )
        })

        val result = dataSource.getSecondLevelNotesFlow(11).first()

        assertEquals(
            listOf(
                note1.copy(folderId = 11),
                note2.copy(folderId = 11)
            ), result
        )
        verify(mockNoteDao).observeSecondLevel(11)
    }

    @Test
    fun getSecondLevelNotesFlow_emitsEmptyList() = runTest {
        `when`(mockNoteDao.observeSecondLevel(anyInt())).thenReturn(flow { emit(emptyList()) })

        val result = dataSource.getSecondLevelNotesFlow(11).first()

        assertEquals(emptyList<NoteEntity>(), result)
        verify(mockNoteDao).observeSecondLevel(11)
    }

    @Test
    fun getNoteByIdFlow_emitsNoteEntity() = runTest {
        `when`(mockNoteDao.observeById(anyInt())).thenReturn(flow { emit(note1) })

        val result = dataSource.getNoteByIdFlow(1).first()

        assertEquals(note1, result)
        verify(mockNoteDao).observeById(1)
    }

    @Test
    fun createNote_invokesDaoMethod() = runTest {
        dataSource.createNote(note1.copy(id = null))

        verify(mockNoteDao).create(note1.copy(id = null))
    }

    @Test
    fun updateNote_invokesDaoMethod() = runTest {
        dataSource.updateNote(note1)

        verify(mockNoteDao).update(note1)
    }

    @Test
    fun deleteNote_invokesDaoMethod() = runTest {
        dataSource.deleteNote(1)

        verify(mockNoteDao).delete(1)
    }
}