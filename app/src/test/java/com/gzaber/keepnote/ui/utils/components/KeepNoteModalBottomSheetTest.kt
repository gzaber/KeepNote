package com.gzaber.keepnote.ui.utils.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class KeepNoteModalBottomSheetTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun keepNoteModalBottomSheet() {
        composeTestRule.setContent {
            KeepNoteModalBottomSheet(
                sheetState = SheetState(
                    skipPartiallyExpanded = false,
                    initialValue = SheetValue.Expanded
                ),
                onDismissRequest = {},
            ) {
                Text(text = "sheet content")
            }
        }

        composeTestRule.onNodeWithText("sheet content").assertIsDisplayed()
    }
}