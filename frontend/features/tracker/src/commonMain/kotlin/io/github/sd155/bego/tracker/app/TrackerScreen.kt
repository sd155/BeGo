package io.github.sd155.bego.tracker.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.sd155.bego.di.Inject
import io.github.sd155.bego.tracker.ui.TrackerView
import io.github.sd155.bego.tracker.ui.TrackerViewIntent
import io.github.sd155.bego.tracker.ui.TrackerViewModel
import io.github.sd155.bego.utils.Result
import kotlinx.coroutines.launch
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
    val prerequisites = Inject.instance<PlatformTrackerRememberer>().rememberLocationPrerequisites()
    val scope = rememberCoroutineScope()

    TrackerView(
        state = state,
        onStart = {
            scope.launch {
                if (prerequisites.ensureReady() is Result.Success) {
                    viewModel.onViewIntent(TrackerViewIntent.Start)
                }
            }
        },
        onStop = { viewModel.onViewIntent(TrackerViewIntent.Stop) },
        onReset = { viewModel.onViewIntent(TrackerViewIntent.Reset) },
        onSetTarget = { viewModel.onViewIntent(TrackerViewIntent.SetTarget(it)) },
    )
}

abstract class PlatformTrackerRememberer {
    @Composable
    internal abstract fun rememberLocationPrerequisites(): LocationPrerequisites
}
