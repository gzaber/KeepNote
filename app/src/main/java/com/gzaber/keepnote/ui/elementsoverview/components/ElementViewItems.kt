package com.gzaber.keepnote.ui.elementsoverview.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.TestModifierUpdaterLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gzaber.keepnote.R
import com.gzaber.keepnote.ui.elementsoverview.Element
import com.gzaber.keepnote.ui.theme.KeepNoteTheme


@Composable
fun ElementIcon(
    isNote: Boolean,
    modifier: Modifier = Modifier
) {
    if (isNote) Icon(
        painter = painterResource(id = R.drawable.ic_note),
        contentDescription = stringResource(id = R.string.note),
        modifier = modifier
    )
    else Icon(
        painter = painterResource(id = R.drawable.ic_folder),
        contentDescription = stringResource(id = R.string.folder),
        modifier = modifier
    )
}

@Composable
fun ElementCircle(
    color: Int,
    modifier: Modifier = Modifier,
    size: Dp = 16.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Color(color))
    )
}

@Composable
fun ElementText(
    element: Element,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = element.name,
            fontWeight = FontWeight.Bold
        )
        if (element.isNote) Text(
            text = element.content,
            overflow = TextOverflow.Ellipsis,
            maxLines = 3
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ElementItem(
    element: Element,
    modifier: Modifier = Modifier,
    onClick: (Element) -> Unit = {},
    onLongClick: (Element) -> Unit = {},
    isGridItem: Boolean = false
) {
    Card(
        modifier = modifier
            .combinedClickable(
                onClick = { onClick(element) },
                onLongClick = { onLongClick(element) }
            )
    ) {
        if (isGridItem) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                ElementText(element = element)
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ElementIcon(isNote = element.isNote)
                    ElementCircle(color = element.color)
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ElementIcon(isNote = element.isNote)
                Spacer(modifier = Modifier.width(16.dp))
                ElementText(element = element, modifier = Modifier.weight(1F))
                Spacer(modifier = Modifier.width(16.dp))
                ElementCircle(color = element.color)
            }
        }
    }
}


@Preview
@Composable
fun NoteElementGridItemPreview() {
    KeepNoteTheme {
        ElementItem(
            modifier = Modifier.width(200.dp),
            isGridItem = true,
            element = Element(
                isNote = true,
                name = "eget mauris pharetra et ultrices neque ornare aenean",
                content = "non quam lacus suspendisse faucibus interdum posuere lorem ipsum dolor sit amet",
                color = Color.Red.toArgb()
            )
        )
    }
}

@Preview
@Composable
fun FolderElementGridItemPreview() {
    KeepNoteTheme {
        ElementItem(
            modifier = Modifier.width(200.dp),
            isGridItem = true,
            element = Element(
                isNote = false,
                name = "eget mauris pharetra et ultrices neque ornare aenean",
                content = "",
                color = Color.Red.toArgb()
            )
        )
    }
}

@Preview
@Composable
fun NoteElementListItemPreview() {
    KeepNoteTheme {
        ElementItem(
            isGridItem = false,
            element = Element(
                isNote = true,
                name = "eget mauris pharetra et ultrices neque ornare aenean",
                content = "non quam lacus suspendisse faucibus interdum posuere lorem ipsum dolor sit amet",
                color = Color.Red.toArgb()
            )
        )
    }
}

@Preview
@Composable
fun FolderElementListItemPreview() {
    KeepNoteTheme {
        ElementItem(
            isGridItem = false,
            element = Element(
                isNote = false,
                name = "eget mauris pharetra et ultrices neque ornare aenean",
                content = "",
                color = Color.Red.toArgb()
            )
        )
    }
}