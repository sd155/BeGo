package io.github.sd155.bego.tracker

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.sd155.bego.theme.LOCALE_EN
import io.github.sd155.bego.theme.LOCALE_RU
import io.github.sd155.bego.theme.PHONE_PORT_SPEC
import io.github.sd155.bego.theme.ThemedPreview
import io.github.sd155.bego.tracker.ui.UiFormatter

private fun format(timeMs: Long = 0L) = UiFormatter().format(timeMs)

@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Composable
private fun InitialStopwatchPreview() {
    ThemedPreview {
//        StopwatchView(
//            state = StopwatchViewState.Initial(format())
//        )
    }
}

@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Composable
private fun RunningNoLapsStopwatchPreview() {
    ThemedPreview {
//        StopwatchView(
//            state = StopwatchViewState.RunningNoLaps(
//                totalTime = format(timeMs = 35665L),
//            )
//        )
    }
}

@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Composable
private fun StoppedNoLapsStopwatchPreview() {
    ThemedPreview {
//        StopwatchView(
//            state = StopwatchViewState.StoppedNoLaps(
//                totalTime = format(timeMs = 155665L),
//            )
//        )
    }
}

@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Composable
private fun RunningWithLapsStopwatchPreview() {
    ThemedPreview {
//        StopwatchView(
//            state = StopwatchViewState.RunningWithLaps(
//                totalTime = format(timeMs = 35665L),
//                currentLapTime = format(timeMs = 1555L),
//            )
//        )
    }
}

@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Preview(showSystemUi = false, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_RU)
@Preview(showSystemUi = false, uiMode = UI_MODE_NIGHT_YES, device = PHONE_PORT_SPEC, locale = LOCALE_EN)
@Composable
private fun StoppedWithLapsStopwatchPreview() {
    ThemedPreview {
//        StopwatchView(
//            state = StopwatchViewState.StoppedWithLaps(
//                totalTime = format(timeMs = 155665L),
//                currentLapTime = format(timeMs = 5551L),
//            )
//        )
    }
}
