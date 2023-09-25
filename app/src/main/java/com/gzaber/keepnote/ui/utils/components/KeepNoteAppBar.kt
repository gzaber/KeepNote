package com.gzaber.keepnote.ui.utils.components

import androidx.annotation.StringRes
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.gzaber.keepnote.R
import com.gzaber.keepnote.ui.theme.KeepNoteTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeepNoteAppBar(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    isGridView: Boolean = false,
    onBackClick: (() -> Unit)? = null,
    onFilterClick: (() -> Unit)? = null,
    onChangeViewClick: (() -> Unit)? = null,
    onSaveClick: (() -> Unit)? = null,
    onShareClick: (() -> Unit)? = null
) {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(id = title)) },
        navigationIcon = {
            if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.navigate_back)
                    )
                }
            }
        },
        actions = {
            if (onFilterClick != null) {
                IconButton(
                    onClick = onFilterClick
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_filter),
                        contentDescription = stringResource(id = R.string.filter)
                    )
                }
            }
            if (onChangeViewClick != null) {
                IconButton(
                    onClick = onChangeViewClick
                )
                {
                    Icon(
                        painter = painterResource(
                            id = if (isGridView) R.drawable.ic_list_view else R.drawable.ic_grid_view
                        ),
                        contentDescription = stringResource(id = R.string.grid_view)
                    )
                }
            }
            if (onSaveClick != null) {
                IconButton(
                    onClick = onSaveClick
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_save),
                        contentDescription = stringResource(id = R.string.save)
                    )
                }
            }
            if (onShareClick != null) {
                IconButton(onClick = onShareClick) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = stringResource(id = R.string.share_note)
                    )
                }
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
fun ElementsOverviewAppBarPreview() {
    KeepNoteTheme {
        KeepNoteAppBar(
            title = R.string.app_name,
            onFilterClick = {},
            onChangeViewClick = {},
        )
    }
}

@Preview
@Composable
fun AddEditElementAppBarPreview() {
    KeepNoteTheme {
        KeepNoteAppBar(
            title = R.string.create_note,
            onBackClick = {},
            onSaveClick = {}
        )
    }
}

@Preview
@Composable
fun NoteDetailsAppBarPreview() {
    KeepNoteTheme {
        KeepNoteAppBar(
            title = R.string.note_details,
            onBackClick = {},
            onShareClick = {}
        )
    }
}

@Preview
@Composable
fun FolderDetailsAppBarPreview() {
    KeepNoteTheme {
        KeepNoteAppBar(
            title = R.string.folder_details,
            onBackClick = {},
            onFilterClick = {},
            onChangeViewClick = {},
        )
    }
}