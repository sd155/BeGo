package io.github.sd155.bego

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.sd155.bego.di.DiTree
import io.github.sd155.bego.tracker.app.TrackerScreen
import io.github.sd155.bego.tracker.app.TrackerScreenRoute

internal val LocalNavHost = staticCompositionLocalOf<NavHostController> { error("No default implementation") }

@Composable
internal fun AppNavGraph(
    diTree: DiTree,
) {
    val navController: NavHostController = rememberNavController()
    CompositionLocalProvider(LocalNavHost provides navController) {
        NavHost(
            navController = navController,
            startDestination = TrackerScreenRoute
        ) {
            composable<TrackerScreenRoute> {
                TrackerScreen(diTree)
            }
        }
    }
}
