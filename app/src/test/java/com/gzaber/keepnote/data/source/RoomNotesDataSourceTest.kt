package com.gzaber.keepnote.data.source

import com.gzaber.keepnote.data.source.room.NoteDao
import com.gzaber.keepnote.data.source.room.NoteEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import java.util.Date

@RunWith(MockitoJUnitRunner::class)
class RoomNotesDataSourceTest {

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

    @Test
    fun getFirstLevelNotesFlow_emitsListOfFirstLevelNoteEntities() = runTest {
        val mockNoteDao = mock<NoteDao> {
            on { observeFirstLevel() } doReturn flow { emit(listOf(note1, note2)) }
        }

        val sut = RoomNotesDataSource(mockNoteDao)
        val result = sut.getFirstLevelNotesFlow().first()

        assertEquals(result, listOf(note1, note2))
        verify(mockNoteDao).observeFirstLevel()
    }

    @Test
    fun getFirstLevelNotesFlow_emitsEmptyList() = runTest {
        val mockNoteDao = mock<NoteDao> {
            on { observeFirstLevel() } doReturn flow { emit(emptyList()) }
        }

        val sut = RoomNotesDataSource(mockNoteDao)
        val result = sut.getFirstLevelNotesFlow().first()

        assertEquals(result, emptyList<NoteEntity>())
        verify(mockNoteDao).observeFirstLevel()
    }

    @Test
    fun getSecondLevelNotesFlow_emitsListOfSecondLevelNoteEntities() = runTest {
        val mockNoteDao = mock<NoteDao> {
            on { observeSecondLevel(any()) } doReturn flow {
                emit(
                    listOf(
                        note1.copy(folderId = 11),
                        note2.copy(folderId = 11)
                    )
                )
            }
        }

        val sut = RoomNotesDataSource(mockNoteDao)
        val result = sut.getSecondLevelNotesFlow(11).first()

        assertEquals(
            result, listOf(
                note1.copy(folderId = 11),
                note2.copy(folderId = 11)
            )
        )
        verify(mockNoteDao).observeSecondLevel(11)
    }

    @Test
    fun getSecondLevelNotesFlow_emitsEmptyList() = runTest {
        val mockNoteDao = mock<NoteDao> {
            on { observeSecondLevel(any()) } doReturn flow { emit(emptyList()) }
        }

        val sut = RoomNotesDataSource(mockNoteDao)
        val result = sut.getSecondLevelNotesFlow(11).first()

        assertEquals(
            result, emptyList<NoteEntity>()
        )
        verify(mockNoteDao).observeSecondLevel(11)
    }

    @Test
    fun getNoteByIdFlow_emitsNoteEntity() = runTest {
        val mockNoteDao = mock<NoteDao> {
            on { observeById(any()) } doReturn flow { emit(note1) }
        }

        val sut = RoomNotesDataSource(mockNoteDao)
        val result = sut.getNoteByIdFlow(1).first()

        assertEquals(result, note1)
        verify(mockNoteDao).observeById(1)
    }

    @Test
    fun createNote_invokesDaoMethod() = runTest {
        val mockNoteDao = mock<NoteDao> {
            onBlocking { create(any()) } doReturn Unit
        }

        val sut = RoomNotesDataSource(mockNoteDao)
        sut.createNote(note1.copy(id = null))

        verify(mockNoteDao).create(note1.copy(id = null))
    }

    @Test
    fun updateNote_invokesDaoMethod() = runTest {
        val mockNoteDao = mock<NoteDao> {
            onBlocking { update(any()) } doReturn Unit
        }

        val sut = RoomNotesDataSource(mockNoteDao)
        sut.updateNote(note1)

        verify(mockNoteDao).update(note1)
    }

    @Test
    fun deleteNote_invokesDaoMethod() = runTest {
        val mockNoteDao = mock<NoteDao> {
            onBlocking { delete(any()) } doReturn Unit
        }

        val sut = RoomNotesDataSource(mockNoteDao)
        sut.deleteNote(1)

        verify(mockNoteDao).delete(1)
    }
}