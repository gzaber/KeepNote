package com.gzaber.keepnote.ui.folderdetails.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
fun FolderDetailsAppBar(
    isGridView: Boolean,
    onBackClick: () -> Unit,
    onFilterClick: () -> Unit,
    onChangeViewClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(id = R.string.folder_details)) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.navigate_back)
                )
            }
        },
        actions = {
            IconButton(
                onClick = onFilterClick
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_filter),
                    contentDescription = stringResource(id = R.string.filter)
                )
            }
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
        },
        modifier = modifier
    )
}

@Preview
@Composable
fun FolderDetailsAppBarPreview() {
    KeepNoteTheme {
        FolderDetailsAppBar(
            isGridView = false,
            onBackClick = { },
            onFilterClick = { },
            onChangeViewClick = { })
    }
}