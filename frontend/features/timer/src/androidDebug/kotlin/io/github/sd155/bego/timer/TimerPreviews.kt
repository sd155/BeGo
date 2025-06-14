package io.github.sd155.bego.timer

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.sd155.bego.theme.LOCALE_EN
import io.github.sd155.bego.theme.LOCALE_RU
import io.github.sd155.bego.theme.PHONE_PORT_SPEC
import io.github.sd155.bego.theme.ThemedPreview
import io.github.sd155.bego.timer.ui.TimeFormatter
import io.github.sd155.bego.timer.ui.TimerView
import io.github.sd155.bego.timer.ui.TimerViewState

private fun format(timeMs: Long = 0L) = TimeFormatter().format(timeMs)

@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Composable
private fun InitialTimerPreview() {
    ThemedPreview {
        TimerView(
            state = TimerViewState.Initial(format())
        )
    }
}

@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Composable
private fun RunningNoLapsTimerPreview() {
    ThemedPreview {
        TimerView(
            state = TimerViewState.RunningNoLaps(
                totalTime = format(timeMs = 35665L),
            )
        )
    }
}

@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Composable
private fun StoppedNoLapsTimerPreview() {
    ThemedPreview {
        TimerView(
            state = TimerViewState.StoppedNoLaps(
                totalTime = format(timeMs = 155665L),
            )
        )
    }
}

@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Composable
private fun RunningWithLapsTimerPreview() {
    ThemedPreview {
        TimerView(
            state = TimerViewState.RunningWithLaps(
                totalTime = format(timeMs = 35665L),
                currentLapTime = format(timeMs = 1555L),
            )
        )
    }
}

@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Composable
private fun StoppedWithLapsTimerPreview() {
    ThemedPreview {
        TimerView(
            state = TimerViewState.StoppedWithLaps(
                totalTime = format(timeMs = 155665L),
                currentLapTime = format(timeMs = 5551L),
            )
        )
    }
}
