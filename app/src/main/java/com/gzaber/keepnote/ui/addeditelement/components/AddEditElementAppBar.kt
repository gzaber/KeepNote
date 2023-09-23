package com.gzaber.keepnote.ui.addeditelement.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
fun AddEditElementAppBar(
    title: String,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack, contentDescription = stringResource(
                        id = R.string.navigate_back
                    )
                )
            }
        },
        actions = {
            IconButton(
                onClick = onSaveClick
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_save),
                    contentDescription = stringResource(id = R.string.save)
                )
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
fun AddEditElementAppBarPreview() {
    KeepNoteTheme {
        AddEditElementAppBar(
            title = "Title",
            onBackClick = {},
            onSaveClick = {}
        )
    }
}