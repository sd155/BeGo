package io.github.sd155.bego.tracker.ui

internal sealed class StopwatchViewIntent {
    data object StartStopwatch : StopwatchViewIntent()
    data object StopStopwatch : StopwatchViewIntent()
    data object NextLap : StopwatchViewIntent()
    data object ContinueStopwatch : StopwatchViewIntent()
    data object ResetStopwatch : StopwatchViewIntent()
}