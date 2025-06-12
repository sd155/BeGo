package io.github.sd155.bego.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape

class BegoShapes(
    val button: Shape = RoundedCornerShape(percent = 50),
)

internal val LocalBegoShapes = staticCompositionLocalOf<BegoShapes> {
    error("No shapes provided")
}