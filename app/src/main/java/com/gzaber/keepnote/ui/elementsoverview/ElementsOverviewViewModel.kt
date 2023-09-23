package com.gzaber.keepnote.ui.elementsoverview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gzaber.keepnote.data.repository.FoldersRepository
import com.gzaber.keepnote.data.repository.NotesRepository
import com.gzaber.keepnote.data.repository.model.Folder
import com.gzaber.keepnote.data.repository.model.Note
import com.gzaber.keepnote.ui.utils.model.Element
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class ElementsFlowResult {
    data class Success(val elements: List<Element>) : ElementsFlowResult()
    object Error : ElementsFlowResult()
    object Loading : ElementsFlowResult()
}

enum class ElementsOverviewStatus {
    LOADING, SUCCESS, FAILURE
}

data class ElementsOverviewUiState(
    val status: ElementsOverviewStatus = ElementsOverviewStatus.LOADING,
    val elements: List<Element> = listOf(),
    val isGridView: Boolean = false
)

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
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = elementsResult.elements,
                    isGridView = isGridView
                )
            }

            ElementsFlowResult.Error -> {
                ElementsOverviewUiState(status = ElementsOverviewStatus.FAILURE)
            }

            ElementsFlowResult.Loading -> {
                ElementsOverviewUiState(status = ElementsOverviewStatus.LOADING)
            }
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ElementsOverviewUiState()
        )

    val uiState: StateFlow<ElementsOverviewUiState> = _uiState

    fun toggleView() {
        _isGridView.value = !_isGridView.value
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
