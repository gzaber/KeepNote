package com.gzaber.keepnote.ui.folderdetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gzaber.keepnote.data.repository.FoldersRepository
import com.gzaber.keepnote.data.repository.NotesRepository
import com.gzaber.keepnote.data.repository.model.Folder
import com.gzaber.keepnote.data.repository.model.Note
import com.gzaber.keepnote.ui.navigation.KeepNoteDestinationArgs
import com.gzaber.keepnote.ui.utils.model.Element
import com.gzaber.keepnote.ui.utils.model.toFolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class FolderDetailsStatus {
    LOADING, SUCCESS, FAILURE
}

data class FolderDetailsUiState(
    val status: FolderDetailsStatus = FolderDetailsStatus.LOADING,
    val folder: Folder = Element.empty().toFolder(),
    val notes: List<Note> = listOf(),
    val isGridView: Boolean = false
)

@HiltViewModel
class FolderDetailsViewModel @Inject constructor(
    private val foldersRepository: FoldersRepository,
    private val notesRepository: NotesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val folderId: String? = savedStateHandle[KeepNoteDestinationArgs.FOLDER_ID_ARG]
    private val _isGridView: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val _uiState = if (folderId != null) {
        combine(
            foldersRepository.getFolderByIdFlow(folderId = folderId.toInt()),
            notesRepository.getSecondLevelNotesFlow(folderId = folderId.toInt()),
            _isGridView
        ) { folder, notes, isGridView ->
            FolderDetailsUiState(
                status = FolderDetailsStatus.SUCCESS,
                isGridView = isGridView,
                folder = folder,
                notes = notes
            )
        }.catch {
            FolderDetailsUiState(status = FolderDetailsStatus.FAILURE)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = FolderDetailsUiState()
            )
    } else {
        MutableStateFlow(
            FolderDetailsUiState(status = FolderDetailsStatus.FAILURE)
        )
    }


    val uiState: StateFlow<FolderDetailsUiState> = _uiState

    fun toggleView() {
        _isGridView.value = _isGridView.value.not()
    }

}