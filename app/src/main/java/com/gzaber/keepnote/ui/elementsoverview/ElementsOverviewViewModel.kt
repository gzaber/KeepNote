package com.gzaber.keepnote.ui.elementsoverview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gzaber.keepnote.R
import com.gzaber.keepnote.data.repository.FoldersRepository
import com.gzaber.keepnote.data.repository.NotesRepository
import com.gzaber.keepnote.ui.utils.model.Element
import com.gzaber.keepnote.ui.utils.model.toElement
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

    private val _uiState = combine(
        foldersRepository.getAllFoldersFlow(),
        notesRepository.getFirstLevelNotesFlow(),
        _isGridView,
        _filterInfo
    ) { folders, notes, isGridView, filterInfo ->
        val elements = folders.map { it.toElement() } + notes.map { it.toElement() }
        val sortedElements = sortElements(elements, filterInfo)
        ElementsOverviewUiState(
            status = ElementsOverviewStatus.SUCCESS,
            elements = sortedElements,
            isGridView = isGridView,
            filterInfo = filterInfo
        )
    }
        .catch { ElementsOverviewUiState(status = ElementsOverviewStatus.FAILURE) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ElementsOverviewUiState()
        )

    val uiState: StateFlow<ElementsOverviewUiState> = _uiState

    fun toggleView() {
        _isGridView.value = !_isGridView.value
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

    // TODO: try catch
    fun deleteElement(element: Element) {
        viewModelScope.launch {
            if (element.isNote) {
                notesRepository.deleteNote(element.id!!)
            } else {
                foldersRepository.deleteFolder(element.id!!)
            }

        }
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
