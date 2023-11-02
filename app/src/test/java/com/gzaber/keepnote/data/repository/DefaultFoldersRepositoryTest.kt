package com.gzaber.keepnote.data.repository

import com.gzaber.keepnote.data.repository.model.Folder
import com.gzaber.keepnote.data.repository.model.toEntity
import com.gzaber.keepnote.data.source.FoldersDataSource
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
class DefaultFoldersRepositoryTest {

    private lateinit var repository: FoldersRepository
    private val folder1 = Folder(id = 1, name = "Folder1", color = 111111, date = Date())
    private val folder2 = Folder(id = 2, name = "Folder1", color = 222222, date = Date())

    @Mock
    private lateinit var mockFoldersDataSource: FoldersDataSource

    @Before
    fun setUp() {
        repository = DefaultFoldersRepository(mockFoldersDataSource)
    }

    @Test
    fun getAllFoldersFlow_emitsListOfFolders() = runTest {
        `when`(mockFoldersDataSource.getAllFoldersFlow()).thenReturn(flow {
            emit(
                listOf(
                    folder1.toEntity(),
                    folder2.toEntity()
                )
            )
        })

        val result = repository.getAllFoldersFlow().first()

        assertEquals(listOf(folder1, folder2), result)
        verify(mockFoldersDataSource).getAllFoldersFlow()
    }

    @Test
    fun getAllFoldersFlow_emitsEmptyList() = runTest {
        `when`(mockFoldersDataSource.getAllFoldersFlow()).thenReturn(flow { emit(emptyList()) })

        val result = repository.getAllFoldersFlow().first()

        assertEquals(emptyList<Folder>(), result)
        verify(mockFoldersDataSource).getAllFoldersFlow()
    }

    @Test
    fun getFolderByIdFlow_emitsFolder() = runTest {
        `when`(mockFoldersDataSource.getFolderByIdFlow(anyInt())).thenReturn(flow {
            emit(folder1.toEntity())
        })

        val result = repository.getFolderByIdFlow(1).first()

        assertEquals(folder1, result)
        verify(mockFoldersDataSource).getFolderByIdFlow(1)
    }

    @Test
    fun createFolder_invokesDataSourceMethod() = runTest {
        repository.createFolder(folder1.copy(id = null))

        verify(mockFoldersDataSource).createFolder(folder1.toEntity().copy(id = null))
    }

    @Test
    fun updateFolder_invokesDataSourceMethod() = runTest {
        repository.updateFolder(folder1)

        verify(mockFoldersDataSource).updateFolder(folder1.toEntity())
    }

    @Test
    fun deleteFolder_invokesDataSourceMethod() = runTest {
        repository.deleteFolder(1)

        verify(mockFoldersDataSource).deleteFolder(1)
    }
}