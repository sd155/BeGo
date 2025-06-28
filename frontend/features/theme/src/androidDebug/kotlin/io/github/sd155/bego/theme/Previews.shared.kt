/**
 * This file contains shared preview utilities and constants for Android Studio previews.
 * It provides device specifications for different screen sizes and orientations,
 * locale constants for internationalization testing, and a themed preview composable
 * that applies the Bego theme to preview content.
 */
package io.github.sd155.bego.theme

import androidx.compose.runtime.Composable

/** English locale identifier */
const val LOCALE_EN = "en"
/** Spanish locale identifier */
const val LOCALE_ES = "es"
/** French locale identifier */
const val LOCALE_FR = "fr"
/** Brazilian Portuguese locale identifier */
const val LOCALE_PTrBR = "pt-rBR"
/** Russian locale identifier */
const val LOCALE_RU = "ru"

/** Device specification for tablet in landscape orientation */
const val TAB_LAND_SPEC = "spec:width=1280dp,height=800dp,dpi=320,isRound=false,chinSize=0dp,orientation=landscape,cutout=none,navigation=gesture"
/** Device specification for tablet in portrait orientation */
const val TAB_PORT_SPEC = "spec:width=1280dp,height=800dp,dpi=320,isRound=false,chinSize=0dp,orientation=portrait,cutout=none,navigation=gesture"
/** Device specification for phone in landscape orientation */
const val PHONE_LAND_SPEC = "spec:width=393dp,height=851dp,dpi=440,isRound=false,chinSize=0dp,orientation=landscape,cutout=none,navigation=gesture"
/** Device specification for phone in portrait orientation */
const val PHONE_PORT_SPEC = "spec:width=393dp,height=851dp,dpi=440,isRound=false,chinSize=0dp,orientation=portrait,cutout=none,navigation=gesture"
/** Device specification for TV */
const val TV_SPEC = "spec:width=960dp,height=540dp,dpi=320,isRound=false,chinSize=0dp,orientation=landscape,cutout=none,navigation=gesture"

/**
 * A preview composable that wraps content with the Bego theme.
 * This is used for previewing composables in Android Studio with the correct theme applied.
 * 
 * @param content The composable content to be previewed with the theme applied
 */
@Composable
fun ThemedPreview(content: @Composable () -> Unit) {
    BegoTheme(
        screen = screenSize(),
        platformIcons = AndroidPlatformIcons(),
        content = content,
    )
}