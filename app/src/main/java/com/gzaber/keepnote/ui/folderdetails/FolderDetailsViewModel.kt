package com.gzaber.keepnote.ui.folderdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gzaber.keepnote.R
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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class FolderDetailsStatus {
    LOADING, SUCCESS, FAILURE
}

data class SortInfo(
    val sortRadioOptions: List<Int> = listOf(R.string.radio_date, R.string.radio_name),
    val sortSelectedOption: Int = sortRadioOptions.first(),
    val orderRadioOptions: List<Int> = listOf(R.string.radio_ascending, R.string.radio_descending),
    val orderSelectedOption: Int = orderRadioOptions.first()
)

data class FolderDetailsUiState(
    val status: FolderDetailsStatus = FolderDetailsStatus.LOADING,
    val folder: Folder = Element.empty().toFolder(),
    val notes: List<Note> = listOf(),
    val isGridView: Boolean = false,
    val isDeleteFailure: Boolean = false,
    val sortInfo: SortInfo = SortInfo()
)

@HiltViewModel
class FolderDetailsViewModel @Inject constructor(
    private val foldersRepository: FoldersRepository,
    private val notesRepository: NotesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _folderId: String? = savedStateHandle[KeepNoteDestinationArgs.FOLDER_ID_ARG]
    private val _isGridView: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _isDeleteFailure: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _isRepositoryFailure: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _sortInfo: MutableStateFlow<SortInfo> = MutableStateFlow(SortInfo())


    val uiState = if (_folderId != null) {
        val folder: StateFlow<Folder> =
            foldersRepository.getFolderByIdFlow(folderId = _folderId.toInt())
                .catch { _isRepositoryFailure.value = true }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = Element.empty().toFolder()
                )
        val notes: StateFlow<List<Note>> =
            notesRepository.getSecondLevelNotesFlow(folderId = _folderId.toInt())
                .catch { _isRepositoryFailure.value = true }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = listOf()
                )
        combine(
            folder,
            notes,
            _isGridView,
            _isDeleteFailure,
            _sortInfo
        ) { folder, notes, isGridView, isDeleteFailure, sortInfo ->
            val sortedNotes = sortNotes(notes, sortInfo)
            FolderDetailsUiState(
                status = if (_isRepositoryFailure.value) FolderDetailsStatus.FAILURE else FolderDetailsStatus.SUCCESS,
                folder = folder,
                notes = sortedNotes,
                isGridView = isGridView,
                isDeleteFailure = isDeleteFailure,
                sortInfo = sortInfo
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = FolderDetailsUiState()
            )
    } else {
        MutableStateFlow(FolderDetailsUiState(status = FolderDetailsStatus.FAILURE))
    }

    fun toggleView() {
        _isGridView.value = _isGridView.value.not()
    }

    fun onSortOptionSelected(optionSelected: Int) {
        _sortInfo.update {
            it.copy(sortSelectedOption = optionSelected)
        }
    }

    fun onOrderOptionSelected(optionSelected: Int) {
        _sortInfo.update {
            it.copy(orderSelectedOption = optionSelected)
        }
    }

    fun deleteNote(noteId: Int) {
        viewModelScope.launch {
            try {
                notesRepository.deleteNote(noteId)
            } catch (e: Throwable) {
                _isDeleteFailure.value = true
            }
        }
    }

    fun snackbarMessageShown() {
        _isDeleteFailure.value = false
    }

    private fun sortNotes(notes: List<Note>, sortInfo: SortInfo): List<Note> {
        return when (sortInfo.orderSelectedOption) {
            sortInfo.orderRadioOptions.first() -> {
                when (sortInfo.sortSelectedOption) {
                    sortInfo.sortRadioOptions.first() -> notes.sortedBy { it.date }
                    else -> notes.sortedBy { it.title }
                }
            }

            else -> {
                when (sortInfo.sortSelectedOption) {
                    sortInfo.sortRadioOptions.first() -> notes.sortedByDescending { it.date }
                    else -> notes.sortedByDescending { it.title }
                }
            }
        }
    }


}