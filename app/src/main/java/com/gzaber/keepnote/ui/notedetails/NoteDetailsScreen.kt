package com.gzaber.keepnote.ui.notedetails

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.gzaber.keepnote.R
import com.gzaber.keepnote.ui.notedetails.components.NoteDetailsContent
import com.gzaber.keepnote.ui.utils.components.KeepNoteAppBar
import com.gzaber.keepnote.ui.utils.components.LoadingBox

@Composable
fun NoteDetailsScreen(
    onBackClick: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: NoteDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val localContext = LocalContext.current

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            KeepNoteAppBar(
                title = R.string.note_details,
                onBackClick = onBackClick,
                onShareClick = {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "${uiState.note.title}\n${uiState.note.content}"
                        )
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(localContext, shareIntent, null)
                }
            )
        }
    ) { paddingValues ->

        when (uiState.status) {
            NoteDetailsStatus.LOADING -> LoadingBox(paddingValues = paddingValues)

            else -> NoteDetailsContent(
                note = uiState.note,
                contentPadding = paddingValues,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            )
        }
    }

    if (uiState.status == NoteDetailsStatus.FAILURE) {
        val errorMessage = stringResource(id = R.string.error_message)
        LaunchedEffect(uiState.status) {
            snackbarHostState.showSnackbar(errorMessage)
        }
    }
}





