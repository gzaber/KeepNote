package com.gzaber.keepnote.ui.elementsoverview.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.gzaber.keepnote.R
import com.gzaber.keepnote.ui.theme.KeepNoteTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElementsOverviewAppBar(
    onFilterClick: () -> Unit,
    onChangeViewClick: () -> Unit,
    isGridView: Boolean,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
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
fun ElementsOverviewAppBarPreview() {
    KeepNoteTheme {
        ElementsOverviewAppBar(
            onFilterClick = { },
            onChangeViewClick = { },
            isGridView = true
        )
    }
}