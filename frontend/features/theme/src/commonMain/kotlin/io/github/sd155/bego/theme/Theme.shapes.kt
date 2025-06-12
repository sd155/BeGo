package io.github.sd155.bego.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape

/**
 * Shape definitions for the Bego application.
 * Defines the shapes used for UI elements.
 *
 * @property button Shape used for buttons, defaults to a fully rounded corner shape.
 */
class BegoShapes(
    val button: Shape = RoundedCornerShape(percent = 50),
)

internal val LocalBegoShapes = staticCompositionLocalOf<BegoShapes> {
    error("No shapes provided")
}