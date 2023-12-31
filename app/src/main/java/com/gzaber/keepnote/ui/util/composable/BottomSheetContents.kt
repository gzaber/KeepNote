package com.gzaber.keepnote.ui.util.composable

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
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    @DrawableRes firstButtonIconRes: Int,
    @StringRes firstButtonContentDescriptionRes: Int,
    @StringRes firstButtonTextRes: Int,
    firstButtonOnClick: () -> Unit,
    @DrawableRes secondButtonIconRes: Int,
    @StringRes secondButtonContentDescriptionRes: Int,
    @StringRes secondButtonTextRes: Int,
    secondButtonOnClick: () -> Unit,
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
        firstButtonOnClick = deleteButtonOnClick,
        firstButtonIconRes = R.drawable.ic_delete,
        firstButtonContentDescriptionRes = R.string.delete_element,
        firstButtonTextRes = R.string.delete,
        secondButtonOnClick = editButtonOnClick,
        secondButtonIconRes = R.drawable.ic_edit,
        secondButtonContentDescriptionRes = R.string.edit_element,
        secondButtonTextRes = R.string.edit,
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
fun BottomSheetRadioGroup(
    @StringRes titleRes: Int,
    @StringRes radioOptions: List<Int>,
    @StringRes selectedOption: Int,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.selectableGroup()
    ) {
        Text(
            text = stringResource(titleRes),
            fontWeight = FontWeight.Bold
        )
        radioOptions.forEach { textRes ->
            RadioRow(
                selected = (textRes == selectedOption),
                onClick = { onOptionSelected(textRes) },
                text = stringResource(id = textRes)
            )
        }
    }
}

@Composable
fun SortBottomSheetContent(
    @StringRes sortRadioOptions: List<Int>,
    @StringRes sortSelectedOption: Int,
    onSortOptionSelected: (Int) -> Unit,
    @StringRes orderRadioOptions: List<Int>,
    @StringRes orderSelectedOption: Int,
    onOrderOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    @StringRes firstElementsRadioOptions: List<Int>? = null,
    @StringRes firstElementsSelectedOption: Int? = null,
    onFirstElementsOptionSelected: ((Int) -> Unit)? = null,
) {
    Column(
        modifier = modifier
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            BottomSheetRadioGroup(
                modifier = Modifier.weight(1F),
                titleRes = R.string.sort_by,
                radioOptions = sortRadioOptions,
                selectedOption = sortSelectedOption,
                onOptionSelected = onSortOptionSelected
            )
            BottomSheetRadioGroup(
                modifier = Modifier.weight(1F),
                titleRes = R.string.order,
                radioOptions = orderRadioOptions,
                selectedOption = orderSelectedOption,
                onOptionSelected = onOrderOptionSelected
            )
        }
        if (firstElementsRadioOptions != null && firstElementsSelectedOption != null &&
            onFirstElementsOptionSelected != null
        ) {
            BottomSheetRadioGroup(
                titleRes = R.string.first_elements,
                radioOptions = firstElementsRadioOptions,
                selectedOption = firstElementsSelectedOption,
                onOptionSelected = onFirstElementsOptionSelected
            )
        }
    }
}

@Preview
@Composable
fun ThreeGroupsSortBottomSheetContentPreview() {
    KeepNoteTheme {
        val sortRadioOptions =
            listOf(R.string.radio_date, R.string.radio_name)
        val (sortSelectedOption, onSortOptionSelected) = remember {
            mutableStateOf(sortRadioOptions.first())
        }
        val orderRadioOptions =
            listOf(R.string.radio_ascending, R.string.radio_descending)
        val (orderSelectedOption, onOrderOptionSelected) = remember {
            mutableStateOf(orderRadioOptions.first())
        }
        val firstElementsRadioOptions =
            listOf(R.string.radio_folders, R.string.radio_notes, R.string.radio_not_applicable)
        val (firstElementsSelectedOption, onFirstElementsOptionSelected) = remember {
            mutableStateOf(firstElementsRadioOptions.first())
        }

        SortBottomSheetContent(
            sortRadioOptions = sortRadioOptions,
            sortSelectedOption = sortSelectedOption,
            onSortOptionSelected = onSortOptionSelected,
            orderRadioOptions = orderRadioOptions,
            orderSelectedOption = orderSelectedOption,
            onOrderOptionSelected = onOrderOptionSelected,
            firstElementsRadioOptions = firstElementsRadioOptions,
            firstElementsSelectedOption = firstElementsSelectedOption,
            onFirstElementsOptionSelected = onFirstElementsOptionSelected
        )
    }
}

@Preview
@Composable
fun TwoGroupsSortBottomSheetContentPreview() {
    KeepNoteTheme {
        val sortRadioOptions =
            listOf(R.string.radio_date, R.string.radio_name)
        val (sortSelectedOption, onSortOptionSelected) = remember {
            mutableStateOf(sortRadioOptions.first())
        }
        val orderRadioOptions =
            listOf(R.string.radio_ascending, R.string.radio_descending)
        val (orderSelectedOption, onOrderOptionSelected) = remember {
            mutableStateOf(orderRadioOptions.first())
        }

        SortBottomSheetContent(
            sortRadioOptions = sortRadioOptions,
            sortSelectedOption = sortSelectedOption,
            onSortOptionSelected = onSortOptionSelected,
            orderRadioOptions = orderRadioOptions,
            orderSelectedOption = orderSelectedOption,
            onOrderOptionSelected = onOrderOptionSelected
        )
    }
}



















