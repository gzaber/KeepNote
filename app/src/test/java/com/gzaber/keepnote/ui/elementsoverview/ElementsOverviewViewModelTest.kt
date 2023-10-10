package com.gzaber.keepnote.ui.elementsoverview

import app.cash.turbine.test
import com.gzaber.keepnote.data.repository.FoldersRepository
import com.gzaber.keepnote.data.repository.NotesRepository
import com.gzaber.keepnote.data.repository.model.Folder
import com.gzaber.keepnote.data.repository.model.Note
import com.gzaber.keepnote.ui.utils.model.Element
import com.gzaber.keepnote.ui.utils.model.toElement
import com.gzaber.keepnote.util.MainDispatcherRule
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.util.Date


@RunWith(MockitoJUnitRunner::class)
class ElementsOverviewViewModelTest {

    private lateinit var viewModel: ElementsOverviewViewModel
    private val folderDate = Date(1000)
    private val noteDate = Date(2000)
    private val folder = Folder(id = 1, name = "folder", color = 111111, date = folderDate)
    private val note = Note(
        id = 1,
        folderId = null,
        title = "title",
        content = "content",
        color = 111111,
        date = noteDate
    )

    @Mock
    private lateinit var mockFoldersRepository: FoldersRepository

    @Mock
    private lateinit var mockNotesRepository: NotesRepository

    @get:Rule
    val rule = MainDispatcherRule()

    @Before
    fun setUp() {
        `when`(mockFoldersRepository.getAllFoldersFlow())
            .thenReturn(flow { emit(listOf(folder)) })
        `when`(mockNotesRepository.getFirstLevelNotesFlow())
            .thenReturn(flow { emit(listOf(note)) })
        createViewModel()
    }

    private fun createViewModel() {
        viewModel = ElementsOverviewViewModel(
            foldersRepository = mockFoldersRepository,
            notesRepository = mockNotesRepository
        )
    }

    @Test
    fun initialState_successStatus() = runTest {

        viewModel.uiState.test {
            assertEquals(
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = listOf(folder.toElement(), note.toElement())
                ), awaitItem()
            )
            expectNoEvents()
        }
    }

    @Test
    fun initialState_foldersRepositoryException_failureStatus() = runTest {
        `when`(mockFoldersRepository.getAllFoldersFlow())
            .thenReturn(flow<List<Folder>> { listOf(folder) }.onStart { throw NullPointerException() })

        createViewModel()

        viewModel.uiState.test {
            assertEquals(
                ElementsOverviewUiState(status = ElementsOverviewStatus.FAILURE), awaitItem()
            )
            expectNoEvents()
        }
    }

    @Test
    fun initialState_notesRepositoryException_failureStatus() = runTest {
        `when`(mockNotesRepository.getFirstLevelNotesFlow())
            .thenReturn(flow<List<Note>> { listOf(note) }.onStart { throw NullPointerException() })

        createViewModel()

        viewModel.uiState.test {
            assertEquals(
                ElementsOverviewUiState(status = ElementsOverviewStatus.FAILURE), awaitItem()
            )
            expectNoEvents()
        }
    }

    @Test
    fun toggleView() = runTest {

        viewModel.uiState.test {
            assertEquals(
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = listOf(folder.toElement(), note.toElement())
                ),
                awaitItem()
            )

            viewModel.toggleView()
            assertEquals(
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = listOf(folder.toElement(), note.toElement()),
                    isGridView = true
                ),
                awaitItem()
            )

            viewModel.toggleView()
            assertEquals(
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = listOf(folder.toElement(), note.toElement()),
                    isGridView = false
                ),
                awaitItem()
            )
            expectNoEvents()
        }
    }

    @Test
    fun onSortOptionSelected() = runTest {
        val dateSelectedOption = SortInfo().sortRadioOptions.first()
        val nameSelectedOption = SortInfo().sortRadioOptions.last()

        viewModel.uiState.test {
            assertEquals(
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = listOf(folder.toElement(), note.toElement()),
                ), awaitItem()
            )

            viewModel.onSortOptionSelected(nameSelectedOption)
            assertEquals(
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = listOf(folder.toElement(), note.toElement()),
                    sortInfo = SortInfo().copy(sortSelectedOption = nameSelectedOption)
                ), awaitItem()
            )

            viewModel.onSortOptionSelected(dateSelectedOption)
            assertEquals(
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = listOf(folder.toElement(), note.toElement()),
                    sortInfo = SortInfo().copy(sortSelectedOption = dateSelectedOption)
                ), awaitItem()
            )
            expectNoEvents()
        }
    }

    @Test
    fun onOrderOptionSelected() = runTest {
        val ascendingSelectedOption = SortInfo().orderRadioOptions.first()
        val descendingSelectedOption = SortInfo().orderRadioOptions.last()
        val nameSelectedOption = SortInfo().sortRadioOptions.last()

        viewModel.uiState.test {
            assertEquals(
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = listOf(folder.toElement(), note.toElement()),
                ), awaitItem()
            )

            viewModel.onOrderOptionSelected(descendingSelectedOption)
            assertEquals(
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = listOf(folder.toElement(), note.toElement()),
                    sortInfo = SortInfo().copy(orderSelectedOption = descendingSelectedOption)
                ), awaitItem()
            )
            assertEquals(
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = listOf(note.toElement(), folder.toElement()),
                    sortInfo = SortInfo().copy(orderSelectedOption = descendingSelectedOption)
                ), awaitItem()
            )

            viewModel.onOrderOptionSelected(ascendingSelectedOption)
            assertEquals(
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = listOf(note.toElement(), folder.toElement()),
                    sortInfo = SortInfo().copy(orderSelectedOption = ascendingSelectedOption)
                ), awaitItem()
            )
            assertEquals(
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = listOf(folder.toElement(), note.toElement()),
                    sortInfo = SortInfo().copy(orderSelectedOption = ascendingSelectedOption)
                ), awaitItem()
            )

            viewModel.onSortOptionSelected(nameSelectedOption)
            assertEquals(
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = listOf(folder.toElement(), note.toElement()),
                    sortInfo = SortInfo().copy(sortSelectedOption = nameSelectedOption)
                ), awaitItem()
            )

            viewModel.onOrderOptionSelected(descendingSelectedOption)
            assertEquals(
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = listOf(folder.toElement(), note.toElement()),
                    sortInfo = SortInfo().copy(
                        sortSelectedOption = nameSelectedOption,
                        orderSelectedOption = descendingSelectedOption
                    )
                ), awaitItem()
            )
            assertEquals(
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = listOf(note.toElement(), folder.toElement()),
                    sortInfo = SortInfo().copy(
                        sortSelectedOption = nameSelectedOption,
                        orderSelectedOption = descendingSelectedOption
                    )
                ), awaitItem()
            )

            expectNoEvents()
        }
    }

    @Test
    fun onFirstElementsOptionSelected() = runTest {
        val notApplicableSelectedOption = SortInfo().firstElementsRadioOptions[0]
        val foldersSelectedOption = SortInfo().firstElementsRadioOptions[1]
        val notesSelectedOption = SortInfo().firstElementsRadioOptions[2]

        viewModel.uiState.test {
            assertEquals(
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = listOf(folder.toElement(), note.toElement()),
                ), awaitItem()
            )

            viewModel.onFirstElementsOptionSelected(foldersSelectedOption)
            assertEquals(
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = listOf(folder.toElement(), note.toElement()),
                    sortInfo = SortInfo().copy(firstElementsSelectedOption = foldersSelectedOption)
                ), awaitItem()
            )

            viewModel.onFirstElementsOptionSelected(notesSelectedOption)
            assertEquals(
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = listOf(folder.toElement(), note.toElement()),
                    sortInfo = SortInfo().copy(firstElementsSelectedOption = notesSelectedOption)
                ), awaitItem()
            )
            assertEquals(
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = listOf(note.toElement(), folder.toElement()),
                    sortInfo = SortInfo().copy(firstElementsSelectedOption = notesSelectedOption)
                ), awaitItem()
            )

            viewModel.onFirstElementsOptionSelected(notApplicableSelectedOption)
            assertEquals(
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = listOf(note.toElement(), folder.toElement()),
                    sortInfo = SortInfo().copy(firstElementsSelectedOption = notApplicableSelectedOption)
                ), awaitItem()
            )
            assertEquals(
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = listOf(folder.toElement(), note.toElement()),
                    sortInfo = SortInfo().copy(firstElementsSelectedOption = notApplicableSelectedOption)
                ), awaitItem()
            )

            expectNoEvents()
        }
    }

    @Test
    fun deleteElement_foldersRepository_success() = runTest {

        viewModel.uiState.test {
            assertEquals(
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = listOf(folder.toElement(), note.toElement()),
                ), awaitItem()
            )
            viewModel.deleteElement(folder.toElement())
            expectNoEvents()
        }
        verify(mockFoldersRepository).deleteFolder(1)
    }

    @Test
    fun deleteElement_foldersRepository_failure() = runTest {
        `when`(mockFoldersRepository.deleteFolder(anyInt())).thenThrow(NullPointerException())

        viewModel.uiState.test {
            assertEquals(
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = listOf(folder.toElement(), note.toElement()),
                ), awaitItem()
            )
            viewModel.deleteElement(folder.toElement())
            assertEquals(
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = listOf(folder.toElement(), note.toElement()),
                    isDeleteFailure = true
                ), awaitItem()
            )
            expectNoEvents()
        }
        verify(mockFoldersRepository).deleteFolder(1)
    }

    @Test
    fun deleteElement_notesRepository_success() = runTest {

        viewModel.uiState.test {
            assertEquals(
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = listOf(folder.toElement(), note.toElement()),
                ), awaitItem()
            )
            viewModel.deleteElement(note.toElement())
            expectNoEvents()
        }
        verify(mockNotesRepository).deleteNote(1)
    }

    @Test
    fun deleteElement_notesRepository_failure() = runTest {
        `when`(mockNotesRepository.deleteNote(anyInt())).thenThrow(NullPointerException())

        viewModel.uiState.test {
            assertEquals(
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = listOf(folder.toElement(), note.toElement()),
                ), awaitItem()
            )
            viewModel.deleteElement(note.toElement())
            assertEquals(
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = listOf(folder.toElement(), note.toElement()),
                    isDeleteFailure = true
                ), awaitItem()
            )
            expectNoEvents()
        }
        verify(mockNotesRepository).deleteNote(1)
    }

    @Test
    fun deleteElement_nullId_failure() = runTest {

        viewModel.uiState.test {
            assertEquals(
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = listOf(folder.toElement(), note.toElement()),
                ), awaitItem()
            )
            viewModel.deleteElement(Element.empty())
            assertEquals(
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = listOf(folder.toElement(), note.toElement()),
                    isDeleteFailure = true
                ), awaitItem()
            )
            expectNoEvents()
        }
    }

    @Test
    fun snackbarMessageShown() = runTest {
        `when`(mockNotesRepository.deleteNote(anyInt())).thenThrow(NullPointerException())

        viewModel.uiState.test {
            assertEquals(
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = listOf(folder.toElement(), note.toElement()),
                ), awaitItem()
            )
            viewModel.deleteElement(note.toElement())
            assertEquals(
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = listOf(folder.toElement(), note.toElement()),
                    isDeleteFailure = true
                ), awaitItem()
            )
            viewModel.snackbarMessageShown()
            assertEquals(
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = listOf(folder.toElement(), note.toElement()),
                    isDeleteFailure = false
                ), awaitItem()
            )
            expectNoEvents()
        }
    }
}














