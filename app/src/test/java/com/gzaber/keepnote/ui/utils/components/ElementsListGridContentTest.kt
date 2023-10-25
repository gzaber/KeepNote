package com.gzaber.keepnote.ui.utils.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import com.gzaber.keepnote.data.repository.model.Note
import com.gzaber.keepnote.ui.utils.model.Element
import com.gzaber.keepnote.ui.utils.model.toElement
import com.gzaber.keepnote.ui.utils.model.toFolder
import com.gzaber.keepnote.ui.utils.model.toNote
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ElementsListGridContentTest {
    val folderElement = Element.empty().copy(name = "folder", isNote = false)
    val noteElement = Element.empty().copy(name = "note", content = "content", isNote = true)

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun elementsListGridContent_isGridView_elementsAreDisplayed() {
        composeTestRule.setContent {
            ElementsListGridContent(
                elements = listOf(folderElement, noteElement),
                isGridView = true
            ) {
                Text("header")
            }
        }

        composeTestRule.apply {
            onNodeWithText("header").assertIsDisplayed()
            onNodeWithText("folder").assertIsDisplayed()
            onNodeWithText("note").assertIsDisplayed()
            onNodeWithText("content").assertIsDisplayed()
        }
    }

    @Test
    fun elementsListGridContent_isListView_elementsAreDisplayed() {
        composeTestRule.setContent {
            ElementsListGridContent(
                elements = listOf(folderElement, noteElement),
                isGridView = false
            ) {
                Text("header")
            }
        }

        composeTestRule.apply {
            onNodeWithText("header").assertIsDisplayed()
            onNodeWithText("folder").assertIsDisplayed()
            onNodeWithText("note").assertIsDisplayed()
            onNodeWithText("content").assertIsDisplayed()
        }
    }

    @Test
    fun elementsListContentPreview_isDisplayed() {
        composeTestRule.setContent {
            ElementsListContentPreview()
        }

        composeTestRule.apply {
            onNodeWithText("Note title1", substring = true).assertIsDisplayed()
            onNodeWithText("Content1", substring = true).assertIsDisplayed()
            onNodeWithText("Folder name1", substring = true).assertIsDisplayed()
            onNodeWithText("Note title2", substring = true).assertIsDisplayed()
            onNodeWithText("Content2", substring = true).assertIsDisplayed()
            onNodeWithText("Folder name2", substring = true).assertIsDisplayed()
        }
    }

    @Test
    fun elementsGridContentPreview_isDisplayed() {
        composeTestRule.setContent {
            ElementsGridContentPreview()
        }

        composeTestRule.apply {
            onNodeWithText("Note title1", substring = true).assertIsDisplayed()
            onNodeWithText("Content1", substring = true).assertIsDisplayed()
            onNodeWithText("Folder name1", substring = true).assertIsDisplayed()
            onNodeWithText("Note title2", substring = true).assertIsDisplayed()
            onNodeWithText("Content2", substring = true).assertIsDisplayed()
            onNodeWithText("Folder name2", substring = true).assertIsDisplayed()
        }
    }

    @Test
    fun folderDetailsListContentPreview_isDisplayed() {
        composeTestRule.setContent {
            FolderDetailsListContentPreview()
        }

        composeTestRule.apply {
            onNodeWithText("Note title1", substring = true).assertIsDisplayed()
            onNodeWithText("Content1", substring = true).assertIsDisplayed()
            onNodeWithText("Note title2", substring = true).assertIsDisplayed()
            onNodeWithText("Content2", substring = true).assertIsDisplayed()
            onNodeWithText("Folder details", substring = true).assertIsDisplayed()
            onNodeWithText("Thu 22 September 2023", substring = true).assertIsDisplayed()
        }
    }

    @Test
    fun folderDetailsGridContentPreview_isDisplayed() {
        composeTestRule.setContent {
            FolderDetailsGridContentPreview()
        }

        composeTestRule.apply {
            onNodeWithText("Note title1", substring = true).assertIsDisplayed()
            onNodeWithText("Content1", substring = true).assertIsDisplayed()
            onNodeWithText("Note title2", substring = true).assertIsDisplayed()
            onNodeWithText("Content2", substring = true).assertIsDisplayed()
            onNodeWithText("Folder details", substring = true).assertIsDisplayed()
            onNodeWithText("Thu 22 September 2023", substring = true).assertIsDisplayed()
        }
    }
}