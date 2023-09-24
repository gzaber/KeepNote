package com.gzaber.keepnote.ui.folderdetails

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gzaber.keepnote.R
import com.gzaber.keepnote.ui.elementsoverview.components.ElementsOverviewContent
import com.gzaber.keepnote.ui.folderdetails.components.FolderDetailsAppBar
import com.gzaber.keepnote.ui.utils.components.LoadingBox
import com.gzaber.keepnote.ui.utils.model.toElement


@Composable
fun FolderDetailsScreen(
    onBackClick: () -> Unit,
    onFabClick: (Int) -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: FolderDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            FolderDetailsAppBar(
                isGridView = uiState.isGridView,
                onBackClick = onBackClick,
                onFilterClick = { /*TODO*/ },
                onChangeViewClick = viewModel::toggleView
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                onClick = { onFabClick(uiState.folder.id!!) }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.create_note)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->

        when (uiState.status) {
            FolderDetailsStatus.LOADING -> LoadingBox(paddingValues = paddingValues)
            else -> ElementsOverviewContent(
                elements = uiState.notes.map { it.toElement() },
                isGridView = uiState.isGridView,
                contentPadding = paddingValues,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

