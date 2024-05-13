package com.gzaber.keepnote.ui.util.composable

import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.gzaber.keepnote.ui.util.model.Element
import com.gzaber.keepnote.util.RobolectricTestActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ElementsListGridContentTest {

    private val folderElement = Element.empty().copy(name = "folder", isNote = false)
    private val noteElement =
        Element.empty().copy(name = "note", content = "content", isNote = true)

    @get:Rule(order = 0)
    val robolectricTestActivityRule = RobolectricTestActivity()

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @Test
    fun elementsListGridContent_isGridView_elementsAreDisplayed() {
        composeTestRule.setContent {
            ElementsListGridContent(
                elements = listOf(folderElement, noteElement),
                isGridView = true,
                modifier = Modifier
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
                isGridView = false,
                modifier = Modifier
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