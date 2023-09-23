package com.gzaber.keepnote.ui.addeditelement.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gzaber.keepnote.R
import com.gzaber.keepnote.ui.theme.KeepNoteTheme


@Composable
fun AddEditElementContent(
    isNote: Boolean,
    contentPadding: PaddingValues,
    title: String,
    content: String,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onColorSelect: (Color) -> Unit,
    modifier: Modifier = Modifier,
    color: Color? = null
) {
    Column(
        modifier = modifier.padding(contentPadding)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            singleLine = true,
            label = { Text(stringResource(id = R.string.title)) },
            modifier = Modifier.fillMaxWidth()
        )
        if (isNote) {
            OutlinedTextField(
                value = content,
                onValueChange = onContentChange,
                label = { Text(stringResource(id = R.string.content)) },
                modifier = Modifier
                    .weight(1F)
                    .fillMaxWidth()
            )
        }
        ColorPicker(
            onColorSelect = onColorSelect,
            currentColor = color,
        )
    }
}

@Preview
@Composable
fun AddEditElementContentPreview() {
    KeepNoteTheme {
        AddEditElementContent(
            isNote = true,
            title = "",
            onTitleChange = {},
            content = "",
            onContentChange = {},
            onColorSelect = {},
            contentPadding = PaddingValues(),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
    }
}