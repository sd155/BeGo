package io.github.sd155.bego

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.sd155.bego.di.DiTree
import io.github.sd155.bego.history.app.HistoryDockItem
import io.github.sd155.bego.history.app.HistoryScreen
import io.github.sd155.bego.history.app.HistoryScreenRoute
import io.github.sd155.bego.theme.BegoTheme
import io.github.sd155.bego.theme.FloatingDock
import io.github.sd155.bego.tracker.app.TrackerDockItem
import io.github.sd155.bego.tracker.app.TrackerScreen
import io.github.sd155.bego.tracker.app.TrackerScreenRoute

internal val LocalNavHost = staticCompositionLocalOf<NavHostController> { error("No default implementation") }

private enum class TopLevelDestination {
    Tracker,
    History,
}

@Composable
internal fun AppNavGraph(
    diTree: DiTree,
) {
    val navController: NavHostController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val selectedDestination = remember(currentDestination) {
        when {
            currentDestination?.hierarchy?.any { it.hasRoute(TrackerScreenRoute::class) } == true ->
                TopLevelDestination.Tracker
            currentDestination?.hierarchy?.any { it.hasRoute(HistoryScreenRoute::class) } == true ->
                TopLevelDestination.History
            else -> TopLevelDestination.Tracker
        }
    }

    CompositionLocalProvider(LocalNavHost provides navController) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BegoTheme.palette.background),
        ) {
            NavHost(
                navController = navController,
                startDestination = TrackerScreenRoute,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = BegoTheme.sizes.dockReservedHeight),
            ) {
                composable<TrackerScreenRoute> {
                    TrackerScreen(diTree)
                }
                composable<HistoryScreenRoute> {
                    HistoryScreen()
                }
            }
            FloatingDock {
                TrackerDockItem(
                    selected = selectedDestination == TopLevelDestination.Tracker,
                    onClick = { navigateToTopLevel(navController, destination = TopLevelDestination.Tracker) },
                )
                HistoryDockItem(
                    selected = selectedDestination == TopLevelDestination.History,
                    onClick = { navigateToTopLevel(navController, destination = TopLevelDestination.History) },
                )
            }
        }
    }
}

private fun navigateToTopLevel(
    navController: NavHostController,
    destination: TopLevelDestination,
) {
    when (destination) {
        TopLevelDestination.Tracker ->
            navController.navigate(TrackerScreenRoute) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        TopLevelDestination.History ->
            navController.navigate(HistoryScreenRoute) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
    }
}
