package com.gzaber.keepnote.ui.utils.components

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import com.gzaber.keepnote.R

@RunWith(RobolectricTestRunner::class)
class KeepNoteAppBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun keepNoteAppBar_componentsAreDisplayed_buttonsAreClickable() {
        composeTestRule.setContent {
            KeepNoteAppBar(
                title = R.string.app_name,
                onBackClick = {},
                onSortClick = {},
                onChangeViewClick = {},
                onSaveClick = {},
                onShareClick = {}
            )
        }

        composeTestRule.apply {
            onNodeWithText("KeepNote").assertIsDisplayed()
            onNodeWithContentDescription("Navigate back").assertIsDisplayed().assertHasClickAction()
            onNodeWithContentDescription("Sort").assertIsDisplayed().assertHasClickAction()
            onNodeWithContentDescription("Grid view").assertIsDisplayed().assertHasClickAction()
            onNodeWithContentDescription("Save").assertIsDisplayed().assertHasClickAction()
            onNodeWithContentDescription("Share note").assertIsDisplayed().assertHasClickAction()
        }
    }

    @Test
    fun elementsOverviewAppBarListViewPreview_isDisplayed() {
        composeTestRule.setContent {
            ElementsOverviewAppBarListViewPreview()
        }

        composeTestRule.apply {
            onNodeWithText("KeepNote").assertIsDisplayed()
            onNodeWithContentDescription("Sort").assertIsDisplayed().performClick()
            onNodeWithContentDescription("Grid view").assertIsDisplayed().performClick()
        }
    }

    @Test
    fun elementsOverviewAppBarGridViewPreview_isDisplayed() {
        composeTestRule.setContent {
            ElementsOverviewAppBarGridViewPreview()
        }

        composeTestRule.apply {
            onNodeWithText("KeepNote").assertIsDisplayed()
            onNodeWithContentDescription("Sort").assertIsDisplayed().performClick()
            onNodeWithContentDescription("List view").assertIsDisplayed().performClick()
        }
    }

    @Test
    fun addEditElementAppBarPreview_isDisplayed() {
        composeTestRule.setContent {
            AddEditElementAppBarPreview()
        }

        composeTestRule.apply {
            onNodeWithText("Create note").assertIsDisplayed()
            onNodeWithContentDescription("Navigate back").assertIsDisplayed().performClick()
            onNodeWithContentDescription("Save").assertIsDisplayed().performClick()
        }
    }

    @Test
    fun noteDetailsAppBarPrevieww_isDisplayed() {
        composeTestRule.setContent {
            NoteDetailsAppBarPreview()
        }

        composeTestRule.apply {
            onNodeWithText("Note details").assertIsDisplayed()
            onNodeWithContentDescription("Navigate back").assertIsDisplayed().performClick()
            onNodeWithContentDescription("Share note").assertIsDisplayed().performClick()
        }
    }

    @Test
    fun folderDetailsAppBarPrevieww_isDisplayed() {
        composeTestRule.setContent {
            FolderDetailsAppBarPreview()
        }

        composeTestRule.apply {
            onNodeWithText("Folder details").assertIsDisplayed()
            onNodeWithContentDescription("Navigate back").assertIsDisplayed().performClick()
            onNodeWithContentDescription("Sort").assertIsDisplayed().performClick()
            onNodeWithContentDescription("Grid view").assertIsDisplayed().performClick()
        }
    }
}