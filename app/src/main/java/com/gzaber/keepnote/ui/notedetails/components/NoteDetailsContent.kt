package com.gzaber.keepnote.ui.notedetails.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gzaber.keepnote.data.repository.model.Note
import com.gzaber.keepnote.ui.theme.KeepNoteTheme

@Composable
fun NoteDetailsContent(
    note: Note,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(contentPadding)
    ) {
        Divider(
            thickness = 4.dp,
            color = Color(note.color)
        )
        Text(
            text = note.title,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(text = note.content)
    }
}

@Preview
@Composable
fun NoteDetailsContentPreview() {
    KeepNoteTheme {
        NoteDetailsContent(
            note = Note(
                title = "Volutpat diam ut venenatis tellus",
                content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ipsum dolor sit amet consectetur adipiscing elit ut aliquam. Aenean vel elit scelerisque mauris pellentesque pulvinar. Sed blandit libero volutpat sed cras ornare. Malesuada fames ac turpis egestas integer.",
                color = Color.Green.toArgb()
            ),
            contentPadding = PaddingValues(16.dp)
        )
    }
}