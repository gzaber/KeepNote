package com.gzaber.keepnote

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import com.gzaber.keepnote.ui.theme.KeepNoteTheme
import com.gzaber.keepnote.ui.utils.components.DetailsHeader
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText

@RunWith(RobolectricTestRunner::class)
class DetailsHeaderTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun myTest() {
        composeTestRule.setContent {
            KeepNoteTheme {
                DetailsHeader(title = "header", color = Color.Red)
            }
        }

        composeTestRule.onNodeWithText("header").assertIsDisplayed()
    }
}