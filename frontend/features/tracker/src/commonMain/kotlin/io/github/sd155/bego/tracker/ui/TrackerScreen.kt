package io.github.sd155.bego.tracker.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.serialization.Serializable

/**
 * Navigation route object for the tracker screen.
 * Used with navigation libraries to identify the tracker feature destination.
 */
@Serializable
object TrackerScreenRoute

/**
 * Main entry point composable for the tracker feature.
 * Displays the tracker UI and handles user interaction.
 */
@Composable
fun TrackerScreen() {
    val viewModel: TrackerViewModel = viewModel { TrackerViewModel() }
    val state by viewModel.state.collectAsState()

    TrackerView(
        state = state,
        onStart = { viewModel.onViewIntent(TrackerViewIntent.Start) },
        onStop = { viewModel.onViewIntent(TrackerViewIntent.Stop) },
        onReset = { viewModel.onViewIntent(TrackerViewIntent.Reset) },
        onSetTarget = { viewModel.onViewIntent(TrackerViewIntent.SetTarget(it)) },
    )
}