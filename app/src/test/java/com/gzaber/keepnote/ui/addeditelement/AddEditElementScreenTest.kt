package com.gzaber.keepnote.ui.addeditelement

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import com.gzaber.keepnote.data.repository.FoldersRepository
import com.gzaber.keepnote.data.repository.NotesRepository
import com.gzaber.keepnote.ui.navigation.KeepNoteDestinationArgs
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
    fun addEditElementScreen_createNoteMode_correctTitleIsDisplayed() = runTest {
        setContent(AddEditElementMode.CREATE_NOTE)

        composeTestRule.onNodeWithText("Create note").assertIsDisplayed()
    }

    @Test
    fun addEditElementScreen_createChildNoteMode_correctTitleIsDisplayed() = runTest {
        setContent(AddEditElementMode.CREATE_CHILD_NOTE)

        composeTestRule.onNodeWithText("Create note").assertIsDisplayed()
    }

    @Test
    fun addEditElementScreen_createFolderMode_correctTitleIsDisplayed() = runTest {
        setContent(AddEditElementMode.CREATE_FOLDER)

        composeTestRule.onNodeWithText("Create folder").assertIsDisplayed()
    }

    @Test
    fun addEditElementScreen_updateNoteMode_correctTitleIsDisplayed() = runTest {
        setContent(AddEditElementMode.UPDATE_NOTE)

        composeTestRule.onNodeWithText("Update note").assertIsDisplayed()
    }

    @Test
    fun addEditElementScreen_updateFolderMode_correctTitleIsDisplayed() = runTest {
        setContent(AddEditElementMode.UPDATE_FOLDER)

        composeTestRule.onNodeWithText("Update folder").assertIsDisplayed()
    }

    @Test
    fun addEditElementScreen_createFolder_labelIsDisplayed() = runTest {
        setContent(AddEditElementMode.CREATE_FOLDER)

        composeTestRule.onNodeWithText("Title").assertIsDisplayed()
    }

    @Test
    fun addEditElementScreen_createNote_labelsAreDisplayed() = runTest {
        setContent(AddEditElementMode.CREATE_NOTE)

        composeTestRule.onNodeWithText("Title").assertIsDisplayed()
        composeTestRule.onNodeWithText("Content").assertIsDisplayed()
    }

    @Test
    fun addEditElementScreen_saveButtonCanBeClicked() = runTest {
        setContent(AddEditElementMode.CREATE_FOLDER)

        composeTestRule.onNodeWithContentDescription("Save").assertIsDisplayed().performClick()
    }

    @Test
    fun addEditElementScreen_backButtonCanBeClicked() = runTest {
        setContent(AddEditElementMode.CREATE_FOLDER)

        composeTestRule.onNodeWithContentDescription("Navigate back").assertIsDisplayed()
            .performClick()
    }

    private fun setContent(mode: AddEditElementMode) {
        var isNoteArg = true
        var elementIdArg: String? = null
        var folderIdArg: String? = null

        when (mode) {
            AddEditElementMode.CREATE_CHILD_NOTE -> folderIdArg = "1"
            AddEditElementMode.CREATE_NOTE -> {
                elementIdArg = null
                isNoteArg = true
            }

            AddEditElementMode.CREATE_FOLDER -> {
                elementIdArg = null
                isNoteArg = false
            }

            AddEditElementMode.UPDATE_NOTE -> {
                elementIdArg = "1"
                isNoteArg = true
            }

            AddEditElementMode.UPDATE_FOLDER -> {
                elementIdArg = "1"
                isNoteArg = false
            }
        }
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