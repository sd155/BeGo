package io.github.sd155.bego

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

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
            startDestination = StartScreenRoute
        ) {
            composable<StartScreenRoute> {
                //TODO: Not Implemented
            }
        }
    }
}

//debug
@SuppressLint("MissingSerializableAnnotation")
private object StartScreenRoute