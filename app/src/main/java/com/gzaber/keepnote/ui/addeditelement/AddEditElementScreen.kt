package com.gzaber.keepnote.ui.addeditelement

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gzaber.keepnote.R
import com.gzaber.keepnote.ui.addeditelement.composable.AddEditElementContent
import com.gzaber.keepnote.ui.util.composable.KeepNoteAppBar
import com.gzaber.keepnote.ui.util.composable.LoadingBox


@Composable
fun AddEditElementScreen(
    onBackClick: () -> Unit,
    onSaved: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: AddEditElementViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            KeepNoteAppBar(
                title = when (uiState.mode) {
                    AddEditElementMode.CREATE_NOTE, AddEditElementMode.CREATE_CHILD_NOTE -> R.string.create_note
                    AddEditElementMode.CREATE_FOLDER -> R.string.create_folder
                    AddEditElementMode.UPDATE_NOTE -> R.string.update_note
                    AddEditElementMode.UPDATE_FOLDER -> R.string.update_folder
                },
                onBackClick = onBackClick,
                onSaveClick = viewModel::saveElement
            )
        }
    ) { paddingValues ->

        when (uiState.status) {
            AddEditElementStatus.LOADING -> LoadingBox(paddingValues = paddingValues)

            else ->
                AddEditElementContent(
                    isNote = uiState.element.isNote,
                    contentPadding = paddingValues,
                    title = uiState.element.name,
                    content = uiState.element.content,
                    color = Color(uiState.element.color),
                    onTitleChange = viewModel::onTitleChanged,
                    onContentChange = viewModel::onContentChanged,
                    onColorSelect = viewModel::onColorChanged,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                )
        }
    }

    uiState.status.let {
        if (it == AddEditElementStatus.SAVE_SUCCESS) {
            LaunchedEffect(uiState.status) {
                onSaved()
            }
        }
        if (it == AddEditElementStatus.FAILURE) {
            val errorMessage = stringResource(id = R.string.error_message)
            LaunchedEffect(uiState.status) {
                snackbarHostState.showSnackbar(errorMessage)
            }
        }
    }
}


