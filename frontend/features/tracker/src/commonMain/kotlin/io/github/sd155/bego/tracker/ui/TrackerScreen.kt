package io.github.sd155.bego.tracker.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.serialization.Serializable

@Serializable
object TrackerScreenRoute

@Composable
fun TrackerScreen() {
    val viewModel: TrackerViewModel = viewModel { TrackerViewModel() }
    val state by viewModel.state.collectAsState()

    TrackerView(
        state = state,
        onStart = { viewModel.onViewIntent(TrackerViewIntent.Start) },
        onStop = { viewModel.onViewIntent(TrackerViewIntent.Stop) },
        onReset = { viewModel.onViewIntent(TrackerViewIntent.Reset) },
    )
    LaunchedEffect(Unit) {
        viewModel.onViewIntent(TrackerViewIntent.Prepare)
    }
}