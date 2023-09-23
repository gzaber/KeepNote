package com.gzaber.keepnote.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gzaber.keepnote.ui.addeditelement.AddEditElementScreen
import com.gzaber.keepnote.ui.elementsoverview.ElementsOverviewScreen
import com.gzaber.keepnote.ui.navigation.KeepNoteDestinationArgs.ELEMENT_ID_ARG
import com.gzaber.keepnote.ui.navigation.KeepNoteDestinationArgs.IS_NOTE_ARG

@Composable
fun KeepNoteNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = KeepNoteDestinations.ELEMENTS_OVERVIEW_ROUTE,
    navActions: KeepNoteNavigationActions = remember(navController) {
        KeepNoteNavigationActions(navController)
    }
) {
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(
            route = KeepNoteDestinations.ELEMENTS_OVERVIEW_ROUTE,
        ) {
            ElementsOverviewScreen(
                onCreateElement = { isNote ->
                    navActions.navigateToAddEditElement(isNote, null)
                },
                onUpdateElement = { isNote, id ->
                    navActions.navigateToAddEditElement(isNote, id)
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
            AddEditElementScreen()

        }
    }
}