package com.gzaber.keepnote.ui.utils.components

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.gzaber.keepnote.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BottomSheetContentsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun twoButtonsBottomSheetContent_iconsAndTextsAreDisplayed() {
        composeTestRule.setContent {
            TwoButtonsBottomSheetContent(
                firstButtonOnClick = {},
                firstButtonIconRes = R.drawable.ic_folder,
                firstButtonContentDescriptionRes = R.string.create_folder,
                firstButtonTextRes = R.string.folder,
                secondButtonOnClick = {},
                secondButtonIconRes = R.drawable.ic_note,
                secondButtonContentDescriptionRes = R.string.create_note,
                secondButtonTextRes = R.string.note
            )
        }

        composeTestRule.apply {
            onNodeWithText("Folder")
                .assertIsDisplayed()
                .assertContentDescriptionEquals("Create folder")
                .assertHasClickAction()
            onNodeWithText("Note")
                .assertIsDisplayed()
                .assertContentDescriptionEquals("Create note")
                .assertHasClickAction()
        }
    }

    @Test
    fun createElementBottomSheetContent_textIsDisplayed_buttonsAreClickable() {
        composeTestRule.setContent {
            CreateElementBottomSheetContent(
                folderButtonOnClick = {},
                noteButtonOnClick = {}
            )
        }

        composeTestRule.apply {
            onNodeWithText("Folder")
                .assertIsDisplayed()
                .assertContentDescriptionEquals("Create folder")
                .assertHasClickAction()
            onNodeWithText("Note")
                .assertIsDisplayed()
                .assertContentDescriptionEquals("Create note")
                .assertHasClickAction()
        }
    }

    @Test
    fun createElementBottomSheetContentPreview_textIsDisplayed_buttonsAreClickable() {
        composeTestRule.setContent {
            CreateElementBottomSheetContentPreview()
        }

        composeTestRule.apply {
            onNodeWithText("Folder")
                .assertIsDisplayed()
                .assertContentDescriptionEquals("Create folder")
                .performClick()
            onNodeWithText("Note")
                .assertIsDisplayed()
                .assertContentDescriptionEquals("Create note")
                .performClick()
        }
    }

    @Test
    fun editDeleteElementBottomSheetContent_textIsDisplayed_buttonsAreClickable() {
        composeTestRule.setContent {
            EditDeleteElementBottomSheetContent(
                editButtonOnClick = {},
                deleteButtonOnClick = {}
            )
        }

        composeTestRule.apply {
            onNodeWithText("Delete")
                .assertIsDisplayed()
                .assertContentDescriptionEquals("Delete element")
                .assertHasClickAction()
            onNodeWithText("Edit")
                .assertIsDisplayed()
                .assertContentDescriptionEquals("Edit element")
                .assertHasClickAction()
        }
    }

    @Test
    fun editDeleteElementBottomSheetContentPreview_textIsDisplayed_buttonsAreClickable() {
        composeTestRule.setContent {
            EditDeleteElementBottomSheetContentPreview()
        }

        composeTestRule.apply {
            onNodeWithText("Delete")
                .assertIsDisplayed()
                .assertContentDescriptionEquals("Delete element")
                .performClick()
            onNodeWithText("Edit")
                .assertIsDisplayed()
                .assertContentDescriptionEquals("Edit element")
                .performClick()
        }
    }

    @Test
    fun radioRow_textIsDisplayed_isSelectable() {
        val isSelected = mutableStateOf(false)
        composeTestRule.setContent {
            RadioRow(
                selected = isSelected.value,
                onClick = { isSelected.value = !isSelected.value },
                text = "option"
            )
        }

        composeTestRule.onNodeWithText("option").assertIsDisplayed().assertIsNotSelected()
            .performClick()
        composeTestRule.onNodeWithText("option").assertIsDisplayed().assertIsSelected()
    }

    @Test
    fun bottomSheetRadioGroup_textIsDisplayed_optionsAreSelectable() {
        val selectedOption = mutableStateOf(R.string.radio_date)

        composeTestRule.setContent {
            BottomSheetRadioGroup(
                titleRes = R.string.sort_by,
                radioOptions = listOf(R.string.radio_date, R.string.radio_name),
                selectedOption = selectedOption.value,
                onOptionSelected = { selectedOption.value = it }
            )
        }

        composeTestRule.apply {
            onNodeWithText("Sort by").assertIsDisplayed()
            onNodeWithText("date").assertIsDisplayed().assertIsSelected()
            onNodeWithText("name").assertIsDisplayed().performClick()
            onNodeWithText("name").assertIsDisplayed().assertIsSelected()
        }
    }

    @Test
    fun sortBottomSheetContent_twoGroupsAreDisplayed() {
        val sortRadioOptions =
            listOf(R.string.radio_date, R.string.radio_name)
        val orderRadioOptions =
            listOf(R.string.radio_ascending, R.string.radio_descending)

        composeTestRule.setContent {
            SortBottomSheetContent(
                sortRadioOptions = sortRadioOptions,
                sortSelectedOption = sortRadioOptions.first(),
                onSortOptionSelected = {},
                orderRadioOptions = orderRadioOptions,
                orderSelectedOption = orderRadioOptions.first(),
                onOrderOptionSelected = {}
            )
        }

        composeTestRule.apply {
            onNodeWithText("Sort by").assertIsDisplayed()
            onNodeWithText("date").assertIsDisplayed().assertIsSelected()
            onNodeWithText("name").assertIsDisplayed().assertIsNotSelected()
            onNodeWithText("Order").assertIsDisplayed()
            onNodeWithText("ascending").assertIsDisplayed().assertIsSelected()
            onNodeWithText("descending").assertIsDisplayed().assertIsNotSelected()
        }
    }

    @Test
    fun twoGroupsSortBottomSheetContentPreview_twoGroupsAreDisplayed() {
        composeTestRule.setContent {
            TwoGroupsSortBottomSheetContentPreview()
        }

        composeTestRule.apply {
            onNodeWithText("Sort by").assertIsDisplayed()
            onNodeWithText("date").assertIsDisplayed().assertIsSelected()
            onNodeWithText("name").assertIsDisplayed().assertIsNotSelected()
            onNodeWithText("Order").assertIsDisplayed()
            onNodeWithText("ascending").assertIsDisplayed().assertIsSelected()
            onNodeWithText("descending").assertIsDisplayed().assertIsNotSelected()
        }
    }

    @Test
    fun sortBottomSheetContent_threeGroupsAreDisplayed() {
        val sortRadioOptions =
            listOf(R.string.radio_date, R.string.radio_name)
        val orderRadioOptions =
            listOf(R.string.radio_ascending, R.string.radio_descending)
        val firstElementsRadioOptions =
            listOf(R.string.radio_folders, R.string.radio_notes, R.string.radio_not_applicable)

        composeTestRule.setContent {
            SortBottomSheetContent(
                sortRadioOptions = sortRadioOptions,
                sortSelectedOption = sortRadioOptions.first(),
                onSortOptionSelected = {},
                orderRadioOptions = orderRadioOptions,
                orderSelectedOption = orderRadioOptions.first(),
                onOrderOptionSelected = {},
                firstElementsRadioOptions = firstElementsRadioOptions,
                firstElementsSelectedOption = firstElementsRadioOptions.first(),
                onFirstElementsOptionSelected = {}
            )
        }

        composeTestRule.apply {
            onNodeWithText("Sort by").assertIsDisplayed()
            onNodeWithText("date").assertIsDisplayed().assertIsSelected()
            onNodeWithText("name").assertIsDisplayed().assertIsNotSelected()
            onNodeWithText("Order").assertIsDisplayed()
            onNodeWithText("ascending").assertIsDisplayed().assertIsSelected()
            onNodeWithText("descending").assertIsDisplayed().assertIsNotSelected()
            onNodeWithText("First elements").assertIsDisplayed()
            onNodeWithText("folders").assertIsDisplayed().assertIsSelected()
            onNodeWithText("notes").assertIsDisplayed().assertIsNotSelected()
            onNodeWithText("n/a").assertIsDisplayed().assertIsNotSelected()
        }
    }

    @Test
    fun threeGroupsSortBottomSheetContentPreview_threeGroupsAreDisplayed() {
        composeTestRule.setContent {
            ThreeGroupsSortBottomSheetContentPreview()
        }

        composeTestRule.apply {
            onNodeWithText("Sort by").assertIsDisplayed()
            onNodeWithText("date").assertIsDisplayed().assertIsSelected()
            onNodeWithText("name").assertIsDisplayed().assertIsNotSelected()
            onNodeWithText("Order").assertIsDisplayed()
            onNodeWithText("ascending").assertIsDisplayed().assertIsSelected()
            onNodeWithText("descending").assertIsDisplayed().assertIsNotSelected()
            onNodeWithText("First elements").assertIsDisplayed()
            onNodeWithText("folders").assertIsDisplayed().assertIsSelected()
            onNodeWithText("notes").assertIsDisplayed().assertIsNotSelected()
            onNodeWithText("n/a").assertIsDisplayed().assertIsNotSelected()
        }
    }
}