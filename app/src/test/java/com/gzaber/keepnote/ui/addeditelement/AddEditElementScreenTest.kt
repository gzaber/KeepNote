package com.gzaber.keepnote.ui.addeditelement

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
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
class AddEditElementScreenTest {

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
    fun addEditElementScreen_createNoteMode_correctTitleAndLabelsAreDisplayed() = runTest {
        setContent(isNoteArg = true)

        composeTestRule.apply {
            onNodeWithText("Create note").assertIsDisplayed()
            onNodeWithText("Title").assertIsDisplayed()
            onNodeWithText("Content").assertIsDisplayed()
        }
    }

    @Test
    fun addEditElementScreen_createChildNoteMode_correctTitleAndLabelsAreDisplayed() = runTest {
        val folderId =
            foldersRepository.createFolder(Element.empty().copy(name = "folder").toFolder())

        setContent(
            isNoteArg = true,
            folderIdArg = "$folderId"
        )

        composeTestRule.apply {
            onNodeWithText("Create note").assertIsDisplayed()
            onNodeWithText("Title").assertIsDisplayed()
            onNodeWithText("Content").assertIsDisplayed()
        }
    }

    @Test
    fun addEditElementScreen_createFolderMode_correctTitleAndLabelAreDisplayed() = runTest {
        setContent(isNoteArg = false)

        composeTestRule.apply {
            onNodeWithText("Create folder").assertIsDisplayed()
            onNodeWithText("Title").assertIsDisplayed()
        }
    }

    @Test
    fun addEditElementScreen_updateNoteMode_correctTitleAndLabelsAndValuesAreDisplayed() = runTest {
        val noteId = notesRepository.createNote(
            Element.empty().copy(name = "note", content = "content").toNote()
        )

        setContent(
            isNoteArg = true,
            elementIdArg = "$noteId"
        )

        composeTestRule.apply {
            onNodeWithText("Update note").assertIsDisplayed()
            waitUntilExactlyOneExists(hasText("Title"))
            onNodeWithText("Title").assertIsDisplayed()
            onNodeWithText("Content").assertIsDisplayed()
            onNodeWithText("note").assertIsDisplayed()
            onNodeWithText("content").assertIsDisplayed()
        }
    }

    @Test
    fun addEditElementScreen_updateFolderMode_correctTitleAndLabelAndValueAreDisplayed() = runTest {
        val folderId =
            foldersRepository.createFolder(Element.empty().copy(name = "folder").toFolder())

        setContent(
            isNoteArg = false,
            elementIdArg = "$folderId"
        )

        composeTestRule.apply {
            onNodeWithText("Update folder").assertIsDisplayed()
            waitUntilExactlyOneExists(hasText("Title"))
            onNodeWithText("Title").assertIsDisplayed()
            onNodeWithText("folder").assertIsDisplayed()
        }
    }

    @Test
    fun addEditElementScreen_saveButtonCanBeClicked() = runTest {
        setContent()

        composeTestRule.onNodeWithContentDescription("Save").assertIsDisplayed().performClick()
    }

    @Test
    fun addEditElementScreen_backButtonCanBeClicked() = runTest {
        setContent()

        composeTestRule.onNodeWithContentDescription("Navigate back").assertIsDisplayed()
            .performClick()
    }

    private fun setContent(
        isNoteArg: Boolean = true,
        elementIdArg: String? = null,
        folderIdArg: String? = null,
    ) {
        composeTestRule.setContent {
            AddEditElementScreen(
                onBackClick = {},
                onSaved = {},
                viewModel = AddEditElementViewModel(
                    foldersRepository = foldersRepository,
                    notesRepository = notesRepository,
                    savedStateHandle = SavedStateHandle(
                        mapOf(
                            KeepNoteDestinationArgs.IS_NOTE_ARG to isNoteArg,
                            KeepNoteDestinationArgs.ELEMENT_ID_ARG to elementIdArg,
                            KeepNoteDestinationArgs.FOLDER_ID_ARG to folderIdArg
                        )
                    )
                )
            )
        }
    }
}