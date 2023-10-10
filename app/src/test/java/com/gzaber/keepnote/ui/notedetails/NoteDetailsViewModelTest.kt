package com.gzaber.keepnote.ui.notedetails

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.gzaber.keepnote.data.repository.NotesRepository
import com.gzaber.keepnote.data.repository.model.Note
import com.gzaber.keepnote.ui.navigation.KeepNoteDestinationArgs
import com.gzaber.keepnote.util.MainDispatcherRule
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import java.util.Date

@RunWith(MockitoJUnitRunner::class)
class NoteDetailsViewModelTest {

    private lateinit var viewModel: NoteDetailsViewModel
    private val note = Note(
        id = 1,
        folderId = null,
        title = "title",
        content = "content",
        color = 111111,
        date = Date()
    )

    @Mock
    private lateinit var mockNotesRepository: NotesRepository

    @get:Rule
    val rule = MainDispatcherRule()

    @Before
    fun setUp() {
        `when`(mockNotesRepository.getNoteByIdFlow(ArgumentMatchers.anyInt())).thenReturn(
            flow { emit(note) }
        )
        viewModel = createViewModelWithSavedStateHandle("1")
    }

    private fun createViewModelWithSavedStateHandle(
        noteIdArg: String? = null
    ) = NoteDetailsViewModel(
        notesRepository = mockNotesRepository,
        savedStateHandle = SavedStateHandle(
            mapOf(
                KeepNoteDestinationArgs.NOTE_ID_ARG to noteIdArg
            )
        )
    )

    @Test
    fun initialState_noteIdIsNotNull_successStatus() = runTest {

        viewModel.uiState.test {
            assertEquals(
                NoteDetailsUiState(
                    status = NoteDetailsStatus.SUCCESS,
                    note = note
                ), awaitItem()
            )
            expectNoEvents()
        }
        verify(mockNotesRepository).getNoteByIdFlow(1)
    }

    @Test
    fun initialState_noteIdIsNull_failureStatus() = runTest {
        viewModel = createViewModelWithSavedStateHandle(null)

        viewModel.uiState.test {
            assertEquals(NoteDetailsStatus.FAILURE, awaitItem().status)
            expectNoEvents()
        }
    }

    @Test
    fun initialState_noteIdIsNotNull_notesRepositoryFailure_failureStatus() = runTest {
        `when`(mockNotesRepository.getNoteByIdFlow(anyInt())).thenReturn(
            flow { emit(note) }.onStart { throw NullPointerException() }
        )

        viewModel = createViewModelWithSavedStateHandle("1")

        viewModel.uiState.test {
            assertEquals(NoteDetailsStatus.FAILURE, awaitItem().status)
            expectNoEvents()
        }
    }
}