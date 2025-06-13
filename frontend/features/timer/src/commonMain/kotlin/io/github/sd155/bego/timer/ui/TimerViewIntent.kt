package io.github.sd155.bego.timer.ui

internal sealed class TimerViewIntent {
    data object StartTimer : TimerViewIntent()
    data object StopTimer : TimerViewIntent()
    data object NextLap : TimerViewIntent()
    data object ContinueTimer : TimerViewIntent()
    data object ResetTimer : TimerViewIntent()
}