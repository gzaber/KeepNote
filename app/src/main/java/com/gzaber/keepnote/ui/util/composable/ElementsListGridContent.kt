package com.gzaber.keepnote.ui.util.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gzaber.keepnote.ui.theme.KeepNoteTheme
import com.gzaber.keepnote.ui.util.model.Element


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ElementsListGridContent(
    elements: List<Element>,
    isGridView: Boolean,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onItemClick: (Boolean, Int) -> Unit = { _, _ -> },
    onItemLongClick: (Element) -> Unit = {},
    horizontalSpace: Dp = 8.dp,
    verticalSpace: Dp = 8.dp,
    header: @Composable (() -> Unit) = {},
) {
    if (isGridView) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            contentPadding = contentPadding,
            verticalItemSpacing = verticalSpace,
            horizontalArrangement = Arrangement.spacedBy(horizontalSpace),
            modifier = modifier
        ) {
            item {
                header()
            }
            items(elements) { element ->
                ElementItem(
                    isGridItem = true,
                    element = element,
                    onClick = onItemClick,
                    onLongClick = onItemLongClick
                )
            }
        }
    } else {
        LazyColumn(
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(verticalSpace),
            modifier = modifier
        ) {
            item {
                header()
            }
            items(elements) { element ->
                ElementItem(
                    element = element,
                    onClick = onItemClick,
                    onLongClick = onItemLongClick
                )
            }
        }
    }
}

@Preview
@Composable
fun ElementsListContentPreview() {
    KeepNoteTheme {
        ElementsListGridContent(
            isGridView = false,
            contentPadding = PaddingValues(8.dp),
            elements = listOf(
                Element.empty().copy(
                    isNote = true,
                    name = "Note title1 eget mauris pharetra et ultrices neque ornare aenean",
                    content = "Content1 non quam lacus suspendisse faucibus interdum posuere lorem ipsum dolor sit amet",
                    color = Color.Red.toArgb()
                ),
                Element.empty().copy(
                    isNote = false,
                    name = "Folder name1 eget mauris pharetra et ultrices neque ornare aenean",
                    content = "",
                    color = Color.Green.toArgb()
                ),
                Element.empty().copy(
                    isNote = true,
                    name = "Note title2",
                    content = "Content2 non quam lacus suspendisse faucibus interdum posuere lorem ipsum dolor sit amet",
                    color = Color.Blue.toArgb()
                ),
                Element.empty().copy(
                    isNote = false,
                    name = "Folder name2 eget mauris pharetra et ultrices neque ornare aenean",
                    content = "",
                    color = Color.Magenta.toArgb()
                )
            )
        )
    }
}

@Preview
@Composable
fun ElementsGridContentPreview() {
    KeepNoteTheme {
        ElementsListGridContent(
            isGridView = true,
            contentPadding = PaddingValues(8.dp),
            elements = listOf(
                Element.empty().copy(
                    isNote = true,
                    name = "Note title1",
                    content = "Content1 lorem ipsum dolor sit amet",
                    color = Color.Red.toArgb()
                ),
                Element.empty().copy(
                    isNote = false,
                    name = "Folder name1",
                    content = "",
                    color = Color.Green.toArgb()
                ),
                Element.empty().copy(
                    isNote = true,
                    name = "Note title2",
                    content = "Content2 lorem ipsum dolor sit amet",
                    color = Color.Blue.toArgb()
                ),
                Element.empty().copy(
                    isNote = false,
                    name = "Folder name2",
                    content = "",
                    color = Color.Magenta.toArgb()
                )
            )
        )
    }
}

@Preview
@Composable
fun FolderDetailsListContentPreview() {
    KeepNoteTheme {
        ElementsListGridContent(
            isGridView = false,
            contentPadding = PaddingValues(8.dp),
            elements = listOf(
                Element.empty().copy(
                    isNote = true,
                    name = "Note title1 eget mauris pharetra et ultrices neque ornare aenean",
                    content = "Content1 non quam lacus suspendisse faucibus interdum posuere lorem ipsum dolor sit amet",
                    color = Color.Red.toArgb()
                ),
                Element.empty().copy(
                    isNote = true,
                    name = "Note title2",
                    content = "Content2 non quam lacus suspendisse faucibus interdum posuere lorem ipsum dolor sit amet",
                    color = Color.Blue.toArgb()
                )
            )
        ) {
            DetailsHeader(
                title = "Folder details",
                subtitle = "Thu 22 September 2023",
                color = Color.Green
            )
        }
    }
}

@Preview
@Composable
fun FolderDetailsGridContentPreview() {
    KeepNoteTheme {
        ElementsListGridContent(
            isGridView = true,
            contentPadding = PaddingValues(8.dp),
            elements = listOf(
                Element.empty().copy(
                    isNote = true,
                    name = "Note title1 eget mauris pharetra et ultrices neque ornare aenean",
                    content = "Content1 non quam lacus suspendisse faucibus interdum posuere lorem ipsum dolor sit amet",
                    color = Color.Red.toArgb()
                ),
                Element.empty().copy(
                    isNote = true,
                    name = "Note title2",
                    content = "Content2 non quam lacus suspendisse faucibus interdum posuere lorem ipsum dolor sit amet",
                    color = Color.Blue.toArgb()
                )
            )
        ) {
            DetailsHeader(
                title = "Folder details",
                subtitle = "Thu 22 September 2023",
                color = Color.Green
            )
        }
    }
}