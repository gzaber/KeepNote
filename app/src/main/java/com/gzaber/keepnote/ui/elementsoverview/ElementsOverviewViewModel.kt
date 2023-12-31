package com.gzaber.keepnote.ui.elementsoverview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gzaber.keepnote.R
import com.gzaber.keepnote.data.repository.FoldersRepository
import com.gzaber.keepnote.data.repository.NotesRepository
import com.gzaber.keepnote.ui.util.model.Element
import com.gzaber.keepnote.ui.util.model.toElement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ElementsOverviewStatus {
    LOADING, SUCCESS, FAILURE
}

data class SortInfo(
    val sortRadioOptions: List<Int> = listOf(R.string.radio_date, R.string.radio_name),
    val sortSelectedOption: Int = sortRadioOptions.first(),
    val orderRadioOptions: List<Int> = listOf(R.string.radio_ascending, R.string.radio_descending),
    val orderSelectedOption: Int = orderRadioOptions.first(),
    val firstElementsRadioOptions: List<Int> = listOf(
        R.string.radio_not_applicable,
        R.string.radio_folders,
        R.string.radio_notes
    ),
    val firstElementsSelectedOption: Int = firstElementsRadioOptions.first()
)

data class ElementsOverviewUiState(
    val status: ElementsOverviewStatus = ElementsOverviewStatus.LOADING,
    val elements: List<Element> = listOf(),
    val isGridView: Boolean = false,
    val isDeleteFailure: Boolean = false,
    val sortInfo: SortInfo = SortInfo()
)

@HiltViewModel
class ElementsOverviewViewModel @Inject constructor(
    private val foldersRepository: FoldersRepository,
    private val notesRepository: NotesRepository
) : ViewModel() {

    private val _isGridView: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _isDeleteFailure: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _isRepositoryFailure: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _sortInfo: MutableStateFlow<SortInfo> = MutableStateFlow(SortInfo())

    private val _elements = combine(
        foldersRepository.getAllFoldersFlow(),
        notesRepository.getFirstLevelNotesFlow(),
        _sortInfo
    ) { folders, notes, sortInfo ->
        val elements = folders.map { it.toElement() } + notes.map { it.toElement() }
        sortElements(elements, sortInfo)
    }
        .catch { _isRepositoryFailure.value = true }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = listOf()
        )

    val uiState = combine(
        _elements,
        _isGridView,
        _isDeleteFailure,
        _isRepositoryFailure,
        _sortInfo
    ) { elements, isGridView, isDeleteFailure, isRepositoryFailure, sortInfo ->

        ElementsOverviewUiState(
            status = if (isRepositoryFailure) ElementsOverviewStatus.FAILURE else ElementsOverviewStatus.SUCCESS,
            elements = elements,
            isGridView = isGridView,
            isDeleteFailure = isDeleteFailure,
            sortInfo = sortInfo
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ElementsOverviewUiState()
        )

    fun toggleView() {
        _isGridView.value = !_isGridView.value
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

    fun onFirstElementsOptionSelected(optionSelected: Int) {
        _sortInfo.update {
            it.copy(firstElementsSelectedOption = optionSelected)
        }
    }

    fun deleteElement(element: Element) {
        viewModelScope.launch {
            try {
                if (element.id != null) {
                    if (element.isNote) {
                        notesRepository.deleteNote(element.id)
                    } else {
                        foldersRepository.deleteFolder(element.id)
                    }
                } else {
                    throw (NullPointerException())
                }
            } catch (e: Throwable) {
                _isDeleteFailure.value = true
            }
        }
    }

    fun snackbarMessageShown() {
        _isDeleteFailure.value = false
    }


    private fun sortElements(elements: List<Element>, sortInfo: SortInfo): List<Element> {
        return when (sortInfo.firstElementsSelectedOption) {

            sortInfo.firstElementsRadioOptions.first() -> basicSortElements(elements, sortInfo)

            else -> {
                val folderElements = elements.filter { it.isNote.not() }
                val noteElements = elements.filter { it.isNote }
                if (sortInfo.firstElementsSelectedOption == sortInfo.firstElementsRadioOptions[1]) {
                    basicSortElements(folderElements, sortInfo) +
                            basicSortElements(noteElements, sortInfo)
                } else {
                    basicSortElements(noteElements, sortInfo) + basicSortElements(
                        folderElements,
                        sortInfo
                    )
                }
            }
        }
    }

    private fun basicSortElements(elements: List<Element>, sortInfo: SortInfo): List<Element> {
        return when (sortInfo.orderSelectedOption) {
            sortInfo.orderRadioOptions.first() -> {
                when (sortInfo.sortSelectedOption) {
                    sortInfo.sortRadioOptions.first() -> elements.sortedBy { it.date }
                    else -> elements.sortedBy { it.name }
                }
            }

            else -> {
                when (sortInfo.sortSelectedOption) {
                    sortInfo.sortRadioOptions.first() -> elements.sortedByDescending { it.date }
                    else -> elements.sortedByDescending { it.name }
                }
            }
        }
    }
}
