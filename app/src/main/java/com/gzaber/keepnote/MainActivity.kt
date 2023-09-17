package com.gzaber.keepnote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.gzaber.keepnote.ui.elementsoverview.ElementsOverviewScreen
import com.gzaber.keepnote.ui.theme.KeepNoteTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KeepNoteTheme {
                ElementsOverviewScreen()
            }
        }
    }
}