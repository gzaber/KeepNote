package com.gzaber.keepnote.ui.navigation

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.performTouchInput
import com.gzaber.keepnote.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalTestApi::class)
@RunWith(RobolectricTestRunner::class)
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
class AppNavigationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun elementsOverviewScreen_fabIsClicked_folderIsSelected_navigatesToAddEditElementScreen() =
        runTest {
            navigateToFolderCreation()
        }

    @Test
    fun elementsOverviewScreen_fabIsClicked_noteIsSelected_navigatesToAddEditElementScreen() =
        runTest {
            navigateToNoteCreation()
        }

    @Test
    fun addEditElementScreen_folderIsCreated_navigatesBack_folderIsDisplayed() = runTest {
        composeTestRule.apply {
            navigateToFolderCreation()
            createFolder()
        }
    }

    @Test
    fun addEditElementScreen_noteIsCreated_navigatesBack_noteIsDisplayed() = runTest {
        composeTestRule.apply {
            navigateToNoteCreation()
            createNote()
        }
    }

    @Test
    fun addEditElementScreen_createFolderMode_navigatesBack() = runTest {
        navigateToFolderCreation()
        navigateBack()
        composeTestRule.onNodeWithText("KeepNote").assertIsDisplayed()
    }

    @Test
    fun addEditElementScreen_createNoteMode_navigatesBack() = runTest {
        navigateToNoteCreation()
        navigateBack()
        composeTestRule.onNodeWithText("KeepNote").assertIsDisplayed()
    }

    @Test
    fun elementsOverviewScreen_folderIsClicked_navigatesToFolderDetailsScreen_navigatesBack() {
        navigateToFolderCreation()
        createFolder()
        composeTestRule.apply {
            onNodeWithText("folder").assertIsDisplayed().performClick()
            onNodeWithText("Folder details").assertIsDisplayed()
            onNodeWithText("folder").assertIsDisplayed()
        }
        navigateBack()
        composeTestRule.apply {
            onNodeWithText("KeepNote").assertIsDisplayed()
            onNodeWithText("folder").assertIsDisplayed()
        }
    }

    @Test
    fun elementsOverviewScreen_noteIsClicked_navigatesToNoteDetailsScreen_navigatesBack() {
        navigateToNoteCreation()
        createNote()
        navigateToNoteDetails()
        navigateBack()
        composeTestRule.apply {
            onNodeWithText("KeepNote").assertIsDisplayed()
            onNodeWithText("note").assertIsDisplayed()
        }
    }

    @Test
    fun elementsOverviewScreen_folderIsLongClicked_navigatesToAddEditElementScreen_updatesValue() {
        navigateToFolderCreation()
        createFolder()
        composeTestRule.apply {
            onNodeWithText("folder").assertIsDisplayed().performTouchInput { longClick() }
            onNodeWithText("Edit").assertIsDisplayed().performClick()
            onNodeWithText("Update folder").assertIsDisplayed()
            waitUntilExactlyOneExists(hasText("Title"))
            onNodeWithText("Title").assertIsDisplayed().performTextReplacement("updatedFolder")
            onNodeWithContentDescription("Save").assertIsDisplayed().performClick()
            waitUntilExactlyOneExists(hasText("KeepNote"))
            onNodeWithText("KeepNote").assertIsDisplayed()
            onNodeWithText("updatedFolder").assertIsDisplayed()
        }
    }

    @Test
    fun elementsOverviewScreen_noteIsLongClicked_navigatesToAddEditElementScreen_updatesValue() {
        navigateToNoteCreation()
        createNote()
        composeTestRule.apply {
            onNodeWithText("note").assertIsDisplayed().performTouchInput { longClick() }
            onNodeWithText("Edit").assertIsDisplayed().performClick()
            onNodeWithText("Update note").assertIsDisplayed()
            waitUntilExactlyOneExists(hasText("Title"))
            onNodeWithText("Title").assertIsDisplayed().performTextReplacement("updatedNote")
            onNodeWithContentDescription("Save").assertIsDisplayed().performClick()
            waitUntilExactlyOneExists(hasText("KeepNote"))
            onNodeWithText("KeepNote").assertIsDisplayed()
            onNodeWithText("updatedNote").assertIsDisplayed()
        }
    }

    @Test
    fun elementsOverviewScreen_folderIsLongClicked_navigatesToAddEditElementScreen_navigatesBack() {
        navigateToFolderCreation()
        createFolder()
        composeTestRule.apply {
            onNodeWithText("folder").assertIsDisplayed().performTouchInput { longClick() }
            onNodeWithText("Edit").assertIsDisplayed().performClick()
            onNodeWithText("Update folder").assertIsDisplayed()
            waitUntilExactlyOneExists(hasText("folder"))
            onNodeWithText("folder").assertIsDisplayed()
        }
        navigateBack()
        composeTestRule.apply {
            onNodeWithText("KeepNote").assertIsDisplayed()
            onNodeWithText("folder").assertIsDisplayed()
        }
    }

    @Test
    fun elementsOverviewScreen_noteIsLongClicked_navigatesToAddEditElementScreen_navigatesBack() {
        navigateToNoteCreation()
        createNote()
        composeTestRule.apply {
            onNodeWithText("note").assertIsDisplayed().performTouchInput { longClick() }
            onNodeWithText("Edit").assertIsDisplayed().performClick()
            onNodeWithText("Update note").assertIsDisplayed()
            waitUntilExactlyOneExists(hasText("note"))
            onNodeWithText("note").assertIsDisplayed()
        }
        navigateBack()
        composeTestRule.apply {
            onNodeWithText("KeepNote").assertIsDisplayed()
            onNodeWithText("note").assertIsDisplayed()
        }
    }

    @Test
    fun folderDetails_fabIsClicked_navigatesToAddEditElementScreen_noteIsCreated() =
        runTest {
            navigateToFolderCreation()
            createFolder()
            navigateToFolderDetails()
            composeTestRule.onNodeWithContentDescription("Create note").assertIsDisplayed()
                .performClick()
            createNote(parentScreenTitle = "Folder details")
        }

    @Test
    fun folderDetails_fabIsClicked_navigatesToAddEditElementScreen_navigatesBack() =
        runTest {
            navigateToFolderCreation()
            createFolder()
            navigateToFolderDetails()
            composeTestRule.onNodeWithContentDescription("Create note").assertIsDisplayed()
                .performClick()
            navigateBack()
            composeTestRule.onNodeWithText("Folder details").assertIsDisplayed()
        }

    @Test
    fun folderDetails_noteIsClicked_navigatesToNoteDetailsScreen_navigatesBack() =
        runTest {
            navigateToFolderCreation()
            createFolder()
            navigateToFolderDetails()
            composeTestRule.onNodeWithContentDescription("Create note").assertIsDisplayed()
                .performClick()
            createNote(parentScreenTitle = "Folder details")
            navigateToNoteDetails()
            navigateBack()
            composeTestRule.apply {
                onNodeWithText("Folder details").assertIsDisplayed()
                onNodeWithText("note").assertIsDisplayed()
            }
        }

    @Test
    fun folderDetails_noteIsLongClicked_navigatesToAddEditElementScreen_updatesValue() {
        navigateToFolderCreation()
        createFolder()
        navigateToFolderDetails()
        composeTestRule.onNodeWithContentDescription("Create note").assertIsDisplayed()
            .performClick()
        createNote(parentScreenTitle = "Folder details")
        composeTestRule.apply {
            onNodeWithText("note").assertIsDisplayed().performTouchInput { longClick() }
            onNodeWithText("Edit").assertIsDisplayed().performClick()
            onNodeWithText("Update note").assertIsDisplayed()
            waitUntilExactlyOneExists(hasText("Title"))
            onNodeWithText("Title").assertIsDisplayed().performTextReplacement("updatedNote")
            onNodeWithContentDescription("Save").assertIsDisplayed().performClick()
            waitUntilExactlyOneExists(hasText("Folder details"))
            onNodeWithText("Folder details").assertIsDisplayed()
            onNodeWithText("updatedNote").assertIsDisplayed()
        }
    }

    @Test
    fun folderDetails_noteIsLongClicked_navigatesToAddEditElementScreen_navigatesBack() {
        navigateToFolderCreation()
        createFolder()
        navigateToFolderDetails()
        composeTestRule.onNodeWithContentDescription("Create note").assertIsDisplayed()
            .performClick()
        createNote(parentScreenTitle = "Folder details")
        composeTestRule.apply {
            onNodeWithText("note").assertIsDisplayed().performTouchInput { longClick() }
            onNodeWithText("Edit").assertIsDisplayed().performClick()
            onNodeWithText("Update note").assertIsDisplayed()
            waitUntilExactlyOneExists(hasText("note"))
            onNodeWithText("note").assertIsDisplayed()
        }
        navigateBack()
        composeTestRule.apply {
            onNodeWithText("Folder details").assertIsDisplayed()
            onNodeWithText("note").assertIsDisplayed()
            onNodeWithText("content").assertIsDisplayed()
        }
    }

    private fun navigateBack() {
        composeTestRule.onNodeWithContentDescription("Navigate back").performClick()
    }

    private fun navigateToFolderCreation() {
        composeTestRule.apply {
            onNodeWithText("KeepNote").assertIsDisplayed()
            onNodeWithContentDescription("Create element").assertIsDisplayed().performClick()
            onNodeWithText("Folder").assertIsDisplayed().performClick()
            onNodeWithText("Create folder").assertIsDisplayed()
        }
    }

    private fun navigateToNoteCreation() {
        composeTestRule.apply {
            onNodeWithText("KeepNote").assertIsDisplayed()
            onNodeWithContentDescription("Create element").assertIsDisplayed().performClick()
            onNodeWithText("Note").assertIsDisplayed().performClick()
            onNodeWithText("Create note").assertIsDisplayed()
        }
    }

    private fun createFolder() {
        composeTestRule.apply {
            onNodeWithText("Title").assertIsDisplayed().performTextInput("folder")
            onNodeWithContentDescription("Save").assertIsDisplayed().performClick()
            waitUntilExactlyOneExists(hasText("KeepNote"))
            onNodeWithText("KeepNote").assertIsDisplayed()
            onNodeWithText("folder").assertIsDisplayed()
        }
    }

    private fun createNote(parentScreenTitle: String = "KeepNote") {
        composeTestRule.apply {
            onNodeWithText("Title").assertIsDisplayed().performTextInput("note")
            onNodeWithText("Content").assertIsDisplayed().performTextInput("content")
            onNodeWithContentDescription("Save").assertIsDisplayed().performClick()

            waitUntilExactlyOneExists(hasText(parentScreenTitle))
            onNodeWithText(parentScreenTitle).assertIsDisplayed()
            onNodeWithText("note").assertIsDisplayed()
            onNodeWithText("content").assertIsDisplayed()
        }
    }

    private fun navigateToNoteDetails() {
        composeTestRule.apply {
            onNodeWithText("note").assertIsDisplayed().performClick()
            onNodeWithText("Note details").assertIsDisplayed()
            onNodeWithText("note").assertIsDisplayed()
        }
    }

    private fun navigateToFolderDetails() {
        composeTestRule.apply {
            onNodeWithText("folder").assertIsDisplayed().performClick()
            onNodeWithText("Folder details").assertIsDisplayed()
            waitUntilExactlyOneExists(hasText("folder"))
            onNodeWithText("folder").assertIsDisplayed()
        }
    }
}