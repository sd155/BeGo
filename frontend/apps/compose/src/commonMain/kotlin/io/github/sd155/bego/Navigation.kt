package io.github.sd155.bego

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.sd155.bego.timer.ui.TimerScreen
import io.github.sd155.bego.timer.ui.TimerScreenRoute

internal val LocalNavHost = staticCompositionLocalOf<NavHostController> { error("No default implementation") }

@Composable
internal fun AppNavGraph() {
    val navController: NavHostController = rememberNavController()
    CompositionLocalProvider(
        LocalNavHost provides navController
    ) {
        val appName = LocalPlatform.current.appName
        val appVersion = LocalPlatform.current.appVersion
        NavHost(
            navController = navController,
            startDestination = TimerScreenRoute
        ) {
            composable<TimerScreenRoute> {
                TimerScreen()
            }
        }
    }
}