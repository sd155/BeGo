package io.github.sd155.bego.timer.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.sd155.bego.di.Inject
import io.github.sd155.logs.api.Logger
import kotlinx.serialization.Serializable

/**
 * Represents the route to the timer screen in the navigation graph.
 */
@Serializable
object TimerScreenRoute

/**
 * A composable that displays the timer screen, managing the timer's state and user interactions.
 * This screen integrates the timer view with its view model and handles all timer-related functionality.
 */
@Composable
fun TimerScreen() {
    val viewModel: TimerViewModel = viewModel { TimerViewModel() }
    val state by viewModel.state.collectAsState()
    val logger = Inject.instance<Logger>()
    logger.debug("View received state", diagnostics = listOf(state))

    TimerView(
        state = state,
        onStart = { viewModel.onViewIntent(TimerViewIntent.StartTimer) },
        onStop = { viewModel.onViewIntent(TimerViewIntent.StopTimer) },
        onContinue = { viewModel.onViewIntent(TimerViewIntent.ContinueTimer) },
        onReset = { viewModel.onViewIntent(TimerViewIntent.ResetTimer) },
        onNextLap = { viewModel.onViewIntent(TimerViewIntent.NextLap) },
    )
}