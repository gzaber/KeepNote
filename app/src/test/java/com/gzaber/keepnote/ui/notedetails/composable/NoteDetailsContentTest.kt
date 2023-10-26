package com.gzaber.keepnote.ui.notedetails.composable

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import com.gzaber.keepnote.ui.util.model.Element
import com.gzaber.keepnote.ui.util.model.toNote
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NoteDetailsContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun noteDetailsContent_titleAndContentAreDisplayed() {
        composeTestRule.setContent {
            NoteDetailsContent(
                note = Element.empty().copy(name = "note", content = "content").toNote(),
                contentPadding = PaddingValues(0.dp)
            )
        }

        composeTestRule.onNodeWithText("note").assertIsDisplayed()
        composeTestRule.onNodeWithText("content").assertIsDisplayed()
    }

    @Test
    fun noteDetailsContentPreview_isDisplayed() {
        composeTestRule.setContent { NoteDetailsContentPreview() }

        composeTestRule.onNodeWithText("Volutpat", substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("Lorem", substring = true).assertIsDisplayed()
    }
}