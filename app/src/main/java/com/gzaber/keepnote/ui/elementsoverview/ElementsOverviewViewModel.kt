package com.gzaber.keepnote.ui.elementsoverview

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gzaber.keepnote.data.repository.FoldersRepository
import com.gzaber.keepnote.data.repository.NotesRepository
import com.gzaber.keepnote.data.repository.model.Folder
import com.gzaber.keepnote.data.repository.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ElementsOverviewViewModel @Inject constructor(
    private val foldersRepository: FoldersRepository,
    private val notesRepository: NotesRepository
) : ViewModel() {

    private val _isGridView: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private var _elementsResult = combine(
        foldersRepository.getAllFoldersFlow(),
        notesRepository.getFirstLevelNotesFlow()
    )
    { folders, notes ->
        ElementsFlowResult.Success(elements = convertToElements(folders, notes))
    }
        .catch {
            ElementsFlowResult.Error
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ElementsFlowResult.Loading
        )

    private val _uiState = combine(_elementsResult, _isGridView) { elementsResult, isGridView ->
        when (elementsResult) {
            is ElementsFlowResult.Success -> {
                UiState(
                    status = Status.SUCCESS,
                    elements = elementsResult.elements,
                    isGridView = isGridView
                )
            }

            ElementsFlowResult.Error -> {
                UiState(status = Status.FAILURE)
            }

            ElementsFlowResult.Loading -> {
                UiState(status = Status.LOADING)
            }
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = UiState()
        )

    val uiState: StateFlow<UiState> = _uiState

    fun toggleView() {
        _isGridView.value = !_isGridView.value
    }

    fun createFolder() {
        viewModelScope.launch {
            foldersRepository.createFolder(
                Folder(
                    name = "Folder name",
                    color = Color.Red.toArgb()
                )
            )
        }
    }

    fun createNote() {
        viewModelScope.launch {
            notesRepository.createNote(
                Note(
                    title = "Note title",
                    content = "Note content",
                    color = Color.Green.toArgb()
                )
            )
        }
    }

    fun deleteElement(element: Element) {
        viewModelScope.launch {
            if (element.isNote) {
                notesRepository.deleteNote(element.id!!)
            } else {
                foldersRepository.deleteFolder(element.id!!)
            }

        }
    }


    private fun convertToElements(folders: List<Folder>, notes: List<Note>): List<Element> {
        val elements = mutableListOf<Element>()
        for (folder in folders) {
            elements.add(
                Element(
                    id = folder.id,
                    isNote = false,
                    name = folder.name,
                    content = "",
                    color = folder.color,
                    date = folder.date
                )
            )
        }
        for (note in notes) {
            elements.add(
                Element(
                    id = note.id,
                    isNote = true,
                    name = note.title,
                    content = note.content,
                    color = note.color,
                    date = note.date
                )
            )
        }

        return elements
    }
}

data class UiState(
    val status: Status = Status.LOADING,
    val elements: List<Element> = listOf(),
    val isGridView: Boolean = false
)

data class Element(
    val id: Int? = null,
    val isNote: Boolean,
    val name: String,
    val content: String,
    val color: Int,
    val date: Date = Date()
)

sealed class ElementsFlowResult {
    data class Success(val elements: List<Element>) : ElementsFlowResult()
    object Error : ElementsFlowResult()
    object Loading : ElementsFlowResult()
}

enum class Status {
    LOADING, SUCCESS, FAILURE
}