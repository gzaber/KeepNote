package com.gzaber.keepnote.ui.notedetails.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.gzaber.keepnote.R
import com.gzaber.keepnote.ui.theme.KeepNoteTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailsAppBar(
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = { Text(text = stringResource(id = R.string.note_details)) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.navigate_back)
                )
            }
        },
        actions = {
            IconButton(onClick = onShareClick) {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = stringResource(id = R.string.share_note)
                )
            }
        }
    )
}

@Preview
@Composable
fun NoteDetailsAppBarPreview() {
    KeepNoteTheme {
        NoteDetailsAppBar(
            onBackClick = {},
            onShareClick = {}
        )
    }
}