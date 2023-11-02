package com.gzaber.keepnote.data.repository.model

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.util.Date

@RunWith(MockitoJUnitRunner::class)
class FolderTest {

    @Test
    fun folder_defaultValueIsNull() {
        val folder = Folder(name = "folder", color = 0, date = Date())

        assertEquals(null, folder.id)
    }
}