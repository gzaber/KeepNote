package com.gzaber.keepnote.data.repository

import com.gzaber.keepnote.data.repository.model.Note
import com.gzaber.keepnote.data.repository.model.toEntity
import com.gzaber.keepnote.data.source.NotesDataSource
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
class DefaultNotesRepositoryTest {

    private val note1 = Note(
        id = 1,
        folderId = null,
        title = "title1",
        content = "content1",
        color = 111111,
        date = Date()
    )
    private val note2 = Note(
        id = 2,
        folderId = null,
        title = "title2",
        content = "content2",
        color = 22222222,
        date = Date()
    )

    @Test
    fun getFirstLevelNotesFlow_emitsListOfFirstLevelNotes() = runTest {
        val mockNotesDataSource = mock<NotesDataSource> {
            on { getFirstLevelNotesFlow() } doReturn flow {
                emit(
                    listOf(
                        note1.toEntity(),
                        note2.toEntity()
                    )
                )
            }
        }

        val sut = DefaultNotesRepository(mockNotesDataSource)
        val result = sut.getFirstLevelNotesFlow().first()

        assertEquals(result, listOf(note1, note2))
        verify(mockNotesDataSource).getFirstLevelNotesFlow()
    }

    @Test
    fun getFirstLevelNotesFlow_emitsEmptyList() = runTest {
        val mockNotesDataSource = mock<NotesDataSource> {
            on { getFirstLevelNotesFlow() } doReturn flow { emit(emptyList()) }
        }

        val sut = DefaultNotesRepository(mockNotesDataSource)
        val result = sut.getFirstLevelNotesFlow().first()

        assertEquals(result, emptyList<Note>())
        verify(mockNotesDataSource).getFirstLevelNotesFlow()
    }

    @Test
    fun getSecondLevelNotesFlow_emitsListOfSecondLevelNotes() = runTest {
        val mockNotesDataSource = mock<NotesDataSource> {
            on { getSecondLevelNotesFlow(any()) } doReturn flow {
                emit(
                    listOf(
                        note1.toEntity().copy(folderId = 11),
                        note2.toEntity().copy(folderId = 11)
                    )
                )
            }
        }

        val sut = DefaultNotesRepository(mockNotesDataSource)
        val result = sut.getSecondLevelNotesFlow(11).first()

        assertEquals(result, listOf(note1.copy(folderId = 11), note2.copy(folderId = 11)))
        verify(mockNotesDataSource).getSecondLevelNotesFlow(11)
    }

    @Test
    fun getSecondLevelNotesFlow_emitsEmptyList() = runTest {
        val mockNotesDataSource = mock<NotesDataSource> {
            on { getSecondLevelNotesFlow(any()) } doReturn flow { emit(emptyList()) }
        }

        val sut = DefaultNotesRepository(mockNotesDataSource)
        val result = sut.getSecondLevelNotesFlow(11).first()

        assertEquals(result, emptyList<Note>())
        verify(mockNotesDataSource).getSecondLevelNotesFlow(11)
    }

    @Test
    fun getNoteByIdFlow_emitsNote() = runTest {
        val mockNotesDataSource = mock<NotesDataSource> {
            on { getNoteByIdFlow(any()) } doReturn flow { emit(note1.toEntity()) }
        }

        val sut = DefaultNotesRepository(mockNotesDataSource)
        val result = sut.getNoteByIdFlow(1).first()

        assertEquals(result, note1)
        verify(mockNotesDataSource).getNoteByIdFlow(1)
    }

    @Test
    fun createNote_invokesDataSourceMethod() = runTest {
        val mockNotesDataSource = mock<NotesDataSource> {
            onBlocking { createNote(any()) } doReturn Unit
        }

        val sut = DefaultNotesRepository(mockNotesDataSource)
        sut.createNote(note1.copy(id = null))

        verify(mockNotesDataSource).createNote(note1.toEntity().copy(id = null))
    }

    @Test
    fun updateNote_invokesDataSourceMethod() = runTest {
        val mockNotesDataSource = mock<NotesDataSource> {
            onBlocking { updateNote(any()) } doReturn Unit
        }

        val sut = DefaultNotesRepository(mockNotesDataSource)
        sut.updateNote(note1)

        verify(mockNotesDataSource).updateNote(note1.toEntity())
    }

    @Test
    fun deleteNote_invokesDataSourceMethod() = runTest {
        val mockNotesDataSource = mock<NotesDataSource> {
            onBlocking { deleteNote(any()) } doReturn Unit
        }

        val sut = DefaultNotesRepository(mockNotesDataSource)
        sut.deleteNote(1)

        verify(mockNotesDataSource).deleteNote(1)
    }
}














