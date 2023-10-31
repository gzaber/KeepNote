package com.gzaber.keepnote.ui.folderdetails

import androidx.compose.ui.test.assertIsDisplayed
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
        setContent()

        composeTestRule.onNodeWithText("Folder details").assertIsDisplayed()
    }

    @Test
    fun folderDetailsScreen_notesAreDisplayed() = runTest {
        val folderId = insertElements()
        setContent(folderIdArg = "$folderId")

        composeTestRule.apply {
            waitForIdle()
            onNodeWithText("folder").assertIsDisplayed()
            onNodeWithText("note1").assertIsDisplayed()
            onNodeWithText("content1").assertIsDisplayed()
            onNodeWithText("note2").assertIsDisplayed()
            onNodeWithText("content2").assertIsDisplayed()
        }
    }

    @Test
    fun folderDetailsScreen_viewIsChanged_notesAreDisplayed() = runTest {
        val folderId = insertElements()
        setContent(folderIdArg = "$folderId")

        composeTestRule.apply {
            onNodeWithContentDescription("Grid view").assertIsDisplayed().performClick()
            onNodeWithText("folder").assertIsDisplayed()
            onNodeWithText("note1").assertIsDisplayed()
            onNodeWithText("content1").assertIsDisplayed()
            onNodeWithText("note2").assertIsDisplayed()
            onNodeWithText("content2").assertIsDisplayed()
        }
    }

    @Test
    fun folderDetailsScreen_sortButtonIsClicked_modalBottomSheetIsDisplayed() = runTest {
        val folderId = insertElements()
        setContent(folderIdArg = "$folderId")

        composeTestRule.apply {
            onNodeWithContentDescription("Sort").assertIsDisplayed().performClick()
            onNodeWithText("Sort by").assertIsDisplayed()
            onNodeWithText("Order").assertIsDisplayed()
        }
    }

    @Test
    fun folderDetailsScreen_fabCanBeClicked() = runTest {
        val folderId = insertElements()
        setContent(folderIdArg = "$folderId")

        composeTestRule.apply {
            onNodeWithContentDescription("Create note").assertIsDisplayed().performClick()
        }
    }

    @Test
    fun folderDetailsScreen_noteItemIsLongClicked_modalBottomSheetIsDisplayed() = runTest {
        val folderId = insertElements()
        setContent(folderIdArg = "$folderId")

        composeTestRule.apply {
            waitForIdle()
            onNodeWithText("note1").assertIsDisplayed().performTouchInput { longClick() }
            onNodeWithText("Edit").assertIsDisplayed().performClick()
            onNodeWithText("note2").assertIsDisplayed().performTouchInput { longClick() }
            onNodeWithText("Delete").assertIsDisplayed().performClick()
        }
    }

    private suspend fun insertElements(): Long {
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

    private fun setContent(folderIdArg: String? = null) {
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