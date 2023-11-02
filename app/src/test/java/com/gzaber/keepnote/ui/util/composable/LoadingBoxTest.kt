package com.gzaber.keepnote.ui.util.composable

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LoadingBoxTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingBox_isDisplayed() {
        composeTestRule.setContent {
            LoadingBox(paddingValues = PaddingValues(0.dp), modifier = Modifier)
        }

        composeTestRule.onNodeWithTag(LOADING_BOX_TAG).assertIsDisplayed()
    }
}