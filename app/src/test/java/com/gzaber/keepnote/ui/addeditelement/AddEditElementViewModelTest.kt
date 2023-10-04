package com.gzaber.keepnote.ui.addeditelement

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import com.gzaber.keepnote.data.repository.FoldersRepository
import com.gzaber.keepnote.data.repository.NotesRepository
import com.gzaber.keepnote.data.repository.model.Folder
import com.gzaber.keepnote.data.repository.model.Note
import com.gzaber.keepnote.ui.navigation.KeepNoteDestinationArgs
import com.gzaber.keepnote.ui.utils.model.Element
import com.gzaber.keepnote.ui.utils.model.toFolder
import com.gzaber.keepnote.ui.utils.model.toNote
import com.gzaber.keepnote.util.MainDispatcherRule
import kotlinx.coroutines.flow.flow
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
import org.mockito.kotlin.any
import java.util.Date

@RunWith(MockitoJUnitRunner::class)
class AddEditElementViewModelTest {

    private lateinit var viewModel: AddEditElementViewModel
    private val tmpDate = Date()
    private val folder = Folder(id = 1, name = "folder", color = 111111, date = tmpDate)
    private val note = Note(
        id = 1,
        folderId = null,
        title = "title",
        content = "content",
        color = 111111,
        date = tmpDate
    )

    @Mock
    private lateinit var mockFoldersRepository: FoldersRepository

    @Mock
    private lateinit var mockNotesRepository: NotesRepository

    @get:Rule
    val rule = MainDispatcherRule()

    @Before
    fun setUp() {
        viewModel = AddEditElementViewModel(
            foldersRepository = mockFoldersRepository,
            notesRepository = mockNotesRepository,
            savedStateHandle = SavedStateHandle()
        )
    }

    private fun updateViewModelWithSavedStateHandle(
        isNoteArg: Boolean = true,
        elementIdArg: String? = null,
        folderIdArg: String? = null
    ) {
        viewModel = AddEditElementViewModel(
            foldersRepository = mockFoldersRepository,
            notesRepository = mockNotesRepository,
            savedStateHandle = SavedStateHandle(
                mapOf(
                    KeepNoteDestinationArgs.IS_NOTE_ARG to isNoteArg,
                    KeepNoteDestinationArgs.ELEMENT_ID_ARG to elementIdArg,
                    KeepNoteDestinationArgs.FOLDER_ID_ARG to folderIdArg
                )
            )
        )
    }


    @Test
    fun init_createFolderMode() {
        updateViewModelWithSavedStateHandle(false, null, null)

        assertEquals(AddEditElementStatus.SUCCESS, viewModel.uiState.value.status)
        assertEquals(AddEditElementMode.CREATE_FOLDER, viewModel.uiState.value.mode)
        assertEquals(
            Element.empty().copy(isNote = false, date = tmpDate),
            viewModel.uiState.value.element.copy(date = tmpDate)
        )
    }

    @Test
    fun init_createNoteMode() {
        updateViewModelWithSavedStateHandle(true, null, null)

        assertEquals(AddEditElementStatus.SUCCESS, viewModel.uiState.value.status)
        assertEquals(AddEditElementMode.CREATE_NOTE, viewModel.uiState.value.mode)
        assertEquals(
            Element.empty().copy(isNote = true, date = tmpDate),
            viewModel.uiState.value.element.copy(date = tmpDate)
        )
    }

    @Test
    fun init_createChildNoteMode() {
        updateViewModelWithSavedStateHandle(true, null, "1")

        assertEquals(AddEditElementStatus.SUCCESS, viewModel.uiState.value.status)
        assertEquals(AddEditElementMode.CREATE_CHILD_NOTE, viewModel.uiState.value.mode)
        assertEquals(
            Element.empty().copy(isNote = true, folderId = 1, date = tmpDate),
            viewModel.uiState.value.element.copy(folderId = 1, date = tmpDate)
        )
    }

    @Test
    fun init_updateFolderMode_successStatus() = runTest {
        `when`(mockFoldersRepository.getFolderByIdFlow(anyInt()))
            .thenReturn(flow { emit(folder) })

        updateViewModelWithSavedStateHandle(false, "1", null)

        assertEquals(AddEditElementStatus.SUCCESS, viewModel.uiState.value.status)
        assertEquals(AddEditElementMode.UPDATE_FOLDER, viewModel.uiState.value.mode)
        assertEquals(folder, viewModel.uiState.value.element.toFolder())
        verify(mockFoldersRepository).getFolderByIdFlow(1)
    }

    @Test
    fun init_updateFolderMode_failureStatus() = runTest {
        `when`(mockFoldersRepository.getFolderByIdFlow(anyInt()))
            .thenThrow(NullPointerException())

        updateViewModelWithSavedStateHandle(false, "1", null)

        assertEquals(AddEditElementStatus.FAILURE, viewModel.uiState.value.status)
        assertEquals(AddEditElementMode.UPDATE_FOLDER, viewModel.uiState.value.mode)
        assertEquals(
            Element.empty().copy(isNote = false, date = tmpDate),
            viewModel.uiState.value.element.copy(date = tmpDate)
        )
        verify(mockFoldersRepository).getFolderByIdFlow(1)
    }

    @Test
    fun init_updateNoteMode_successStatus() = runTest {
        `when`(mockNotesRepository.getNoteByIdFlow(anyInt()))
            .thenReturn(flow { emit(note) })

        updateViewModelWithSavedStateHandle(true, "1", null)

        assertEquals(AddEditElementStatus.SUCCESS, viewModel.uiState.value.status)
        assertEquals(AddEditElementMode.UPDATE_NOTE, viewModel.uiState.value.mode)
        assertEquals(note, viewModel.uiState.value.element.toNote())
    }

    @Test
    fun init_updateNoteMode_failureStatus() = runTest {
        `when`(mockNotesRepository.getNoteByIdFlow(anyInt()))
            .thenThrow(NullPointerException())

        updateViewModelWithSavedStateHandle(true, "1", null)

        assertEquals(AddEditElementStatus.FAILURE, viewModel.uiState.value.status)
        assertEquals(AddEditElementMode.UPDATE_NOTE, viewModel.uiState.value.mode)
        assertEquals(
            Element.empty().copy(date = tmpDate),
            viewModel.uiState.value.element.copy(date = tmpDate)
        )
        verify(mockNotesRepository).getNoteByIdFlow(1)
    }

    @Test
    fun onTitleChanged() {
        val title = "NewTitle"

        viewModel.onTitleChanged(title)

        assertEquals(title, viewModel.uiState.value.element.name)
    }

    @Test
    fun onContentChanged() {
        val content = "NewContent"

        viewModel.onContentChanged(content)

        assertEquals(content, viewModel.uiState.value.element.content)
    }

    @Test
    fun onColorChanged() {
        val color = Color(Color.Yellow.toArgb())

        viewModel.onColorChanged(color)

        assertEquals(color.toArgb(), viewModel.uiState.value.element.color)
    }

    @Test
    fun saveElement_createChildNoteMode_saveSuccessStatus() = runTest {
        updateViewModelWithSavedStateHandle(true, null, "1")

        viewModel.saveElement()

        assertEquals(AddEditElementStatus.SAVE_SUCCESS, viewModel.uiState.value.status)
        assertEquals(AddEditElementMode.CREATE_CHILD_NOTE, viewModel.uiState.value.mode)
        verify(mockNotesRepository).createNote(
            Element.empty().copy(
                folderId = 1,
                date = viewModel.uiState.value.element.date
            ).toNote()
        )
    }

    @Test
    fun saveElement_createChildNoteMode_failureStatus() = runTest {
        `when`(mockNotesRepository.createNote(any())).thenThrow(NullPointerException())
        updateViewModelWithSavedStateHandle(true, null, "1")

        viewModel.saveElement()

        assertEquals(AddEditElementStatus.FAILURE, viewModel.uiState.value.status)
        assertEquals(AddEditElementMode.CREATE_CHILD_NOTE, viewModel.uiState.value.mode)
        verify(mockNotesRepository).createNote(
            Element.empty().copy(
                folderId = 1,
                date = viewModel.uiState.value.element.date
            ).toNote()
        )
    }

    @Test
    fun saveElement_createNoteMode_saveSuccessStatus() = runTest {
        updateViewModelWithSavedStateHandle(true, null, null)

        viewModel.saveElement()

        assertEquals(AddEditElementStatus.SAVE_SUCCESS, viewModel.uiState.value.status)
        assertEquals(AddEditElementMode.CREATE_NOTE, viewModel.uiState.value.mode)
        verify(mockNotesRepository).createNote(
            Element.empty().copy(date = viewModel.uiState.value.element.date).toNote()
        )
    }

    @Test
    fun saveElement_createNoteMode_failureStatus() = runTest {
        `when`(mockNotesRepository.createNote(any())).thenThrow(NullPointerException())
        updateViewModelWithSavedStateHandle(true, null, null)

        viewModel.saveElement()

        assertEquals(AddEditElementStatus.FAILURE, viewModel.uiState.value.status)
        assertEquals(AddEditElementMode.CREATE_NOTE, viewModel.uiState.value.mode)
        verify(mockNotesRepository).createNote(
            Element.empty().copy(date = viewModel.uiState.value.element.date).toNote()
        )
    }

    @Test
    fun saveElement_createFolderMode_saveSuccessStatus() = runTest {
        updateViewModelWithSavedStateHandle(false, null, null)

        viewModel.saveElement()

        assertEquals(AddEditElementStatus.SAVE_SUCCESS, viewModel.uiState.value.status)
        assertEquals(AddEditElementMode.CREATE_FOLDER, viewModel.uiState.value.mode)
        verify(mockFoldersRepository).createFolder(
            Element.empty().copy(date = viewModel.uiState.value.element.date).toFolder()
        )
    }

    @Test
    fun saveElement_createFolderMode_failureStatus() = runTest {
        `when`(mockFoldersRepository.createFolder(any())).thenThrow(NullPointerException())
        updateViewModelWithSavedStateHandle(false, null, null)

        viewModel.saveElement()

        assertEquals(AddEditElementStatus.FAILURE, viewModel.uiState.value.status)
        assertEquals(AddEditElementMode.CREATE_FOLDER, viewModel.uiState.value.mode)
        verify(mockFoldersRepository).createFolder(
            Element.empty().copy(date = viewModel.uiState.value.element.date).toFolder()
        )
    }

    @Test
    fun saveElement_updateNoteMode_saveSuccessStatus() = runTest {
        updateViewModelWithSavedStateHandle(true, "1", null)

        viewModel.saveElement()

        assertEquals(AddEditElementStatus.SAVE_SUCCESS, viewModel.uiState.value.status)
        assertEquals(AddEditElementMode.UPDATE_NOTE, viewModel.uiState.value.mode)
        verify(mockNotesRepository).updateNote(
            Element.empty().copy(date = viewModel.uiState.value.element.date).toNote()
        )
    }

    @Test
    fun saveElement_updateNoteMode_failureStatus() = runTest {
        `when`(mockNotesRepository.updateNote(any())).thenThrow(NullPointerException())
        updateViewModelWithSavedStateHandle(true, "1", null)

        viewModel.saveElement()

        assertEquals(AddEditElementStatus.FAILURE, viewModel.uiState.value.status)
        assertEquals(AddEditElementMode.UPDATE_NOTE, viewModel.uiState.value.mode)
        verify(mockNotesRepository).updateNote(
            Element.empty().copy(date = viewModel.uiState.value.element.date).toNote()
        )
    }

    @Test
    fun saveElement_updateFolderMode_saveSuccessStatus() = runTest {
        updateViewModelWithSavedStateHandle(false, "1", null)

        viewModel.saveElement()

        assertEquals(AddEditElementStatus.SAVE_SUCCESS, viewModel.uiState.value.status)
        assertEquals(AddEditElementMode.UPDATE_FOLDER, viewModel.uiState.value.mode)
        verify(mockFoldersRepository).updateFolder(
            Element.empty().copy(date = viewModel.uiState.value.element.date).toFolder()
        )
    }

    @Test
    fun saveElement_updateFolderMode_failureStatus() = runTest {
        `when`(mockFoldersRepository.updateFolder(any())).thenThrow(NullPointerException())
        updateViewModelWithSavedStateHandle(false, "1", null)

        viewModel.saveElement()

        assertEquals(AddEditElementStatus.FAILURE, viewModel.uiState.value.status)
        assertEquals(AddEditElementMode.UPDATE_FOLDER, viewModel.uiState.value.mode)
        verify(mockFoldersRepository).updateFolder(
            Element.empty().copy(date = viewModel.uiState.value.element.date).toFolder()
        )
    }
}