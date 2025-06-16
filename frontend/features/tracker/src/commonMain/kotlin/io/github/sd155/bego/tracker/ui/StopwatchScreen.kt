package io.github.sd155.bego.tracker.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.sd155.bego.di.Inject
import io.github.sd155.bego.tracker.di.moduleName
import io.github.sd155.logs.api.Logger
import kotlinx.serialization.Serializable

/**
 * Represents the route to the stopwatch screen in the navigation graph.
 */
@Serializable
object StopwatchScreenRoute

/**
 * A composable that displays the stopwatch screen, managing the stopwatch's state and user interactions.
 * This screen integrates the stopwatch view with its view model and handles all stopwatch-related functionality.
 */
@Composable
fun StopwatchScreen() {
    val viewModel: StopwatchViewModel = viewModel { StopwatchViewModel() }
    val state by viewModel.state.collectAsState()
    val logger = Inject.instance<Logger>(tag = moduleName)
    logger.debug("View received state", diagnostics = listOf(state))

    StopwatchView(
        state = state,
        onStart = { viewModel.onViewIntent(StopwatchViewIntent.StartStopwatch) },
        onStop = { viewModel.onViewIntent(StopwatchViewIntent.StopStopwatch) },
        onContinue = { viewModel.onViewIntent(StopwatchViewIntent.ContinueStopwatch) },
        onReset = { viewModel.onViewIntent(StopwatchViewIntent.ResetStopwatch) },
        onNextLap = { viewModel.onViewIntent(StopwatchViewIntent.NextLap) },
    )
}