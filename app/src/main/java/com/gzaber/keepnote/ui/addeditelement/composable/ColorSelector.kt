package com.gzaber.keepnote.ui.addeditelement.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gzaber.keepnote.R
import com.gzaber.keepnote.ui.theme.ColorSelectorColor1
import com.gzaber.keepnote.ui.theme.ColorSelectorColor2
import com.gzaber.keepnote.ui.theme.ColorSelectorColor3
import com.gzaber.keepnote.ui.theme.ColorSelectorColor4
import com.gzaber.keepnote.ui.theme.ColorSelectorColor5
import com.gzaber.keepnote.ui.theme.ColorSelectorColor6
import com.gzaber.keepnote.ui.theme.KeepNoteTheme


@Composable
fun ColorCircle(
    isSelected: Boolean,
    color: Color,
    onClick: (Color) -> Unit,
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
fun ColorSelector(
    onColorSelect: (Color) -> Unit,
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(
        ColorSelectorColor1,
        ColorSelectorColor2,
        ColorSelectorColor3,
        ColorSelectorColor4,
        ColorSelectorColor5,
        ColorSelectorColor6
    ),
    currentColor: Color? = null
) {
    var selectedColor by remember {
        mutableStateOf(currentColor ?: colors.first())
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
        ColorSelector(
            onColorSelect = {}
        )
    }
}