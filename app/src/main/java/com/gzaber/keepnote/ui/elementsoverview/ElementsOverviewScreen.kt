package com.gzaber.keepnote.ui.elementsoverview


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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gzaber.keepnote.R
import com.gzaber.keepnote.ui.util.composable.CreateElementBottomSheetContent
import com.gzaber.keepnote.ui.util.composable.EditDeleteElementBottomSheetContent
import com.gzaber.keepnote.ui.util.composable.ElementsListGridContent
import com.gzaber.keepnote.ui.util.composable.KeepNoteAppBar
import com.gzaber.keepnote.ui.util.composable.KeepNoteFloatingActionButton
import com.gzaber.keepnote.ui.util.composable.KeepNoteModalBottomSheet
import com.gzaber.keepnote.ui.util.composable.LoadingBox
import com.gzaber.keepnote.ui.util.composable.SortBottomSheetContent
import com.gzaber.keepnote.ui.util.model.Element
import kotlinx.coroutines.launch


sealed class BottomSheetStatus {
    data object Hidden : BottomSheetStatus()
    data object CreateElement : BottomSheetStatus()
    data class EditDeleteElement(val element: Element) : BottomSheetStatus()
    data object SortElements : BottomSheetStatus()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElementsOverviewScreen(
    onElementClick: (Boolean, Int) -> Unit,
    onCreateElement: (Boolean) -> Unit,
    onUpdateElement: (Boolean, Int?) -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: ElementsOverviewViewModel = hiltViewModel()
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
                title = R.string.app_name,
                isTitleBold = true,
                isGridView = uiState.isGridView,
                onSortClick = {
                    bottomSheetStatus = BottomSheetStatus.SortElements
                },
                onChangeViewClick = viewModel::toggleView,
            )
        },
        floatingActionButton = {
            KeepNoteFloatingActionButton(
                onClick = { bottomSheetStatus = BottomSheetStatus.CreateElement },
                contentDescription = R.string.create_element
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { paddingValues ->

        when (uiState.status) {
            ElementsOverviewStatus.LOADING -> LoadingBox(paddingValues = paddingValues)

            else -> ElementsListGridContent(
                elements = uiState.elements,
                isGridView = uiState.isGridView,
                contentPadding = paddingValues,
                onItemClick = onElementClick,
                onItemLongClick = {
                    bottomSheetStatus = BottomSheetStatus.EditDeleteElement(element = it)
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            )
        }
    }

    if (uiState.status == ElementsOverviewStatus.FAILURE || uiState.isDeleteFailure) {
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
                BottomSheetStatus.CreateElement -> {
                    CreateElementBottomSheetContent(
                        folderButtonOnClick = {
                            onCreateElement(false)
                            hideBottomSheet()
                        },
                        noteButtonOnClick = {
                            onCreateElement(true)
                            hideBottomSheet()
                        }
                    )
                }

                is BottomSheetStatus.EditDeleteElement -> {
                    EditDeleteElementBottomSheetContent(
                        editButtonOnClick = {
                            onUpdateElement(
                                (bottomSheetStatus as BottomSheetStatus.EditDeleteElement).element.isNote,
                                (bottomSheetStatus as BottomSheetStatus.EditDeleteElement).element.id
                            )
                        },
                        deleteButtonOnClick = {
                            viewModel.deleteElement(
                                (bottomSheetStatus as BottomSheetStatus.EditDeleteElement).element
                            )
                            hideBottomSheet()
                        }
                    )
                }

                BottomSheetStatus.SortElements -> {
                    SortBottomSheetContent(
                        sortRadioOptions = uiState.sortInfo.sortRadioOptions,
                        sortSelectedOption = uiState.sortInfo.sortSelectedOption,
                        onSortOptionSelected = viewModel::onSortOptionSelected,
                        orderRadioOptions = uiState.sortInfo.orderRadioOptions,
                        orderSelectedOption = uiState.sortInfo.orderSelectedOption,
                        onOrderOptionSelected = viewModel::onOrderOptionSelected,
                        firstElementsRadioOptions = uiState.sortInfo.firstElementsRadioOptions,
                        firstElementsSelectedOption = uiState.sortInfo.firstElementsSelectedOption,
                        onFirstElementsOptionSelected = viewModel::onFirstElementsOptionSelected
                    )
                }

                else -> {}
            }
        }
    }
}









