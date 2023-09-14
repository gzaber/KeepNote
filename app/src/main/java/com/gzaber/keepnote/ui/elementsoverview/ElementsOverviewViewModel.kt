package com.gzaber.keepnote.ui.elementsoverview

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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ElementsOverviewViewModel @Inject constructor(
    foldersRepository: FoldersRepository,
    notesRepository: NotesRepository
) : ViewModel() {

   private val _uiState: StateFlow<UiState> = combine(
        foldersRepository.getAllFoldersFlow(),
        notesRepository.getFirstLevelNotesFlow()
    ) { folders, notes ->
        convertToElements(folders, notes)
    }
        .map {
            FlowResult.Success(it)
        }
        .catch<FlowResult> {
            emit(FlowResult.Error(message = "Error"))
        }
        .map {
            UiState(flowResult = it)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = UiState()
        )

    val uiState: StateFlow<UiState> = _uiState


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
    val flowResult: FlowResult = FlowResult.Loading
)

data class Element(
    val id: Int? = null,
    val isNote: Boolean,
    val name: String,
    val content: String,
    val color: Int,
    val date: Date = Date()
)

sealed class FlowResult {
    data class Success(val elements: List<Element>) : FlowResult()
    data class Error(val message: String) : FlowResult()
    object Loading : FlowResult()
}