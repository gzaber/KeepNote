package com.gzaber.keepnote.data.repository

import com.gzaber.keepnote.data.repository.model.Folder
import com.gzaber.keepnote.data.repository.model.toEntity
import com.gzaber.keepnote.data.source.FoldersDataSource
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
class DefaultFoldersRepositoryTest {

    private val folder1 = Folder(id = 1, name = "Folder1", color = 111111, date = Date())
    private val folder2 = Folder(id = 2, name = "Folder1", color = 222222, date = Date())

    @Test
    fun getAllFoldersFlow_emitsListOfFolders() = runTest {
        val mockFoldersDataSource = mock<FoldersDataSource> {
            on { getAllFoldersFlow() } doReturn flow {
                emit(
                    listOf(
                        folder1.toEntity(),
                        folder2.toEntity()
                    )
                )
            }
        }

        val sut = DefaultFoldersRepository(mockFoldersDataSource)
        val result = sut.getAllFoldersFlow().first()

        assertEquals(result, listOf(folder1, folder2))
        verify(mockFoldersDataSource).getAllFoldersFlow()
    }

    @Test
    fun getAllFoldersFlow_emitsEmptyList() = runTest {
        val mockFoldersDataSource = mock<FoldersDataSource> {
            on { getAllFoldersFlow() } doReturn flow { emit(emptyList()) }
        }

        val sut = DefaultFoldersRepository(mockFoldersDataSource)
        val result = sut.getAllFoldersFlow().first()

        assertEquals(result, emptyList<Folder>())
        verify(mockFoldersDataSource).getAllFoldersFlow()
    }

    @Test
    fun getFolderByIdFlow_emitsFolder() = runTest {
        val mockFoldersDataSource = mock<FoldersDataSource> {
            on { getFolderByIdFlow(any()) } doReturn flow { emit(folder1.toEntity()) }
        }

        val sut = DefaultFoldersRepository(mockFoldersDataSource)
        val result = sut.getFolderByIdFlow(1).first()

        assertEquals(result, folder1)
        verify(mockFoldersDataSource).getFolderByIdFlow(1)
    }

    @Test
    fun createFolder_invokesDataSourceMethod() = runTest {
        val mockFoldersDataSource = mock<FoldersDataSource> {
            onBlocking { createFolder(any()) } doReturn Unit
        }

        val sut = DefaultFoldersRepository(mockFoldersDataSource)
        sut.createFolder(folder1.copy(id = null))

        verify(mockFoldersDataSource).createFolder(folder1.toEntity().copy(id = null))
    }

    @Test
    fun updateFolder_invokesDataSourceMethod() = runTest {
        val mockFoldersDataSource = mock<FoldersDataSource> {
            onBlocking { updateFolder(any()) } doReturn Unit
        }

        val sut = DefaultFoldersRepository(mockFoldersDataSource)
        sut.updateFolder(folder1)

        verify(mockFoldersDataSource).updateFolder(folder1.toEntity())
    }

    @Test
    fun deleteFolder_invokesDataSourceMethod() = runTest {
        val mockFoldersDataSource = mock<FoldersDataSource> {
            onBlocking { deleteFolder(any()) } doReturn Unit
        }

        val sut = DefaultFoldersRepository(mockFoldersDataSource)
        sut.deleteFolder(1)

        verify(mockFoldersDataSource).deleteFolder(1)
    }
}