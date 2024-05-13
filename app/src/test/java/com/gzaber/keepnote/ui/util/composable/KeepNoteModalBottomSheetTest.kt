package com.gzaber.keepnote.ui.util.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.gzaber.keepnote.util.RobolectricTestActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class KeepNoteModalBottomSheetTest {

    @get:Rule(order = 0)
    val robolectricTestActivityRule = RobolectricTestActivity()

    @get:Rule(order = 1)
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
                modifier = Modifier
            ) {
                Text(text = "sheet content")
            }
        }

        composeTestRule.onNodeWithText("sheet content").assertIsDisplayed()
    }
}