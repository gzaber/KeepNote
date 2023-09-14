package com.gzaber.keepnote.ui.elementsoverview

import android.graphics.Color.toArgb
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gzaber.keepnote.R
import com.gzaber.keepnote.ui.theme.KeepNoteTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElementsOverviewScreen(
    viewModel: ElementsOverviewViewModel = hiltViewModel()
) {
    //val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            ElementsAppBar(
                onFilterClick = {},
                onChangeViewClick = {}
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElementsAppBar(
    onFilterClick: () -> Unit,
    onChangeViewClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        actions = {
            IconButton(
                onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_filter),
                    contentDescription = stringResource(id = R.string.content_desc_filter)
                )
            }
            IconButton(
                onClick = { /*TODO*/ })
            {
                Icon(
                    painter = painterResource(id = R.drawable.ic_grid_view),
                    contentDescription = stringResource(id = R.string.content_desc_grid_view)
                )
            }
        }
    )
}

@Preview
@Composable
fun ElementsAppBarPreview() {
    KeepNoteTheme {
        ElementsAppBar(
            onFilterClick = { },
            onChangeViewClick = { }
        )
    }
}

@Composable
fun ElementGridItem(
    element: Element,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = element.name,
                fontWeight = FontWeight.Bold
            )
            if (element.isNote) Text(text = element.content)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (element.isNote) Icon(
                    painter = painterResource(id = R.drawable.ic_note),
                    contentDescription = stringResource(
                        id = R.string.content_desc_note
                    )
                )
                else Icon(
                    painter = painterResource(id = R.drawable.ic_folder),
                    contentDescription = stringResource(
                        id = R.string.content_desc_folder
                    )
                )
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color(element.color))
                )
            }
        }
    }
}

@Preview
@Composable
fun NoteElementGridItemPreview() {
    KeepNoteTheme {
        ElementGridItem(
            element = Element(
                isNote = true,
                name = "Note title",
                content = "Lorem ipsum dolor sit amet",
                color = Color.Red.toArgb()
            )
        )
    }
}

@Preview
@Composable
fun FolderElementGridItemPreview() {
    KeepNoteTheme {
        ElementGridItem(
            element = Element(
                isNote = false,
                name = "Folder name",
                content = "",
                color = Color.Blue.toArgb()
            )
        )
    }
}


@Composable
fun ElementListItem(
    element: Element,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (element.isNote) Icon(
                painter = painterResource(id = R.drawable.ic_note),
                contentDescription = stringResource(
                    id = R.string.content_desc_note
                )
            )
            else Icon(
                painter = painterResource(id = R.drawable.ic_folder),
                contentDescription = stringResource(
                    id = R.string.content_desc_folder
                )
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = element.name,
                    fontWeight = FontWeight.Bold
                )
                if (element.isNote) Text(text = element.content)
            }
            Spacer(modifier = Modifier.weight(1F))
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color(element.color))
            )
        }
    }
}

@Preview
@Composable
fun NoteElementListItemPreview() {
    KeepNoteTheme {
        ElementListItem(
            element = Element(
                isNote = true,
                name = "Note title",
                content = "Lorem ipsum dolor sit amet",
                color = Color.Magenta.toArgb()
            )
        )
    }
}

@Preview
@Composable
fun FolderElementListItemPreview() {
    KeepNoteTheme {
        ElementListItem(
            element = Element(
                isNote = false,
                name = "Folder name",
                content = "",
                color = Color.Green.toArgb()
            )
        )
    }
}

