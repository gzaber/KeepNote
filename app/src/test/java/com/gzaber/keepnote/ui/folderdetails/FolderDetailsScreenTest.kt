package com.gzaber.keepnote.ui.folderdetails

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.lifecycle.SavedStateHandle
import com.gzaber.keepnote.data.repository.FoldersRepository
import com.gzaber.keepnote.data.repository.NotesRepository
import com.gzaber.keepnote.ui.navigation.KeepNoteDestinationArgs
import com.gzaber.keepnote.ui.util.model.Element
import com.gzaber.keepnote.ui.util.model.toFolder
import com.gzaber.keepnote.ui.util.model.toNote
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
import javax.inject.Inject

@OptIn(ExperimentalTestApi::class)
@RunWith(RobolectricTestRunner::class)
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
class FolderDetailsScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @Inject
    lateinit var foldersRepository: FoldersRepository

    @Inject
    lateinit var notesRepository: NotesRepository

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun folderDetailsScreen_titleIsDisplayed() = runTest {
        val folderId = insertNotes()

        setContent(folderIdArg = "$folderId")

        composeTestRule.onNodeWithText("Folder details").assertIsDisplayed()
    }

    @Test
    fun folderDetailsScreen_notesAreDisplayedAndCanBeClicked() = runTest {
        val folderId = insertNotes()

        setContent(folderIdArg = "$folderId")

        composeTestRule.apply {
            waitUntilExactlyOneExists(hasText("folder"))
            onNodeWithText("folder").assertIsDisplayed()
            onNodeWithText("note1").assertIsDisplayed().performClick()
            onNodeWithText("content1").assertIsDisplayed().performClick()
            onNodeWithText("note2").assertIsDisplayed().performClick()
            onNodeWithText("content2").assertIsDisplayed().performClick()
        }
    }

    @Test
    fun folderDetailsScreen_noteItemIsLongClicked_modalBottomSheetIsDisplayed() = runTest {
        val folderId = insertNotes()

        setContent(folderIdArg = "$folderId")

        composeTestRule.apply {
            waitUntilExactlyOneExists(hasText("note1"))
            onNodeWithText("note1").assertIsDisplayed().performTouchInput { longClick() }
            onNodeWithText("Edit").assertIsDisplayed().performClick()
            onNodeWithText("note2").assertIsDisplayed().performTouchInput { longClick() }
            onNodeWithText("Delete").assertIsDisplayed().performClick()
        }
    }

    @Test
    fun folderDetailsScreen_viewIsChanged_notesAreDisplayed() = runTest {
        val folderId = insertNotes()

        setContent(folderIdArg = "$folderId")

        composeTestRule.apply {
            onNodeWithContentDescription("Grid view").assertIsDisplayed().performClick()
            waitUntilExactlyOneExists(hasText("folder"))
            onNodeWithText("folder").assertIsDisplayed()
            onNodeWithText("note1").assertIsDisplayed()
            onNodeWithText("content1").assertIsDisplayed()
            onNodeWithText("note2").assertIsDisplayed()
            onNodeWithText("content2").assertIsDisplayed()
        }
    }

    @Test
    fun folderDetailsScreen_sortButtonIsClicked_modalBottomSheetIsDisplayed() = runTest {
        val folderId = insertNotes()

        setContent(folderIdArg = "$folderId")

        composeTestRule.apply {
            onNodeWithContentDescription("Sort").assertIsDisplayed().performClick()
            onNodeWithText("Sort by").assertIsDisplayed()
            onNodeWithText("Order").assertIsDisplayed()
        }
    }

    @Test
    fun folderDetailsScreen_fabCanBeClicked() = runTest {
        val folderId = insertNotes()

        setContent(folderIdArg = "$folderId")

        composeTestRule.onNodeWithContentDescription("Create note").assertIsDisplayed()
            .performClick()
    }

    @Test
    fun folderDetailsScreen_backButtonCanBeClicked() = runTest {
        val folderId = insertNotes()

        setContent(folderIdArg = "$folderId")

        composeTestRule.onNodeWithContentDescription("Navigate back").assertIsDisplayed()
            .performClick()
    }

    @Test
    fun folderDetailsScreen_errorMessageIsDisplayed() = runTest {
        setContent(folderIdArg = null)

        composeTestRule.onNodeWithText("Something went wrong").assertIsDisplayed()
    }

    private suspend fun insertNotes(): Long {
        val folderId =
            foldersRepository.createFolder(Element.empty().copy(name = "folder").toFolder())
        notesRepository.createNote(
            Element.empty().copy(folderId = folderId.toInt(), name = "note1", content = "content1")
                .toNote()
        )
        notesRepository.createNote(
            Element.empty().copy(folderId = folderId.toInt(), name = "note2", content = "content2")
                .toNote()
        )

        return folderId
    }

    private fun setContent(folderIdArg: String?) {
        composeTestRule.setContent {
            FolderDetailsScreen(
                onNoteClick = {},
                onBackClick = {},
                onFabClick = {},
                onUpdateNote = {},
                viewModel = FolderDetailsViewModel(
                    foldersRepository = foldersRepository,
                    notesRepository = notesRepository,
                    savedStateHandle = SavedStateHandle(
                        mapOf(
                            KeepNoteDestinationArgs.FOLDER_ID_ARG to folderIdArg
                        )
                    )
                )
            )
        }
    }
}