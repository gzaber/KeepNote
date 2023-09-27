package com.gzaber.keepnote.ui.elementsoverview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gzaber.keepnote.R
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
import kotlinx.coroutines.flow.update
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

data class FilterInfo(
    val sortRadioOptions: List<Int> = listOf(R.string.radio_name, R.string.radio_date),
    val sortSelectedOption: Int = sortRadioOptions.first(),
    val orderRadioOptions: List<Int> = listOf(R.string.radio_ascending, R.string.radio_descending),
    val orderSelectedOption: Int = orderRadioOptions.first(),
    val firstElementsRadioOptions: List<Int> = listOf(
        R.string.radio_not_applicable,
        R.string.radio_folders,
        R.string.radio_notes
    ),
    val firstElementsSelectedOption: Int = firstElementsRadioOptions.first(),

    )

data class ElementsOverviewUiState(
    val status: ElementsOverviewStatus = ElementsOverviewStatus.LOADING,
    val elements: List<Element> = listOf(),
    val isGridView: Boolean = false,
    val filterInfo: FilterInfo = FilterInfo()
)

@HiltViewModel
class ElementsOverviewViewModel @Inject constructor(
    private val foldersRepository: FoldersRepository,
    private val notesRepository: NotesRepository
) : ViewModel() {

    private val _isGridView: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _filterInfo: MutableStateFlow<FilterInfo> = MutableStateFlow(FilterInfo())

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

    private val _uiState = combine(
        _elementsResult,
        _isGridView,
        _filterInfo
    ) { elementsResult, isGridView, filterInfo ->
        when (elementsResult) {
            is ElementsFlowResult.Success -> {
                val sortedElements = sortElements(elementsResult.elements, filterInfo)
                ElementsOverviewUiState(
                    status = ElementsOverviewStatus.SUCCESS,
                    elements = sortedElements,
                    isGridView = isGridView,
                    filterInfo = filterInfo
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

    fun onSortOptionSelected(optionSelected: Int) {
        _filterInfo.update {
            it.copy(sortSelectedOption = optionSelected)
        }
    }

    fun onOrderOptionSelected(optionSelected: Int) {
        _filterInfo.update {
            it.copy(orderSelectedOption = optionSelected)
        }
    }

    fun onFirstElementsOptionSelected(optionSelected: Int) {
        _filterInfo.update {
            it.copy(firstElementsSelectedOption = optionSelected)
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

    private fun sortElements(elements: List<Element>, filterInfo: FilterInfo): List<Element> {
        return when (filterInfo.firstElementsSelectedOption) {

            filterInfo.firstElementsRadioOptions.first() -> basicSortElements(elements, filterInfo)

            else -> {
                val folderElements = elements.filter { it.isNote.not() }
                val noteElements = elements.filter { it.isNote }
                if (filterInfo.firstElementsSelectedOption == filterInfo.firstElementsRadioOptions[1]) {
                    basicSortElements(folderElements, filterInfo) +
                            basicSortElements(noteElements, filterInfo)
                } else {
                    basicSortElements(noteElements, filterInfo) + basicSortElements(
                        folderElements,
                        filterInfo
                    )
                }
            }
        }
    }

    private fun basicSortElements(elements: List<Element>, filterInfo: FilterInfo): List<Element> {
        return when (filterInfo.orderSelectedOption) {
            filterInfo.orderRadioOptions.first() -> {
                when (filterInfo.sortSelectedOption) {
                    filterInfo.sortRadioOptions.first() -> elements.sortedBy { it.name }
                    else -> elements.sortedBy { it.date }
                }
            }

            else -> {
                when (filterInfo.sortSelectedOption) {
                    filterInfo.sortRadioOptions.first() -> elements.sortedByDescending { it.name }
                    else -> elements.sortedByDescending { it.date }
                }
            }
        }
    }
}
