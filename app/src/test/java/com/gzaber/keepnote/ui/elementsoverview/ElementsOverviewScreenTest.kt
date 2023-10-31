package com.gzaber.keepnote.ui.elementsoverview

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import com.gzaber.keepnote.data.repository.FoldersRepository
import com.gzaber.keepnote.data.repository.NotesRepository
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
class ElementsOverviewScreenTest {

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
    fun elementsOverviewScreen_appTitleAndFolderAndNoteAreDisplayed() = runTest {
        foldersRepository.createFolder(Element.empty().copy(name = "folder").toFolder())
        notesRepository.createNote(
            Element.empty().copy(name = "note", content = "content").toNote()
        )

        setContent()

        composeTestRule.apply {
            mainClock.advanceTimeByFrame()
            onNodeWithText("KeepNote").assertIsDisplayed()
            onNodeWithText("folder").assertIsDisplayed()
            onNodeWithText("note").assertIsDisplayed()
            onNodeWithText("content").assertIsDisplayed()
        }
    }

    @Test
    fun elementsOverviewScreen_viewIsChanged_folderAndNoteAreDisplayed() = runTest {
        foldersRepository.createFolder(Element.empty().copy(name = "folder").toFolder())
        notesRepository.createNote(
            Element.empty().copy(name = "note", content = "content").toNote()
        )

        setContent()

        composeTestRule.apply {
            onNodeWithContentDescription("Grid view").performClick()
            onNodeWithText("folder").assertIsDisplayed()
            onNodeWithText("note").assertIsDisplayed()
            onNodeWithText("content").assertIsDisplayed()
        }
    }

    @Test
    fun elementsOverviewScreen_sortButtonIsClicked_modalBottomSheetIsDisplayed() = runTest {
        setContent()

        composeTestRule.apply {
            onNodeWithContentDescription("Sort").performClick()
            onNodeWithText("Sort by").assertIsDisplayed()
            onNodeWithText("Order").assertIsDisplayed()
            onNodeWithText("First elements").assertIsDisplayed()
        }
    }

    @Test
    fun elementsOverviewScreen_fabIsClicked_modalBottomSheetIsDisplayed() = runTest {
        setContent()

        composeTestRule.apply {
            onNodeWithContentDescription("Create element").performClick()
            onNodeWithText("Folder").assertIsDisplayed().performClick()
            onNodeWithContentDescription("Create element").performClick()
            onNodeWithText("Note").assertIsDisplayed().performClick()
        }
    }

    @Test
    fun elementsOverviewScreen_elementItemIsLongClicked_modalBottomSheetIsDisplayed() = runTest {
        foldersRepository.createFolder(Element.empty().copy(name = "folder").toFolder())

        setContent()

        composeTestRule.apply {
            mainClock.advanceTimeByFrame()
            onNodeWithText("folder").assertIsDisplayed().performTouchInput { longClick() }
            onNodeWithText("Edit").assertIsDisplayed().performClick()
            onNodeWithText("folder").assertIsDisplayed().performTouchInput { longClick() }
            onNodeWithText("Delete").assertIsDisplayed().performClick()
        }
    }

    private fun setContent() {
        composeTestRule.setContent {
            ElementsOverviewScreen(
                onElementClick = { _, _ -> },
                onCreateElement = {},
                onUpdateElement = { _, _ -> },
                viewModel = ElementsOverviewViewModel(
                    foldersRepository = foldersRepository,
                    notesRepository = notesRepository
                )
            )
        }
    }
}