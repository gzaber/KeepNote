package com.gzaber.keepnote.ui.folderdetails

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.gzaber.keepnote.data.repository.FoldersRepository
import com.gzaber.keepnote.data.repository.NotesRepository
import com.gzaber.keepnote.data.repository.model.Folder
import com.gzaber.keepnote.data.repository.model.Note
import com.gzaber.keepnote.ui.navigation.KeepNoteDestinationArgs
import com.gzaber.keepnote.ui.util.model.Element
import com.gzaber.keepnote.ui.util.model.toFolder
import com.gzaber.keepnote.util.MainDispatcherRule
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.util.Date

@RunWith(MockitoJUnitRunner::class)
class FolderDetailsViewModelTest {
    private lateinit var viewModel: FolderDetailsViewModel
    private val emptyFolder = Element.empty().toFolder().copy(date = Date())
    private val folder = Folder(id = 1, name = "folder", color = 111111, date = Date())
    private val note1 = Note(
        id = 1,
        folderId = 1,
        title = "titleB",
        content = "content1",
        color = 111111,
        date = Date(1000)
    )
    private val note2 = Note(
        id = 2,
        folderId = 1,
        title = "titleA",
        content = "content2",
        color = 222222,
        date = Date(2000)
    )

    @Mock
    private lateinit var mockFoldersRepository: FoldersRepository

    @Mock
    private lateinit var mockNotesRepository: NotesRepository

    @get:Rule
    val rule = MainDispatcherRule()

    @Before
    fun setUp() {
        `when`(mockFoldersRepository.getFolderByIdFlow(anyInt())).thenReturn(
            flow { emit(folder) }
        )
        `when`(mockNotesRepository.getSecondLevelNotesFlow(anyInt())).thenReturn(
            flow { emit(listOf(note1, note2)) }
        )
        viewModel = createViewModelWithSavedStateHandle("1")
    }

    private fun createViewModelWithSavedStateHandle(
        folderIdArg: String? = null
    ) = FolderDetailsViewModel(
        foldersRepository = mockFoldersRepository,
        notesRepository = mockNotesRepository,
        savedStateHandle = SavedStateHandle(
            mapOf(
                KeepNoteDestinationArgs.FOLDER_ID_ARG to folderIdArg
            )
        )
    )

    @Test
    fun initialState_folderIdIsNotNull_successStatus() = runTest {

        viewModel.uiState.test {
            assertEquals(
                FolderDetailsUiState(
                    status = FolderDetailsStatus.SUCCESS,
                    folder = folder,
                    notes = listOf(note1, note2)
                ), awaitItem()
            )
            expectNoEvents()
        }
    }

    @Test
    fun initialState_folderIdIsNull_failureStatus() = runTest {
        viewModel = createViewModelWithSavedStateHandle(null)

        viewModel.uiState.test {
            assertEquals(
                FolderDetailsUiState(
                    status = FolderDetailsStatus.FAILURE,
                    folder = emptyFolder
                ), awaitItem().copy(folder = emptyFolder)
            )
            expectNoEvents()
        }
    }

    @Test
    fun initialState_folderIdIsNotNull_foldersRepositoryFailure_failureStatus() = runTest {
        `when`(mockFoldersRepository.getFolderByIdFlow(anyInt())).thenReturn(
            flow { emit(folder) }.onStart { throw NullPointerException() }
        )
        viewModel = createViewModelWithSavedStateHandle("1")

        viewModel.uiState.test {
            assertEquals(
                FolderDetailsUiState(
                    status = FolderDetailsStatus.FAILURE,
                    notes = listOf(note1, note2),
                    folder = emptyFolder
                ), awaitItem().copy(folder = emptyFolder)
            )
            expectNoEvents()
        }
    }

    @Test
    fun initialState_folderIdIsNotNull_notesRepositoryFailure_failureStatus() = runTest {
        `when`(mockNotesRepository.getSecondLevelNotesFlow(anyInt())).thenReturn(
            flow { emit(listOf(note1, note2)) }.onStart { throw NullPointerException() }
        )
        viewModel = createViewModelWithSavedStateHandle("1")

        viewModel.uiState.test {
            assertEquals(
                FolderDetailsUiState(
                    status = FolderDetailsStatus.FAILURE,
                    folder = folder
                ), awaitItem()
            )
            expectNoEvents()
        }
    }

    @Test
    fun toggleView() = runTest {
        val uiState = FolderDetailsUiState(
            status = FolderDetailsStatus.SUCCESS,
            folder = folder,
            notes = listOf(note1, note2)
        )

        viewModel.uiState.test {
            assertEquals(uiState, awaitItem())

            viewModel.toggleView()
            assertEquals(
                uiState.copy(isGridView = true),
                awaitItem()
            )

            viewModel.toggleView()
            assertEquals(
                uiState.copy(isGridView = false),
                awaitItem()
            )
            expectNoEvents()
        }
    }

    @Test
    fun onSortOptionSelected() = runTest {
        val dateSelectedOption = SortInfo().sortRadioOptions[0]
        val nameSelectedOption = SortInfo().sortRadioOptions[1]
        val uiState = FolderDetailsUiState(
            status = FolderDetailsStatus.SUCCESS,
            folder = folder,
            notes = listOf(note1, note2)
        )

        viewModel.uiState.test {
            assertEquals(uiState, awaitItem())

            viewModel.onSortOptionSelected(nameSelectedOption)
            assertEquals(
                uiState.copy(
                    notes = listOf(note2, note1),
                    sortInfo = SortInfo(sortSelectedOption = nameSelectedOption)
                ),
                awaitItem()
            )

            viewModel.onSortOptionSelected(dateSelectedOption)
            assertEquals(
                uiState.copy(
                    notes = listOf(note1, note2),
                    sortInfo = SortInfo(sortSelectedOption = dateSelectedOption)
                ),
                awaitItem()
            )
            expectNoEvents()
        }
    }

    @Test
    fun onOrderOptionSelected() = runTest {
        val ascendingSelectedOption = SortInfo().orderRadioOptions[0]
        val descendingSelectedOption = SortInfo().orderRadioOptions[1]
        val nameSelectedOption = SortInfo().sortRadioOptions[1]
        val uiState = FolderDetailsUiState(
            status = FolderDetailsStatus.SUCCESS,
            folder = folder,
            notes = listOf(note1, note2)
        )

        viewModel.uiState.test {
            assertEquals(uiState, awaitItem())

            viewModel.onOrderOptionSelected(descendingSelectedOption)
            assertEquals(
                uiState.copy(
                    notes = listOf(note2, note1),
                    sortInfo = SortInfo(orderSelectedOption = descendingSelectedOption)
                ),
                awaitItem()
            )

            viewModel.onOrderOptionSelected(ascendingSelectedOption)
            assertEquals(
                uiState.copy(
                    notes = listOf(note1, note2),
                    sortInfo = SortInfo(orderSelectedOption = ascendingSelectedOption)
                ),
                awaitItem()
            )

            viewModel.onSortOptionSelected(nameSelectedOption)
            assertEquals(
                uiState.copy(
                    notes = listOf(note2, note1),
                    sortInfo = SortInfo(sortSelectedOption = nameSelectedOption)
                ),
                awaitItem()
            )

            viewModel.onOrderOptionSelected(descendingSelectedOption)
            assertEquals(
                uiState.copy(
                    notes = listOf(note1, note2),
                    sortInfo = SortInfo(
                        sortSelectedOption = nameSelectedOption,
                        orderSelectedOption = descendingSelectedOption
                    )
                ),
                awaitItem()
            )

            expectNoEvents()
        }
    }

    @Test
    fun deleteNote_success() = runTest {

        viewModel.uiState.test {
            assertEquals(
                FolderDetailsUiState(
                    status = FolderDetailsStatus.SUCCESS,
                    folder = folder,
                    notes = listOf(note1, note2)
                ), awaitItem()
            )

            viewModel.deleteNote(1)
            expectNoEvents()
        }
        verify(mockNotesRepository).deleteNote(1)
    }

    @Test
    fun deleteNote_failure() = runTest {
        `when`(mockNotesRepository.deleteNote(anyInt())).thenThrow(NullPointerException())
        val uiState = FolderDetailsUiState(
            status = FolderDetailsStatus.SUCCESS,
            folder = folder,
            notes = listOf(note1, note2)
        )

        viewModel.uiState.test {
            assertEquals(uiState, awaitItem())

            viewModel.deleteNote(1)
            assertEquals(uiState.copy(isDeleteFailure = true), awaitItem())
            expectNoEvents()
        }
        verify(mockNotesRepository).deleteNote(1)
    }

    @Test
    fun snackbarMessageShown() = runTest {
        `when`(mockNotesRepository.deleteNote(anyInt())).thenThrow(NullPointerException())
        val uiState = FolderDetailsUiState(
            status = FolderDetailsStatus.SUCCESS,
            folder = folder,
            notes = listOf(note1, note2)
        )

        viewModel.uiState.test {
            assertEquals(uiState, awaitItem())

            viewModel.deleteNote(1)
            assertEquals(uiState.copy(isDeleteFailure = true), awaitItem())

            viewModel.snackbarMessageShown()
            assertEquals(uiState.copy(isDeleteFailure = false), awaitItem())
            expectNoEvents()
        }
    }
}