package com.gzaber.keepnote.ui.util.composable

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.gzaber.keepnote.R
import com.gzaber.keepnote.ui.theme.KeepNoteTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeepNoteAppBar(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    isGridView: Boolean = false,
    isTitleBold: Boolean = false,
    onBackClick: (() -> Unit)? = null,
    onSortClick: (() -> Unit)? = null,
    onChangeViewClick: (() -> Unit)? = null,
    onSaveClick: (() -> Unit)? = null,
    onShareClick: (() -> Unit)? = null
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = if (isTitleBold) FontWeight.Bold else FontWeight.Normal
                )
            )
        },
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
            if (onSortClick != null) {
                IconButton(
                    onClick = onSortClick
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_sort),
                        contentDescription = stringResource(id = R.string.sort)
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
                        contentDescription = stringResource(
                            id = if (isGridView) R.string.list_view else R.string.grid_view
                        )
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
fun ElementsOverviewAppBarListViewPreview() {
    KeepNoteTheme {
        KeepNoteAppBar(
            title = R.string.app_name,
            isTitleBold = true,
            onSortClick = {},
            onChangeViewClick = {},
        )
    }
}

@Preview
@Composable
fun ElementsOverviewAppBarGridViewPreview() {
    KeepNoteTheme {
        KeepNoteAppBar(
            title = R.string.app_name,
            isGridView = true,
            isTitleBold = true,
            onSortClick = {},
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
            onSortClick = {},
            onChangeViewClick = {},
        )
    }
}