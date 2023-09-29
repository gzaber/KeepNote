package com.gzaber.keepnote.ui.notedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gzaber.keepnote.data.repository.NotesRepository
import com.gzaber.keepnote.data.repository.model.Note
import com.gzaber.keepnote.ui.navigation.KeepNoteDestinationArgs
import com.gzaber.keepnote.ui.utils.model.Element
import com.gzaber.keepnote.ui.utils.model.toNote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class NoteDetailsStatus {
    LOADING, SUCCESS, FAILURE
}

data class NoteDetailsUiState(
    val status: NoteDetailsStatus = NoteDetailsStatus.LOADING,
    val note: Note = Element.empty().toNote()
)

@HiltViewModel
class NoteDetailsViewModel @Inject constructor(
    private val notesRepository: NotesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val noteId: String? = savedStateHandle[KeepNoteDestinationArgs.NOTE_ID_ARG]

    private val _uiState = MutableStateFlow(NoteDetailsUiState())
    val uiState: StateFlow<NoteDetailsUiState> = _uiState.asStateFlow()

    init {
        if (noteId != null) {
            readNote(noteId.toInt())
        } else {
            _uiState.update {
                it.copy(status = NoteDetailsStatus.FAILURE)
            }
        }
    }

    private fun readNote(noteId: Int) {
        _uiState.update {
            it.copy(status = NoteDetailsStatus.LOADING)
        }

        viewModelScope.launch {
            notesRepository.getNoteByIdFlow(noteId)
                .catch {
                    _uiState.update {
                        it.copy(status = NoteDetailsStatus.FAILURE)
                    }
                }
                .first().let { note ->
                    _uiState.update {
                        it.copy(
                            status = NoteDetailsStatus.SUCCESS,
                            note = note
                        )
                    }
                }
        }
    }
}













