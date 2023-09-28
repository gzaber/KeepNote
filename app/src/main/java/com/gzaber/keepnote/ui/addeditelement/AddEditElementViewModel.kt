package com.gzaber.keepnote.ui.addeditelement

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gzaber.keepnote.data.repository.FoldersRepository
import com.gzaber.keepnote.data.repository.NotesRepository
import com.gzaber.keepnote.ui.navigation.KeepNoteDestinationArgs
import com.gzaber.keepnote.ui.utils.model.Element
import com.gzaber.keepnote.ui.utils.model.toElement
import com.gzaber.keepnote.ui.utils.model.toFolder
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


enum class AddEditElementStatus {
    LOADING, SUCCESS, FAILURE, SAVE_SUCCESS
}

enum class AddEditElementMode {
    CREATE_CHILD_NOTE,
    CREATE_NOTE,
    CREATE_FOLDER,
    UPDATE_NOTE,
    UPDATE_FOLDER
}

data class AddEditElementUiState(
    val status: AddEditElementStatus = AddEditElementStatus.SUCCESS,
    val mode: AddEditElementMode = AddEditElementMode.CREATE_NOTE,
    val element: Element = Element.empty()
)

@HiltViewModel
class AddEditElementViewModel @Inject constructor(
    private val foldersRepository: FoldersRepository,
    private val notesRepository: NotesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val isNote: Boolean = savedStateHandle[KeepNoteDestinationArgs.IS_NOTE_ARG] ?: false
    private val elementId: String? = savedStateHandle[KeepNoteDestinationArgs.ELEMENT_ID_ARG]
    private val folderId: String? = savedStateHandle[KeepNoteDestinationArgs.FOLDER_ID_ARG]

    private val _uiState = MutableStateFlow(AddEditElementUiState())
    val uiState: StateFlow<AddEditElementUiState> = _uiState.asStateFlow()

    init {
        if (elementId != null) {
            readElement(elementId.toInt(), isNote)
        }
        _uiState.update {
            it.copy(
                mode = when {
                    folderId != null -> AddEditElementMode.CREATE_CHILD_NOTE
                    elementId == null && isNote -> AddEditElementMode.CREATE_NOTE
                    elementId == null && !isNote -> AddEditElementMode.CREATE_FOLDER
                    elementId != null && isNote -> AddEditElementMode.UPDATE_NOTE
                    else -> AddEditElementMode.UPDATE_FOLDER
                },
                element = it.element.copy(
                    isNote = isNote,
                    folderId = it.element.folderId ?: folderId?.toInt()
                )
            )
        }
    }

    fun onTitleChanged(title: String) {
        _uiState.update {
            it.copy(
                element = it.element.copy(name = title)
            )
        }
    }

    fun onContentChanged(content: String) {
        _uiState.update {
            it.copy(
                element = it.element.copy(content = content)
            )
        }
    }

    fun onColorChanged(color: Color) {
        _uiState.update {
            it.copy(
                element = it.element.copy(color = color.toArgb())
            )
        }
    }

    fun saveElement() {
        _uiState.update {
            it.copy(status = AddEditElementStatus.LOADING)
        }

        try {
            viewModelScope.launch {
                when (_uiState.value.mode) {
                    AddEditElementMode.CREATE_CHILD_NOTE -> notesRepository.createNote(_uiState.value.element.toNote())
                    AddEditElementMode.CREATE_NOTE -> notesRepository.createNote(_uiState.value.element.toNote())
                    AddEditElementMode.CREATE_FOLDER -> foldersRepository.createFolder(_uiState.value.element.toFolder())
                    AddEditElementMode.UPDATE_NOTE -> notesRepository.updateNote(_uiState.value.element.toNote())
                    AddEditElementMode.UPDATE_FOLDER -> foldersRepository.updateFolder(_uiState.value.element.toFolder())
                }
                _uiState.update {
                    it.copy(status = AddEditElementStatus.SAVE_SUCCESS)
                }
            }
        } catch (e: Throwable) {
            _uiState.update {
                it.copy(status = AddEditElementStatus.FAILURE)
            }
        }
    }

    private fun readElement(elementId: Int, isNote: Boolean) {
        _uiState.update {
            it.copy(status = AddEditElementStatus.LOADING)
        }
        try {
            viewModelScope.launch {
                if (isNote) {
                    notesRepository.getNoteByIdFlow(elementId)
                        .catch {
                            _uiState.update {
                                it.copy(status = AddEditElementStatus.FAILURE)
                            }
                        }
                        .first().let { note ->
                            _uiState.update {
                                it.copy(
                                    status = AddEditElementStatus.SUCCESS,
                                    element = note.toElement()
                                )
                            }
                        }
                } else {
                    foldersRepository.getFolderByIdFlow(elementId)
                        .catch {
                            _uiState.update {
                                it.copy(status = AddEditElementStatus.FAILURE)
                            }
                        }
                        .first().let { folder ->
                            _uiState.update {
                                it.copy(
                                    status = AddEditElementStatus.SUCCESS,
                                    element = folder.toElement()
                                )
                            }
                        }
                }
            }
        } catch (e: Throwable) {
            _uiState.update {
                it.copy(status = AddEditElementStatus.FAILURE)
            }
        }
    }
}


