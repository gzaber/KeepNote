package com.gzaber.keepnote.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gzaber.keepnote.ui.addeditelement.AddEditElementScreen
import com.gzaber.keepnote.ui.elementsoverview.ElementsOverviewScreen
import com.gzaber.keepnote.ui.navigation.KeepNoteDestinationArgs.ELEMENT_ID_ARG
import com.gzaber.keepnote.ui.navigation.KeepNoteDestinationArgs.IS_NOTE_ARG
import com.gzaber.keepnote.ui.navigation.KeepNoteDestinationArgs.NOTE_ID_ARG
import com.gzaber.keepnote.ui.notedetails.NoteDetailsScreen

@Composable
fun KeepNoteNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = KeepNoteDestinations.ELEMENTS_OVERVIEW_ROUTE,
    navActions: KeepNoteNavigationActions = remember(navController) {
        KeepNoteNavigationActions(navController)
    }
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(
            route = KeepNoteDestinations.ELEMENTS_OVERVIEW_ROUTE,
        ) {
            ElementsOverviewScreen(
                onElementClick = { isNote, id ->
                    if (isNote) {
                        navActions.navigateToNoteDetails(id.toString())
                    }
                },
                onCreateElement = { isNote ->
                    navActions.navigateToAddEditElement(isNote, null)
                },
                onUpdateElement = { isNote, id ->
                    navActions.navigateToAddEditElement(isNote, id.toString())
                }
            )
        }

        composable(
            route = KeepNoteDestinations.ADD_EDIT_ELEMENT_ROUTE,
            arguments = listOf(
                navArgument(IS_NOTE_ARG) { type = NavType.BoolType },
                navArgument(ELEMENT_ID_ARG) { type = NavType.StringType; nullable = true }
            )
        ) {
            AddEditElementScreen(
                onBackClick = { navController.popBackStack() },
                onSaved = { navController.popBackStack() }
            )

        }

        composable(
            route = KeepNoteDestinations.NOTE_DETAILS_ROUTE,
            arguments = listOf(
                navArgument(NOTE_ID_ARG) { type = NavType.StringType }
            )
        ) {
            NoteDetailsScreen(
                onBackClick = { navController.popBackStack() },
                onShareClick = { }
            )
        }
    }
}