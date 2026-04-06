package io.github.sd155.bego.tracker.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.sd155.bego.di.Inject
import io.github.sd155.bego.tracker.domain.PlatformReason
import io.github.sd155.bego.tracker.domain.Tracker
import io.github.sd155.bego.tracker.ui.TrackerView
import io.github.sd155.bego.tracker.ui.TrackerViewIntent
import io.github.sd155.bego.tracker.ui.TrackerViewModel
import io.github.sd155.bego.tracker.ui.TrackerViewState
import io.github.sd155.logs.api.Logger
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
    val viewModel: TrackerViewModel = viewModel {
        TrackerViewModel(
            tracker = Inject.instance<Tracker>(),
            logger = Inject.instance<Logger>(tag = trackerModuleName),
        )
    }
    val state by viewModel.state.collectAsState()
    val platformHooks = Inject.instance<TrackerPlatformHooks>()
    val prerequisites = platformHooks.rememberLocationPrerequisites()
    val emitInitializationIntent: () -> Unit = { viewModel.onViewIntent(TrackerViewIntent.Initialization(prerequisites)) }
    val platformNotReadyContent: (@Composable () -> Unit)? =
        (state as? TrackerViewState.PlatformNotReady)?.let { notReadyState ->
            {
                platformHooks.PlatformNotReadyView(
                    reason = notReadyState.reason,
                    onRetryInitialization = emitInitializationIntent,
                )
            }
        }
    LaunchedEffect(prerequisites) {
        emitInitializationIntent()
    }

    TrackerView(
        state = state,
        onStart = { viewModel.onViewIntent(TrackerViewIntent.Start) },
        onStop = { viewModel.onViewIntent(TrackerViewIntent.Stop) },
        onReset = { viewModel.onViewIntent(TrackerViewIntent.Reset) },
        onRetryInitialization = emitInitializationIntent,
        onSetTarget = { viewModel.onViewIntent(TrackerViewIntent.SetTarget(it)) },
        notReadyContent = platformNotReadyContent,
    )
}

abstract class TrackerPlatformHooks {
    @Composable
    internal abstract fun rememberLocationPrerequisites(): LocationPrerequisites

    @Composable
    internal abstract fun PlatformNotReadyView(
        reason: PlatformReason,
        onRetryInitialization: () -> Unit = {},
    )
}
