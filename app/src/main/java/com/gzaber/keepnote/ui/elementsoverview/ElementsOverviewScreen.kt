package com.gzaber.keepnote.ui.elementsoverview


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
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
import com.gzaber.keepnote.ui.elementsoverview.components.CreateElementBottomSheetContent
import com.gzaber.keepnote.ui.elementsoverview.components.EditDeleteElementBottomSheetContent
import com.gzaber.keepnote.ui.elementsoverview.components.ElementsOverviewAppBar
import com.gzaber.keepnote.ui.elementsoverview.components.ElementsOverviewContent
import com.gzaber.keepnote.ui.elementsoverview.components.FilterBottomSheetContent
import com.gzaber.keepnote.ui.utils.components.LoadingBox
import com.gzaber.keepnote.ui.utils.model.Element
import kotlinx.coroutines.launch


sealed class BottomSheetStatus {
    object Hidden : BottomSheetStatus()
    object CreateElement : BottomSheetStatus()
    data class EditDeleteElement(val element: Element) : BottomSheetStatus()
    object FilterElements : BottomSheetStatus()
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
            ElementsOverviewAppBar(
                onFilterClick = {
                    bottomSheetStatus = BottomSheetStatus.FilterElements
                },
                onChangeViewClick = viewModel::toggleView,
                isGridView = uiState.isGridView
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                onClick = { bottomSheetStatus = BottomSheetStatus.CreateElement }
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.create_element)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { paddingValues ->

        when (uiState.status) {
            ElementsOverviewStatus.LOADING -> LoadingBox(paddingValues = paddingValues)

            else -> ElementsOverviewContent(
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

    if (uiState.status == ElementsOverviewStatus.FAILURE) {
        val errorMessage = stringResource(id = R.string.error_message)
        LaunchedEffect(uiState.status) {
            snackbarHostState.showSnackbar(errorMessage)
        }
    }


    if (bottomSheetStatus != BottomSheetStatus.Hidden) {
        // TODO: extract composable
        ModalBottomSheet(
            onDismissRequest = { bottomSheetStatus = BottomSheetStatus.Hidden },
            sheetState = sheetState
        ) {
            Box(
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 100.dp)
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

                    BottomSheetStatus.FilterElements -> {
                        FilterBottomSheetContent()
                    }

                    else -> {}
                }
            }
        }
    }
}









