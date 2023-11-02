package com.gzaber.keepnote.data.source

import com.gzaber.keepnote.data.source.room.FolderDao
import com.gzaber.keepnote.data.source.room.FolderEntity
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
class RoomFoldersDataSourceTest {

    private lateinit var dataSource: FoldersDataSource
    private val folder1 = FolderEntity(id = 1, name = "Folder1", color = 111111, date = Date())
    private val folder2 = FolderEntity(id = 2, name = "Folder1", color = 222222, date = Date())

    @Mock
    private lateinit var mockFolderDao: FolderDao

    @Before
    fun setUp() {
        dataSource = RoomFoldersDataSource(mockFolderDao)
    }

    @Test
    fun getAllFoldersFlow_emitsListOfFolderEntities() = runTest {
        `when`(mockFolderDao.observeAll())
            .thenReturn(flow { emit(listOf(folder1, folder2)) })

        val result = dataSource.getAllFoldersFlow().first()

        assertEquals(listOf(folder1, folder2), result)
        verify(mockFolderDao).observeAll()
    }

    @Test
    fun getAllFoldersFlow_emitsEmptyList() = runTest {
        `when`(mockFolderDao.observeAll()).thenReturn(flow { emit(emptyList()) })

        val result = dataSource.getAllFoldersFlow().first()

        assertEquals(emptyList<FolderEntity>(), result)
        verify(mockFolderDao).observeAll()
    }

    @Test
    fun getFolderByIdFlow_emitsFolderEntity() = runTest {
        `when`(mockFolderDao.observeById(anyInt())).thenReturn(flow { emit(folder1) })

        val result = dataSource.getFolderByIdFlow(1).first()

        assertEquals(folder1, result)
        verify(mockFolderDao).observeById(1)
    }

    @Test
    fun createFolder_invokesDaoMethod() = runTest {
        dataSource.createFolder(folder1.copy(id = null))

        verify(mockFolderDao).create(folder1.copy(id = null))
    }

    @Test
    fun updateFolder_invokesDaoMethod() = runTest {
        dataSource.updateFolder(folder1)

        verify(mockFolderDao).update(folder1)
    }

    @Test
    fun deleteFolder_invokesDaoMethod() = runTest {
        dataSource.deleteFolder(1)

        verify(mockFolderDao).delete(1)
    }
}