package com.gzaber.keepnote.ui.folderdetails

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gzaber.keepnote.R
import com.gzaber.keepnote.ui.utils.components.DetailsHeader
import com.gzaber.keepnote.ui.utils.components.EditDeleteElementBottomSheetContent
import com.gzaber.keepnote.ui.utils.components.ElementsListGridContent
import com.gzaber.keepnote.ui.utils.components.KeepNoteAppBar
import com.gzaber.keepnote.ui.utils.components.KeepNoteFloatingActionButton
import com.gzaber.keepnote.ui.utils.components.KeepNoteModalBottomSheet
import com.gzaber.keepnote.ui.utils.components.LoadingBox
import com.gzaber.keepnote.ui.utils.components.SortBottomSheetContent
import com.gzaber.keepnote.ui.utils.model.toElement
import kotlinx.coroutines.launch


sealed class BottomSheetStatus {
    object Hidden : BottomSheetStatus()
    data class EditDeleteNote(val noteId: Int) : BottomSheetStatus()
    object SortNotes : BottomSheetStatus()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderDetailsScreen(
    onNoteClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    onFabClick: (Int) -> Unit,
    onUpdateNote: (Int) -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: FolderDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var bottomSheetStatus by remember { mutableStateOf<BottomSheetStatus>(BottomSheetStatus.Hidden) }

    fun hideBottomSheet() {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                bottomSheetStatus = BottomSheetStatus.Hidden
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            KeepNoteAppBar(
                title = R.string.folder_details,
                isGridView = uiState.isGridView,
                onBackClick = onBackClick,
                onSortClick = { bottomSheetStatus = BottomSheetStatus.SortNotes },
                onChangeViewClick = viewModel::toggleView
            )
        },
        floatingActionButton = {
            KeepNoteFloatingActionButton(
                onClick = { onFabClick(uiState.folder.id!!) },
                contentDescription = R.string.create_note
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->

        when (uiState.status) {
            FolderDetailsStatus.LOADING -> LoadingBox(paddingValues = paddingValues)

            else -> ElementsListGridContent(
                elements = uiState.notes.map { it.toElement() },
                onItemClick = { _, id -> onNoteClick(id) },
                onItemLongClick = {
                    bottomSheetStatus = BottomSheetStatus.EditDeleteNote(noteId = it.id!!)
                },
                isGridView = uiState.isGridView,
                contentPadding = paddingValues,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                DetailsHeader(
                    title = uiState.folder.name,
                    subtitle = uiState.folder.date.toString(),
                    color = Color(uiState.folder.color)
                )
            }
        }
    }

    if (uiState.status == FolderDetailsStatus.FAILURE || uiState.isDeleteFailure) {
        val errorMessage = stringResource(id = R.string.error_message)
        LaunchedEffect(errorMessage) {
            snackbarHostState.showSnackbar(errorMessage)
            viewModel.snackbarMessageShown()
        }
    }

    if (bottomSheetStatus != BottomSheetStatus.Hidden) {
        KeepNoteModalBottomSheet(
            onDismissRequest = { bottomSheetStatus = BottomSheetStatus.Hidden },
            sheetState = sheetState
        ) {
            when (bottomSheetStatus) {
                is BottomSheetStatus.EditDeleteNote -> {
                    EditDeleteElementBottomSheetContent(
                        editButtonOnClick = {
                            onUpdateNote(
                                (bottomSheetStatus as BottomSheetStatus.EditDeleteNote).noteId
                            )
                        },
                        deleteButtonOnClick = {
                            viewModel.deleteNote(
                                (bottomSheetStatus as BottomSheetStatus.EditDeleteNote).noteId
                            )
                            hideBottomSheet()
                        }
                    )
                }

                BottomSheetStatus.SortNotes -> {
                    SortBottomSheetContent(
                        sortRadioOptions = uiState.sortInfo.sortRadioOptions,
                        sortSelectedOption = uiState.sortInfo.sortSelectedOption,
                        onSortOptionSelected = viewModel::onSortOptionSelected,
                        orderRadioOptions = uiState.sortInfo.orderRadioOptions,
                        orderSelectedOption = uiState.sortInfo.orderSelectedOption,
                        onOrderOptionSelected = viewModel::onOrderOptionSelected
                    )
                }

                else -> {}
            }
        }
    }
}


