package com.gzaber.keepnote.ui.utils.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gzaber.keepnote.R
import com.gzaber.keepnote.ui.theme.KeepNoteTheme


@Composable
fun TwoButtonsBottomSheetContent(
    firstButtonOnClick: () -> Unit,
    @DrawableRes firstButtonIconRes: Int,
    @StringRes firstButtonContentDescriptionRes: Int,
    @StringRes firstButtonTextRes: Int,
    secondButtonOnClick: () -> Unit,
    @DrawableRes secondButtonIconRes: Int,
    @StringRes secondButtonContentDescriptionRes: Int,
    @StringRes secondButtonTextRes: Int,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier.fillMaxWidth()
    ) {
        Button(onClick = firstButtonOnClick) {
            Icon(
                painter = painterResource(id = firstButtonIconRes),
                contentDescription = stringResource(
                    id = firstButtonContentDescriptionRes
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(id = firstButtonTextRes))
        }
        Button(onClick = secondButtonOnClick) {
            Icon(
                painter = painterResource(id = secondButtonIconRes),
                contentDescription = stringResource(
                    id = secondButtonContentDescriptionRes
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(id = secondButtonTextRes))
        }
    }
}

@Composable
fun CreateElementBottomSheetContent(
    folderButtonOnClick: () -> Unit,
    noteButtonOnClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TwoButtonsBottomSheetContent(
        firstButtonOnClick = folderButtonOnClick,
        firstButtonIconRes = R.drawable.ic_folder,
        firstButtonContentDescriptionRes = R.string.create_folder,
        firstButtonTextRes = R.string.folder,
        secondButtonOnClick = noteButtonOnClick,
        secondButtonIconRes = R.drawable.ic_note,
        secondButtonContentDescriptionRes = R.string.create_note,
        secondButtonTextRes = R.string.note,
        modifier = modifier
    )
}

@Preview
@Composable
fun CreateElementBottomSheetContentPreview() {
    KeepNoteTheme {
        CreateElementBottomSheetContent(
            folderButtonOnClick = {},
            noteButtonOnClick = {}
        )
    }
}

@Composable
fun EditDeleteElementBottomSheetContent(
    editButtonOnClick: () -> Unit,
    deleteButtonOnClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TwoButtonsBottomSheetContent(
        firstButtonOnClick = editButtonOnClick,
        firstButtonIconRes = R.drawable.ic_edit,
        firstButtonContentDescriptionRes = R.string.edit_element,
        firstButtonTextRes = R.string.edit,
        secondButtonOnClick = deleteButtonOnClick,
        secondButtonIconRes = R.drawable.ic_delete,
        secondButtonContentDescriptionRes = R.string.delete_element,
        secondButtonTextRes = R.string.delete,
        modifier = modifier
    )
}

@Preview
@Composable
fun EditDeleteElementBottomSheetContentPreview() {
    KeepNoteTheme {
        EditDeleteElementBottomSheetContent(
            editButtonOnClick = {},
            deleteButtonOnClick = {}
        )
    }
}

@Composable
fun RadioRow(
    selected: Boolean,
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(vertical = 4.dp)
            .selectable(
                selected,
                onClick = onClick,
                role = Role.RadioButton
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text)
    }
}

@Composable
fun FilterBottomSheetContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1F)) {
                Text(text = stringResource(id = R.string.filter_by), fontWeight = FontWeight.Bold)
                RadioRow(
                    selected = true,
                    onClick = { /*TODO*/ },
                    text = stringResource(id = R.string.radio_name)
                )
                RadioRow(
                    selected = false,
                    onClick = { /*TODO*/ },
                    text = stringResource(id = R.string.radio_date)
                )
            }

            Column(modifier = Modifier.weight(1F)) {
                Text(text = stringResource(id = R.string.order), fontWeight = FontWeight.Bold)
                RadioRow(
                    selected = true,
                    onClick = { /*TODO*/ },
                    text = stringResource(id = R.string.radio_asc)
                )
                RadioRow(
                    selected = false,
                    onClick = { /*TODO*/ },
                    text = stringResource(id = R.string.radio_desc)
                )
            }
        }

        Column {
            Text(
                text = stringResource(id = R.string.type_of_elements),
                fontWeight = FontWeight.Bold
            )
            RadioRow(
                selected = true,
                onClick = { /*TODO*/ },
                text = stringResource(id = R.string.radio_folders_first)
            )
            RadioRow(
                selected = false,
                onClick = { /*TODO*/ },
                text = stringResource(id = R.string.radio_notes_first)
            )
            RadioRow(
                selected = false,
                onClick = { /*TODO*/ },
                text = stringResource(id = R.string.radio_none)
            )
        }
    }
}

@Preview
@Composable
fun FilterBottomSheetContentPreview() {
    KeepNoteTheme {
        FilterBottomSheetContent()
    }
}


















