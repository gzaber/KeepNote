package com.gzaber.keepnote.ui.addeditelement.composable

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.unit.dp
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AddEditElementContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun addEditElementContent_isFolder_titleIsChanged() {
        val title = mutableStateOf("")
        composeTestRule.setContent {
            AddEditElementContent(
                isNote = false,
                contentPadding = PaddingValues(0.dp),
                title = title.value,
                content = "",
                onTitleChange = { title.value = it },
                onContentChange = {},
                onColorSelect = {},
                modifier = Modifier
            )
        }

        composeTestRule.apply {
            onNodeWithText("Title").assertIsDisplayed()
            onNodeWithTag(TITLE_TEXT_FIELD_TAG).assertIsDisplayed()
                .performTextInput("title text")
            onNodeWithTag(TITLE_TEXT_FIELD_TAG).assert(hasText("title text"))
            onNodeWithTag(COLOR_SELECTOR_TAG).assertIsDisplayed()
        }
    }

    @Test
    fun addEditElementContent_isNote_titleAndContentAreChanged() {
        val title = mutableStateOf("")
        val content = mutableStateOf("")
        composeTestRule.setContent {
            AddEditElementContent(
                isNote = true,
                contentPadding = PaddingValues(0.dp),
                title = title.value,
                content = content.value,
                onTitleChange = { title.value = it },
                onContentChange = { content.value = it },
                onColorSelect = {},
                modifier = Modifier
            )
        }

        composeTestRule.apply {
            onNodeWithText("Title").assertIsDisplayed()
            onNodeWithText("Content").assertIsDisplayed()
            onNodeWithTag(TITLE_TEXT_FIELD_TAG).assertIsDisplayed()
                .performTextInput("title text")
            onNodeWithTag(TITLE_TEXT_FIELD_TAG).assert(hasText("title text"))
            onNodeWithTag(CONTENT_TEXT_FIELD_TAG).assertIsDisplayed()
                .performTextInput("content text")
            onNodeWithTag(CONTENT_TEXT_FIELD_TAG).assert(hasText("content text"))
            onNodeWithTag(COLOR_SELECTOR_TAG).assertIsDisplayed()
        }
    }

    @Test
    fun addEditElementContentPreview_isDisplayed() {
        composeTestRule.setContent {
            AddEditElementContentPreview()
        }

        composeTestRule.apply {
            onNodeWithText("Title").assertIsDisplayed()
            onNodeWithText("Content").assertIsDisplayed()
            onNodeWithTag(TITLE_TEXT_FIELD_TAG).assertIsDisplayed().performTextInput("title")
            onNodeWithTag(CONTENT_TEXT_FIELD_TAG).assertIsDisplayed().performTextInput("content")
            onNodeWithTag(COLOR_SELECTOR_TAG).assertIsDisplayed()
            composeTestRule.onAllNodesWithTag(COLOR_CIRCLE_TAG).onLast().performClick()
        }
    }
}