package io.github.sd155.bego.theme

import androidx.compose.runtime.Composable

const val LOCALE_EN = "en"
const val LOCALE_ES = "es"
const val LOCALE_FR = "fr"
const val LOCALE_PTrBR = "pt-rBR"
const val LOCALE_RU = "ru"
const val TAB_LAND_SPEC = "spec:width=1280dp,height=800dp,dpi=320,isRound=false,chinSize=0dp,orientation=landscape,cutout=none,navigation=gesture"
const val TAB_PORT_SPEC = "spec:width=1280dp,height=800dp,dpi=320,isRound=false,chinSize=0dp,orientation=portrait,cutout=none,navigation=gesture"
const val PHONE_LAND_SPEC = "spec:width=393dp,height=851dp,dpi=440,isRound=false,chinSize=0dp,orientation=landscape,cutout=none,navigation=gesture"
const val PHONE_PORT_SPEC = "spec:width=393dp,height=851dp,dpi=440,isRound=false,chinSize=0dp,orientation=portrait,cutout=none,navigation=gesture"
const val TV_SPEC = "spec:width=960dp,height=540dp,dpi=320,isRound=false,chinSize=0dp,orientation=landscape,cutout=none,navigation=gesture"

@Composable
fun ThemedPreview(content: @Composable () -> Unit) {
    BegoTheme(
        screen = screenSize(),
        content = content,
    )
}