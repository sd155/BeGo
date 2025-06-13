package io.github.sd155.bego.timer.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.serialization.Serializable

@Serializable
object TimerScreenRoute

@Composable
fun TimerScreen() {
    val viewModel: TimerViewModel = viewModel { TimerViewModel() }
    val state by viewModel.state.collectAsState()

    TimerView(
        state = state,
        onStart = { viewModel.onViewIntent(TimerViewIntent.StartTimer) },
        onStop = { viewModel.onViewIntent(TimerViewIntent.StopTimer) },
        onContinue = { viewModel.onViewIntent(TimerViewIntent.ContinueTimer) },
        onReset = { viewModel.onViewIntent(TimerViewIntent.ResetTimer) },
        onNextLap = { viewModel.onViewIntent(TimerViewIntent.NextLap) },
    )
}