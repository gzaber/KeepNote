package com.gzaber.keepnote.data.repository.model

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.util.Date

@RunWith(MockitoJUnitRunner::class)
class NoteTest {

    @Test
    fun folder_defaultValuesAreNull() {
        val note = Note(title = "note", content = "content", color = 0, date = Date())

        assertEquals(null, note.id)
        assertEquals(null, note.folderId)
    }
}