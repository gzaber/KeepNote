package com.gzaber.keepnote.ui.util.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gzaber.keepnote.R
import com.gzaber.keepnote.ui.theme.KeepNoteTheme

@Composable
fun KeepNoteFloatingActionButton(
    @StringRes contentDescription: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Filled.Add
) {
    FloatingActionButton(
        shape = CircleShape,
        containerColor = MaterialTheme.colorScheme.primary,
        elevation = FloatingActionButtonDefaults.elevation(0.dp),
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            icon,
            contentDescription = stringResource(id = contentDescription)
        )
    }
}

@Preview
@Composable
fun KeepNoteFloatingActionButtonPreview() {
    KeepNoteTheme {
        KeepNoteFloatingActionButton(
            onClick = { },
            contentDescription = R.string.create_note
        )
    }
}