package com.gzaber.keepnote.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.gzaber.keepnote.ui.navigation.KeepNoteDestinationArgs.ELEMENT_ID_ARG
import com.gzaber.keepnote.ui.navigation.KeepNoteDestinationArgs.IS_NOTE_ARG
import com.gzaber.keepnote.ui.navigation.KeepNoteDestinationArgs.NOTE_ID_ARG
import com.gzaber.keepnote.ui.navigation.KeepNoteScreens.ADD_EDIT_ELEMENT_SCREEN
import com.gzaber.keepnote.ui.navigation.KeepNoteScreens.ELEMENTS_OVERVIEW_SCREEN
import com.gzaber.keepnote.ui.navigation.KeepNoteScreens.NOTE_DETAILS_SCREEN

private object KeepNoteScreens {
    const val ELEMENTS_OVERVIEW_SCREEN = "elementsOverview"
    const val ADD_EDIT_ELEMENT_SCREEN = "addEditElement"
    const val NOTE_DETAILS_SCREEN = "noteDetails"
}

object KeepNoteDestinationArgs {
    const val ELEMENT_ID_ARG = "elementId"
    const val IS_NOTE_ARG = "isNote"
    const val NOTE_ID_ARG = "noteId"
}

object KeepNoteDestinations {
    const val ELEMENTS_OVERVIEW_ROUTE = "$ELEMENTS_OVERVIEW_SCREEN"
    const val ADD_EDIT_ELEMENT_ROUTE =
        "$ADD_EDIT_ELEMENT_SCREEN/{$IS_NOTE_ARG}?$ELEMENT_ID_ARG={$ELEMENT_ID_ARG}"
    const val NOTE_DETAILS_ROUTE = "$NOTE_DETAILS_SCREEN/{$NOTE_ID_ARG}"
}

class KeepNoteNavigationActions(private val navController: NavController) {

    fun navigateToElementsOverview() {
        navController.navigate(KeepNoteDestinations.ELEMENTS_OVERVIEW_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToAddEditElement(isNote: Boolean, elementId: String?) {
        navController.navigate(
            "$ADD_EDIT_ELEMENT_SCREEN/$isNote".let {
                if (elementId != null) "$it?$ELEMENT_ID_ARG=$elementId" else it
            }
        )
    }

    fun navigateToNoteDetails(noteId: String) {
        navController.navigate("$NOTE_DETAILS_SCREEN/$noteId")
    }
}