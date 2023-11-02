package com.gzaber.keepnote.data.repository

import com.gzaber.keepnote.data.repository.model.Note
import com.gzaber.keepnote.data.repository.model.toEntity
import com.gzaber.keepnote.data.source.NotesDataSource
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
class DefaultNotesRepositoryTest {

    private lateinit var repository: NotesRepository
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

    @Mock
    private lateinit var mockNotesDataSource: NotesDataSource

    @Before
    fun setUp() {
        repository = DefaultNotesRepository(mockNotesDataSource)
    }

    @Test
    fun getFirstLevelNotesFlow_emitsListOfFirstLevelNotes() = runTest {
        `when`(mockNotesDataSource.getFirstLevelNotesFlow()).thenReturn(flow {
            emit(
                listOf(
                    note1.toEntity(),
                    note2.toEntity()
                )
            )
        })

        val result = repository.getFirstLevelNotesFlow().first()

        assertEquals(listOf(note1, note2), result)
        verify(mockNotesDataSource).getFirstLevelNotesFlow()
    }

    @Test
    fun getFirstLevelNotesFlow_emitsEmptyList() = runTest {
        `when`(mockNotesDataSource.getFirstLevelNotesFlow()).thenReturn(flow { emit(emptyList()) })

        val result = repository.getFirstLevelNotesFlow().first()

        assertEquals(emptyList<Note>(), result)
        verify(mockNotesDataSource).getFirstLevelNotesFlow()
    }

    @Test
    fun getSecondLevelNotesFlow_emitsListOfSecondLevelNotes() = runTest {
        `when`(mockNotesDataSource.getSecondLevelNotesFlow(anyInt())).thenReturn(flow {
            emit(
                listOf(
                    note1.toEntity().copy(folderId = 11),
                    note2.toEntity().copy(folderId = 11)
                )
            )
        })

        val result = repository.getSecondLevelNotesFlow(11).first()

        assertEquals(listOf(note1.copy(folderId = 11), note2.copy(folderId = 11)), result)
        verify(mockNotesDataSource).getSecondLevelNotesFlow(11)
    }

    @Test
    fun getSecondLevelNotesFlow_emitsEmptyList() = runTest {
        `when`(mockNotesDataSource.getSecondLevelNotesFlow(anyInt())).thenReturn(
            flow { emit(emptyList()) })

        val result = repository.getSecondLevelNotesFlow(11).first()

        assertEquals(emptyList<Note>(), result)
        verify(mockNotesDataSource).getSecondLevelNotesFlow(11)
    }

    @Test
    fun getNoteByIdFlow_emitsNote() = runTest {
        `when`(mockNotesDataSource.getNoteByIdFlow(anyInt())).thenReturn(flow {
            emit(note1.toEntity())
        })

        val result = repository.getNoteByIdFlow(1).first()

        assertEquals(note1, result)
        verify(mockNotesDataSource).getNoteByIdFlow(1)
    }

    @Test
    fun createNote_invokesDataSourceMethod() = runTest {
        repository.createNote(note1.copy(id = null))

        verify(mockNotesDataSource).createNote(note1.toEntity().copy(id = null))
    }

    @Test
    fun updateNote_invokesDataSourceMethod() = runTest {
        repository.updateNote(note1)

        verify(mockNotesDataSource).updateNote(note1.toEntity())
    }

    @Test
    fun deleteNote_invokesDataSourceMethod() = runTest {
        repository.deleteNote(1)

        verify(mockNotesDataSource).deleteNote(1)
    }
}














