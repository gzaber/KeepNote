package com.gzaber.keepnote.ui.util.composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.gzaber.keepnote.util.RobolectricTestActivity

@RunWith(RobolectricTestRunner::class)
class DetailsHeaderTest {

    @get:Rule(order = 0)
    val robolectricTestActivityRule = RobolectricTestActivity()

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @Test
    fun titleAndSubtitle_areDisplayed() {
        composeTestRule.setContent {
            DetailsHeader(
                title = "header",
                subtitle = "2023",
                color = Color.Red,
                modifier = Modifier
            )
        }

        composeTestRule.onNodeWithText("header").assertIsDisplayed()
        composeTestRule.onNodeWithText("2023").assertIsDisplayed()
    }
}