package com.gzaber.keepnote.ui.util.composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import com.gzaber.keepnote.ui.util.model.Element
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ElementViewItemsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun elementIcon_folderNodeIsDisplayed() {
        composeTestRule.setContent {
            ElementIcon(isNote = false, modifier = Modifier)
        }

        composeTestRule.onNodeWithContentDescription("Folder").assertIsDisplayed()
    }

    @Test
    fun elementIcon_noteNodeIsDisplayed() {
        composeTestRule.setContent {
            ElementIcon(isNote = true, modifier = Modifier)
        }

        composeTestRule.onNodeWithContentDescription("Note").assertIsDisplayed()
    }

    @Test
    fun elementText_isFolder_nameIsDisplayed() {
        composeTestRule.setContent {
            ElementText(
                element = Element.empty().copy(isNote = false, name = "name"),
                modifier = Modifier
            )
        }

        composeTestRule.onNodeWithText("name").assertIsDisplayed()
    }

    @Test
    fun elementText_isNote_nameAndContentAreDisplayed() {
        composeTestRule.setContent {
            ElementText(
                element = Element.empty().copy(isNote = true, name = "name", content = "content"),
                modifier = Modifier
            )
        }

        composeTestRule.onNodeWithText("name").assertIsDisplayed()
        composeTestRule.onNodeWithText("content").assertIsDisplayed()
    }

    @Test
    fun elementItem_isGridItem_nameAndContentAreDisplayed_isClickable() {
        composeTestRule.setContent {
            ElementItem(
                element = Element.empty()
                    .copy(id = 1, isNote = true, name = "name", content = "content"),
                isGridItem = true,
                modifier = Modifier
            )
        }

        composeTestRule.onNodeWithText("name").assertIsDisplayed().performClick()
            .performTouchInput { longClick() }
        composeTestRule.onNodeWithText("content").assertIsDisplayed().performClick()
            .performTouchInput { longClick() }
    }

    @Test
    fun elementItem_isListItem_nameAndContentAreDisplayed_isClickable() {
        composeTestRule.setContent {
            ElementItem(
                element = Element.empty()
                    .copy(id = 1, isNote = true, name = "name", content = "content"),
                isGridItem = false,
                modifier = Modifier
            )
        }

        composeTestRule.onNodeWithText("name").assertIsDisplayed().performClick()
            .performTouchInput { longClick() }
        composeTestRule.onNodeWithText("content").assertIsDisplayed().performClick()
            .performTouchInput { longClick() }
    }

    @Test
    fun noteElementGridItemPreview_isDisplayed() {
        composeTestRule.setContent {
            NoteElementGridItemPreview()
        }

        composeTestRule.onNodeWithText("Title", substring = true).assertIsDisplayed()
            .assertHasClickAction()
        composeTestRule.onNodeWithText("Content", substring = true).assertIsDisplayed()
            .assertHasClickAction()
    }

    @Test
    fun folderElementGridItemPreview_isDisplayed() {
        composeTestRule.setContent {
            FolderElementGridItemPreview()
        }

        composeTestRule.onNodeWithText("Name", substring = true).assertIsDisplayed()
            .assertHasClickAction()
    }

    @Test
    fun noteElementListItemPreview_isDisplayed() {
        composeTestRule.setContent {
            NoteElementListItemPreview()
        }

        composeTestRule.onNodeWithText("Title", substring = true).assertIsDisplayed()
            .assertHasClickAction()
        composeTestRule.onNodeWithText("Content", substring = true).assertIsDisplayed()
            .assertHasClickAction()
    }

    @Test
    fun folderElementListItemPreview_isDisplayed() {
        composeTestRule.setContent {
            FolderElementListItemPreview()
        }

        composeTestRule.onNodeWithText("Name", substring = true).assertIsDisplayed()
            .assertHasClickAction()
    }
}