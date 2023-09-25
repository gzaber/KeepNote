package com.gzaber.keepnote.ui.utils.components

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
import com.gzaber.keepnote.ui.utils.model.Element


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ElementsOverviewContent(
    elements: List<Element>,
    isGridView: Boolean,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    onItemClick: (Boolean, Int) -> Unit = { _, _ -> },
    onItemLongClick: (Element) -> Unit = {},
    horizontalSpace: Dp = 8.dp,
    verticalSpace: Dp = 8.dp,
) {
    if (isGridView) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            contentPadding = contentPadding,
            verticalItemSpacing = verticalSpace,
            horizontalArrangement = Arrangement.spacedBy(horizontalSpace),
            modifier = modifier
        ) {
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
fun ListViewElementsContentPreview() {
    KeepNoteTheme {
        ElementsOverviewContent(
            isGridView = false,
            contentPadding = PaddingValues(8.dp),
            elements = listOf(
                Element(
                    isNote = true,
                    name = "eget mauris pharetra et ultrices neque ornare aenean",
                    content = "non quam lacus suspendisse faucibus interdum posuere lorem ipsum dolor sit amet",
                    color = Color.Red.toArgb()
                ),
                Element(
                    isNote = false,
                    name = "eget mauris pharetra et ultrices neque ornare aenean",
                    content = "",
                    color = Color.Green.toArgb()
                ),
                Element(
                    isNote = true,
                    name = "Note title",
                    content = "non quam lacus suspendisse faucibus interdum posuere lorem ipsum dolor sit amet",
                    color = Color.Blue.toArgb()
                ),
                Element(
                    isNote = false,
                    name = "eget mauris pharetra et ultrices neque ornare aenean",
                    content = "",
                    color = Color.Magenta.toArgb()
                )
            )
        )
    }
}

@Preview
@Composable
fun GridViewElementsContentPreview() {
    KeepNoteTheme {
        ElementsOverviewContent(
            isGridView = true,
            contentPadding = PaddingValues(8.dp),
            elements = listOf(
                Element(
                    isNote = true,
                    name = "Note title",
                    content = "Lorem ipsum dolor sit amet",
                    color = Color.Red.toArgb()
                ),
                Element(
                    isNote = false,
                    name = "Folder name",
                    content = "",
                    color = Color.Green.toArgb()
                ),
                Element(
                    isNote = true,
                    name = "Note title",
                    content = "Lorem ipsum dolor sit amet",
                    color = Color.Blue.toArgb()
                ),
                Element(
                    isNote = false,
                    name = "Folder name",
                    content = "",
                    color = Color.Magenta.toArgb()
                )
            )
        )
    }
}