package com.gzaber.keepnote.ui.addeditelement.composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.gzaber.keepnote.util.RobolectricTestActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ColorSelectorTest {

    @get:Rule(order = 0)
    val robolectricTestActivityRule = RobolectricTestActivity()

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @Test
    fun colorCircle_isSelected() {
        composeTestRule.setContent {
            ColorCircle(
                isSelected = true,
                color = Color.Red,
                onClick = {}, modifier = Modifier
            )
        }

        composeTestRule.onNodeWithTag(COLOR_CIRCLE_TAG).assertIsDisplayed().assertHasClickAction()
        composeTestRule.onNodeWithContentDescription("Selected color").assertIsDisplayed()
    }

    @Test
    fun colorCircle_isNotSelected() {
        composeTestRule.setContent {
            ColorCircle(
                isSelected = false,
                color = Color.Red,
                onClick = {},
                modifier = Modifier
            )
        }

        composeTestRule.onNodeWithTag(COLOR_CIRCLE_TAG).assertIsDisplayed().assertHasClickAction()
        composeTestRule.onNodeWithContentDescription("Selected color").assertDoesNotExist()
    }

    @Test
    fun colorSelector_hasColorCircles_colorIsSelected() {
        composeTestRule.setContent {
            ColorSelector(onColorSelect = {}, modifier = Modifier)
        }

        composeTestRule.onAllNodesWithTag(COLOR_CIRCLE_TAG).assertCountEquals(6).assertAll(
            hasClickAction()
        )
        composeTestRule.onNodeWithContentDescription("Selected color").assertIsDisplayed()
        composeTestRule.onAllNodesWithTag(COLOR_CIRCLE_TAG).onLast().performClick()
        composeTestRule.onNodeWithContentDescription("Selected color").assertIsDisplayed()
    }

    @Test
    fun colorPickerPreview_isDisplayed() {
        composeTestRule.setContent {
            ColorPickerPreview()
        }

        composeTestRule.onNodeWithTag(COLOR_SELECTOR_TAG).assertIsDisplayed()
    }
}