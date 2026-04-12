package io.github.sd155.bego.tracker.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.sd155.bego.di.DiTree
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
 *
 * This is the feature app-layer composable. It resolves feature bindings from the provided [DiTree]
 * and keeps the lower UI and domain layers free from direct DI access.
 */
@Composable
fun TrackerScreen(
    diTree: DiTree,
) {
    val viewModel: TrackerViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                require(modelClass == TrackerViewModel::class.java)
                @Suppress("UNCHECKED_CAST")
                return TrackerViewModel(
                    tracker = diTree.instance<Tracker>(),
                    logger = diTree.instance<Logger>(tag = trackerModuleName),
                ) as T
            }
        }
    )
    val state by viewModel.state.collectAsState()
    val hooks = remember { diTree.instance<PlatformHooks>() }
    val prerequisites = hooks.rememberLocationPrerequisites()
    val emitInitializationIntent: () -> Unit = { viewModel.onViewIntent(TrackerViewIntent.Initialization(prerequisites)) }
    val platformNotReadyContent: (@Composable () -> Unit)? =
        (state as? TrackerViewState.PlatformNotReady)?.let { notReadyState ->
            {
                hooks.PlatformNotReadyView(
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

/**
 * Platform integration hooks needed by the tracker screen.
 *
 * This type is public only to allow platform-specific construction and DI wiring from the app module.
 */
abstract class PlatformHooks {
    @Composable
    internal abstract fun rememberLocationPrerequisites(): LocationPrerequisites

    @Composable
    internal abstract fun PlatformNotReadyView(
        reason: PlatformReason,
        onRetryInitialization: () -> Unit = {},
    )
}
