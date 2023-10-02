package com.gzaber.keepnote.data.source

import com.gzaber.keepnote.data.source.room.FolderDao
import com.gzaber.keepnote.data.source.room.FolderEntity
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
class RoomFoldersDataSourceTest {

    private val folder1 = FolderEntity(id = 1, name = "Folder1", color = 111111, date = Date())
    private val folder2 = FolderEntity(id = 2, name = "Folder1", color = 222222, date = Date())

    @Test
    fun getAllFoldersFlow_emitsListOfFolderEntities() = runTest {
        val mockFolderDao = mock<FolderDao> {
            on { observeAll() } doReturn flow { emit(listOf(folder1, folder2)) }
        }

        val sut = RoomFoldersDataSource(mockFolderDao)
        val result = sut.getAllFoldersFlow().first()

        assertEquals(result, listOf(folder1, folder2))
        verify(mockFolderDao).observeAll()
    }

    @Test
    fun getAllFoldersFlow_emitsEmptyList() = runTest {
        val mockFolderDao = mock<FolderDao> {
            on { observeAll() } doReturn flow { emit(emptyList()) }
        }

        val sut = RoomFoldersDataSource(mockFolderDao)
        val result = sut.getAllFoldersFlow().first()

        assertEquals(result, emptyList<FolderEntity>())
        verify(mockFolderDao).observeAll()
    }

    @Test
    fun getFolderByIdFlow_emitsFolderEntity() = runTest {
        val mockFolderDao = mock<FolderDao> {
            on { observeById(any()) } doReturn flow { emit(folder1) }
        }

        val sut = RoomFoldersDataSource(mockFolderDao)
        val result = sut.getFolderByIdFlow(1).first()

        assertEquals(result, folder1)
        verify(mockFolderDao).observeById(1)
    }

    @Test
    fun createFolder_invokesDaoMethod() = runTest {
        val mockFolderDao = mock<FolderDao> {
            onBlocking { create(any()) } doReturn Unit
        }

        val sut = RoomFoldersDataSource(mockFolderDao)
        sut.createFolder(folder1)

        verify(mockFolderDao).create(folder1)
    }

    @Test
    fun updateFolder_invokesDaoMethod() = runTest {
        val mockFolderDao = mock<FolderDao> {
            onBlocking { update(any()) } doReturn Unit
        }

        val sut = RoomFoldersDataSource(mockFolderDao)
        sut.updateFolder(folder1)

        verify(mockFolderDao).update(folder1)
    }

    @Test
    fun deleteFolder_invokesDaoMethod() = runTest {
        val mockFolderDao = mock<FolderDao> {
            onBlocking { delete(any()) } doReturn Unit
        }

        val sut = RoomFoldersDataSource(mockFolderDao)
        sut.deleteFolder(1)

        verify(mockFolderDao).delete(1)
    }
}