package com.gzaber.keepnote.ui.notedetails

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import com.gzaber.keepnote.data.repository.NotesRepository
import com.gzaber.keepnote.ui.navigation.KeepNoteDestinationArgs
import com.gzaber.keepnote.ui.util.composable.LOADING_BOX_TAG
import com.gzaber.keepnote.ui.util.model.Element
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
class NoteDetailsScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @Inject
    lateinit var notesRepository: NotesRepository

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun noteDetailsScreen_titleIsDisplayed() = runTest {
        val noteId = insertNote()

        setContent(noteIdArg = "$noteId")

        composeTestRule.onNodeWithText("Note details").assertIsDisplayed()
    }

    @Test
    fun noteDetailsScreen_noteDetailsAreDisplayed() = runTest {
        val noteId = insertNote()

        setContent(noteIdArg = "$noteId")

        composeTestRule.apply {
            waitUntilDoesNotExist(hasTestTag(LOADING_BOX_TAG))
            onNodeWithText("note").assertIsDisplayed()
            onNodeWithText("content").assertIsDisplayed()
        }
    }

    @Test
    fun noteDetailsScreen_shareButtonCanBeClicked() = runTest {
        val noteId = insertNote()

        setContent(noteIdArg = "$noteId")

        composeTestRule.onNodeWithContentDescription("Share note").assertIsDisplayed()
            .performClick()
    }

    @Test
    fun noteDetailsScreen_backButtonCanBeClicked() = runTest {
        val noteId = insertNote()

        setContent(noteIdArg = "$noteId")

        composeTestRule.onNodeWithContentDescription("Navigate back").assertIsDisplayed()
            .performClick()
    }

    @Test
    fun noteDetailsScreen_errorMessageIsDisplayed() = runTest {
        setContent(noteIdArg = null)

        composeTestRule.onNodeWithText("Something went wrong").assertIsDisplayed()
    }

    private suspend fun insertNote(): Long {
        return notesRepository.createNote(
            Element.empty().copy(name = "note", content = "content")
                .toNote()
        )
    }

    private fun setContent(noteIdArg: String?) {
        composeTestRule.setContent {
            NoteDetailsScreen(
                onBackClick = {},
                viewModel = NoteDetailsViewModel(
                    notesRepository = notesRepository,
                    savedStateHandle = SavedStateHandle(
                        mapOf(
                            KeepNoteDestinationArgs.NOTE_ID_ARG to noteIdArg
                        )
                    )
                )
            )
        }
    }
}