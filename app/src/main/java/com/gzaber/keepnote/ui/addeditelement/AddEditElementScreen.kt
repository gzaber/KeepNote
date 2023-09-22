package com.gzaber.keepnote.ui.addeditelement

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gzaber.keepnote.R
import com.gzaber.keepnote.ui.elementsoverview.Status
import com.gzaber.keepnote.ui.theme.KeepNoteTheme

@Composable
fun AddEditElementScreen(
    viewModel: AddEditElementViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            AddEditElementAppBar(
                title = stringResource(
                    id = when (uiState.mode) {
                        AddEditMode.CREATE_NOTE -> R.string.create_note
                        AddEditMode.CREATE_FOLDER -> R.string.create_folder
                        AddEditMode.UPDATE_NOTE -> R.string.update_note
                        AddEditMode.UPDATE_FOLDER -> R.string.update_folder
                    }
                ),
                onSaveClick = viewModel::saveElement
            )
        }
    ) { paddingValues ->

        when (uiState.status) {
            Status.LOADING ->
                CircularProgressIndicator()

            Status.SUCCESS ->
                AddEditElementContent(
                    isNote = uiState.element.isNote,
                    contentPadding = paddingValues,
                    title = uiState.element.name,
                    content = uiState.element.content,
                    onTitleChange = viewModel::onTitleChanged,
                    onContentChange = viewModel::onContentChanged,
                    onColorSelect = viewModel::onColorChanged,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                )

            Status.FAILURE -> Text(
                text = stringResource(id = R.string.error_message),
                fontWeight = FontWeight.Bold,
                color = Color.Red
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditElementAppBar(
    title: String,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(text = title) },
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
            onSaveClick = {})
    }
}


@Composable
fun ColorCircle(
    color: Color,
    onClick: (Color) -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 36.dp
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(color)
            .clickable { onClick(color) }
    ) {
        if (isSelected) {
            Icon(
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = stringResource(id = R.string.select_color),
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun ColorPicker(
    onColorSelect: (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Gray)
    var selectedColor by remember {
        mutableStateOf(colors.first())
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        for (color in colors) {
            ColorCircle(
                color = color,
                onClick = {
                    selectedColor = color
                    onColorSelect(selectedColor)
                },
                isSelected = color == selectedColor
            )
        }
    }
}

@Preview
@Composable
fun ColorPickerPreview() {
    KeepNoteTheme {
        ColorPicker(
            onColorSelect = {}
        )
    }
}

@Composable
fun AddEditElementContent(
    isNote: Boolean,
    contentPadding: PaddingValues,
    title: String,
    content: String,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onColorSelect: (Color) -> Unit,
    modifier: Modifier = Modifier
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
            onColorSelect = onColorSelect
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
