package com.gzaber.keepnote.ui.util.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DetailsHeader(
    title: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        HorizontalDivider(
            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
            thickness = 4.dp,
            color = color
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}