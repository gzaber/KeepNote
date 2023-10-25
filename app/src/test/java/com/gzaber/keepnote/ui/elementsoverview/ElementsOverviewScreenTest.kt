package com.gzaber.keepnote.ui.elementsoverview

//import androidx.compose.ui.test.assertIsDisplayed
//import androidx.compose.ui.test.junit4.createComposeRule
//import androidx.compose.ui.test.onNodeWithText
//import com.gzaber.keepnote.data.repository.FoldersRepository
//import com.gzaber.keepnote.data.repository.NotesRepository
//import com.gzaber.keepnote.ui.utils.model.Element
//import com.gzaber.keepnote.ui.utils.model.toFolder
//import com.gzaber.keepnote.ui.utils.model.toNote
//import dagger.hilt.android.testing.HiltAndroidRule
//import dagger.hilt.android.testing.HiltAndroidTest
//import dagger.hilt.android.testing.HiltTestApplication
//import kotlinx.coroutines.test.runTest
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.robolectric.RobolectricTestRunner
//import org.robolectric.annotation.Config
//import javax.inject.Inject
//
//
//@RunWith(RobolectricTestRunner::class)
//@HiltAndroidTest
//@Config(application = HiltTestApplication::class)
//class ElementsOverviewScreenTest {
//
//    @get:Rule(order = 0)
//    val hiltRule = HiltAndroidRule(this)
//
//    @get:Rule(order = 1)
//    val composeTestRule = createComposeRule()
//
//    @Inject
//    lateinit var foldersRepository: FoldersRepository
//
//    @Inject
//    lateinit var notesRepository: NotesRepository
//
//    @Before
//    fun setUp() {
//        hiltRule.inject()
//    }
//
//    @Test
//    fun elementsOverviewScreen_appTitleIsDisplayed_folderAndNoteAreDisplayed() = runTest {
//        foldersRepository.createFolder(Element.empty().copy(name = "folder").toFolder())
//        notesRepository.createNote(
//            Element.empty().copy(name = "note", content = "content").toNote()
//        )
//
//        setContent()
//
//        composeTestRule.apply {
//            onNodeWithText("KeepNote").assertIsDisplayed()
//            onNodeWithText("folder").assertIsDisplayed()
//            onNodeWithText("note").assertIsDisplayed()
//            onNodeWithText("content").assertIsDisplayed()
//        }
//    }
//
//    private fun setContent() {
//        composeTestRule.setContent {
//            ElementsOverviewScreen(
//                onElementClick = { _, _ -> },
//                onCreateElement = {},
//                onUpdateElement = { _, _ -> },
//                viewModel = ElementsOverviewViewModel(
//                    foldersRepository = foldersRepository,
//                    notesRepository = notesRepository
//                )
//            )
//        }
//    }
//}