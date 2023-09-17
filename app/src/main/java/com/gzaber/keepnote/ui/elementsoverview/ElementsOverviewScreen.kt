package com.gzaber.keepnote.ui.elementsoverview


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gzaber.keepnote.R
import com.gzaber.keepnote.ui.elementsoverview.components.CreateElementBottomSheetContent
import com.gzaber.keepnote.ui.elementsoverview.components.EditDeleteElementBottomSheetContent
import com.gzaber.keepnote.ui.elementsoverview.components.ElementsAppBar
import com.gzaber.keepnote.ui.elementsoverview.components.ElementsOverviewContent
import com.gzaber.keepnote.ui.elementsoverview.components.FilterBottomSheetContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElementsOverviewScreen(
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
        topBar = {
            ElementsAppBar(
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
            Status.LOADING -> CircularProgressIndicator()
            Status.SUCCESS ->
                ElementsOverviewContent(
                    elements = uiState.elements,
                    isGridView = uiState.isGridView,
                    contentPadding = paddingValues,
                    onItemClick = { },
                    onItemLongClick = {
                        bottomSheetStatus = BottomSheetStatus.EditDeleteElement(element = it)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                )

            Status.FAILURE -> Text(
                text = stringResource(id = R.string.error_message),
                fontWeight = FontWeight.Bold,
                color = Color.Red
            )
        }

        if (bottomSheetStatus != BottomSheetStatus.Hidden) {
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
                                    viewModel.createFolder()
                                    hideBottomSheet()
                                },
                                noteButtonOnClick = {
                                    viewModel.createNote()
                                    hideBottomSheet()
                                }
                            )
                        }

                        is BottomSheetStatus.EditDeleteElement -> {
                            EditDeleteElementBottomSheetContent(
                                editButtonOnClick = { /*TODO*/ },
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
}

sealed class BottomSheetStatus {
    object Hidden : BottomSheetStatus()
    object CreateElement : BottomSheetStatus()
    data class EditDeleteElement(val element: Element) : BottomSheetStatus()
    object FilterElements : BottomSheetStatus()
}

//@OptIn(ExperimentalMaterial3Api::class)
//fun hideBottomSheet(
//    scope: CoroutineScope,
//    sheetState: SheetState,
//    changeSheetStatus: () -> Unit
//) {
//    scope.launch { sheetState.hide() }.invokeOnCompletion {
//        if (!sheetState.isVisible) {
//            changeSheetStatus()
//        }
//    }
//}







