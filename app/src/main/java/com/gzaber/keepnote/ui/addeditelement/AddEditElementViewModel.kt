package com.gzaber.keepnote.ui.addeditelement

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gzaber.keepnote.data.repository.FoldersRepository
import com.gzaber.keepnote.data.repository.NotesRepository
import com.gzaber.keepnote.data.repository.model.Folder
import com.gzaber.keepnote.data.repository.model.Note
import com.gzaber.keepnote.ui.elementsoverview.Element
import com.gzaber.keepnote.ui.elementsoverview.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddEditElementUiState(
    val status: Status = Status.SUCCESS,
    val mode: AddEditMode = AddEditMode.CREATE_NOTE,
    val element: Element = Element.empty()
)

@HiltViewModel
class AddEditElementViewModel @Inject constructor(
    private val foldersRepository: FoldersRepository,
    private val notesRepository: NotesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val elementId: Int? = savedStateHandle[""] ?: 10
    private val isNote: Boolean = savedStateHandle[""] ?: false

    private val _uiState = MutableStateFlow(AddEditElementUiState())
    val uiState: StateFlow<AddEditElementUiState> = _uiState.asStateFlow()

    init {
        if (elementId != null) {
            readElement(elementId, isNote)
        }
        _uiState.update {
            it.copy(
                mode = when {
                    elementId == null && isNote -> AddEditMode.CREATE_NOTE
                    elementId == null && !isNote -> AddEditMode.CREATE_FOLDER
                    elementId != null && isNote -> AddEditMode.UPDATE_NOTE
                    else -> AddEditMode.UPDATE_FOLDER
                },
                element = it.element.copy(isNote = isNote)
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
            it.copy(status = Status.LOADING)
        }

        try {
            viewModelScope.launch {
                when (_uiState.value.mode) {
                    AddEditMode.CREATE_NOTE -> notesRepository.createNote(_uiState.value.element.toNote())
                    AddEditMode.CREATE_FOLDER -> foldersRepository.createFolder(_uiState.value.element.toFolder())
                    AddEditMode.UPDATE_NOTE -> notesRepository.updateNote(_uiState.value.element.toNote())
                    AddEditMode.UPDATE_FOLDER -> foldersRepository.updateFolder(_uiState.value.element.toFolder())
                }
                _uiState.update {
                    it.copy(status = Status.SUCCESS)
                }
            }
        } catch (e: Throwable) {
            _uiState.update {
                it.copy(status = Status.FAILURE)
            }
        }
    }

    private fun readElement(elementId: Int, isNote: Boolean) {
        _uiState.update {
            it.copy(status = Status.LOADING)
        }
        try {
            viewModelScope.launch {
                if (isNote) {
                    notesRepository.getNoteByIdFlow(elementId)
                        .catch {
                            _uiState.update {
                                it.copy(status = Status.FAILURE)
                            }
                        }
                        .first().let { note ->
                            _uiState.update {
                                it.copy(
                                    status = Status.SUCCESS,
                                    element = note.toElement()
                                )
                            }
                        }
                } else {
                    foldersRepository.getFolderByIdFlow(elementId)
                        .catch {
                            _uiState.update {
                                it.copy(status = Status.FAILURE)
                            }
                        }
                        .first().let { folder ->
                            _uiState.update {
                                it.copy(
                                    status = Status.SUCCESS,
                                    element = folder.toElement()
                                )
                            }
                        }
                }
            }
        } catch (e: Throwable) {
            _uiState.update {
                it.copy(status = Status.FAILURE)
            }
        }
    }
}

enum class AddEditMode {
    CREATE_NOTE,
    CREATE_FOLDER,
    UPDATE_NOTE,
    UPDATE_FOLDER
}

fun Element.toNote(): Note {
    return Note(
        id = id,
        title = name,
        content = content,
        color = color,
        date = date
    )
}

fun Note.toElement(): Element {
    return Element(
        id = id,
        isNote = true,
        name = title,
        content = content,
        color = color,
        date = date
    )
}

fun Element.toFolder(): Folder {
    return Folder(
        id = id,
        name = name,
        color = color,
        date = date
    )
}

fun Folder.toElement(): Element {
    return Element(
        id = id,
        isNote = true,
        name = name,
        content = "",
        color = color,
        date = date
    )
}