package io.github.sd155.bego

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.sd155.bego.tracker.ui.TrackerScreen
import io.github.sd155.bego.tracker.ui.TrackerScreenRoute

internal val LocalNavHost = staticCompositionLocalOf<NavHostController> { error("No default implementation") }

@Composable
internal fun AppNavGraph() {
    val navController: NavHostController = rememberNavController()
    CompositionLocalProvider(
        LocalNavHost provides navController
    ) {
        NavHost(
            navController = navController,
            startDestination = TrackerScreenRoute
        ) {
            composable<TrackerScreenRoute> {
                TrackerScreen()
            }
        }
    }
}