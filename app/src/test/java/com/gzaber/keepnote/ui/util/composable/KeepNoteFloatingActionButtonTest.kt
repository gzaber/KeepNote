package com.gzaber.keepnote.ui.util.composable

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import com.gzaber.keepnote.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class KeepNoteFloatingActionButtonTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun keepNoteFloatingActionButton_isDisplayed_isClickable() {
        composeTestRule.setContent {
            KeepNoteFloatingActionButton(
                contentDescription = R.string.create_element,
                onClick = { }
            )
        }

        composeTestRule.onNodeWithContentDescription("Create element").assertIsDisplayed()
            .assertHasClickAction()
    }

    @Test
    fun keepNoteFloatingActionButtonPreview_isDisplayed() {
        composeTestRule.setContent {
            KeepNoteFloatingActionButtonPreview()
        }

        composeTestRule.onNodeWithContentDescription("Create note").assertIsDisplayed()
            .performClick()
    }
}