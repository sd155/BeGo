package io.github.sd155.bego.timer

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.sd155.bego.theme.LOCALE_EN
import io.github.sd155.bego.theme.LOCALE_RU
import io.github.sd155.bego.theme.PHONE_PORT_SPEC
import io.github.sd155.bego.theme.ThemedPreview
import io.github.sd155.bego.timer.ui.TimerView
import io.github.sd155.bego.timer.ui.TimerViewState

@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Composable
private fun InitialTimerPreview() {
    ThemedPreview {
        TimerView(
            state = TimerViewState.Initial
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
                totalTimeCs = 35665L,
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
                totalTimeCs = 155665L,
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
                totalTimeCs = 35665L,
                currentLapTimeCs = 1555L,
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
                totalTimeCs = 155665L,
                currentLapTimeCs = 5551L,
            )
        )
    }
}
