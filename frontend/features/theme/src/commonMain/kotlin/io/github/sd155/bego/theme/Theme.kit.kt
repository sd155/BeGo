package io.github.sd155.bego.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle

/**
 * A filled button component that follows the Bego design system.
 * This button is based on Material 3 Filled Button.
 *
 * @param modifier The modifier to be applied to the button.
 * @param onClick The callback to be invoked when the button is clicked.
 * @param label The text to be displayed on the button.
 * @param contentColor The color of the button's content (text and icons).
 * @param backgroundColor The background color of the button.
 * @param enabled Whether the button is enabled for interaction.
 */
@Composable
fun BegoFilledButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: String,
    contentColor: Color = BegoTheme.palette.onAccent,
    backgroundColor: Color = BegoTheme.palette.accent,
    enabled: Boolean = true,
) = TextButton(
    modifier = modifier,
    onClick = onClick,
    label = label,
    icon = null,
    enabled = enabled,
    buttonColors = ButtonDefaults.buttonColors(
        backgroundColor = backgroundColor,
        disabledBackgroundColor = backgroundColor,
        contentColor = contentColor,
        disabledContentColor = contentColor,
    ),
)

@Composable
private fun TextButton(
    modifier: Modifier,
    onClick: () -> Unit,
    label: String,
    icon: Painter? = null,
    tailIcon: Painter? = null,
    enabled: Boolean = true,
    buttonColors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = Color.Transparent,
        disabledBackgroundColor = Color.Transparent,
        contentColor = BegoTheme.palette.primary,
        disabledContentColor = BegoTheme.palette.secondary,
    ),
    border: BorderStroke? = null,
    shape: Shape = BegoTheme.shapes.button,
    textStyle: TextStyle? = null,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        elevation = null,
        colors = buttonColors,
        border = border,
        shape = shape,
    ) {
        icon?.let { painter ->
            Icon(
                modifier = Modifier
                    .size(BegoTheme.sizes.icon),
                painter = painter,
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(BegoTheme.sizes.paddingHorizontal))
        }
        Text(
            text = label,
            style = textStyle ?: BegoTheme.typography.label,
        )
        tailIcon?.let { painter ->
            Spacer(modifier = Modifier.width(BegoTheme.sizes.paddingHorizontal))
            Icon(
                modifier = Modifier
                    .size(BegoTheme.sizes.icon),
                painter = painter,
                contentDescription = null,
            )
        }
    }
}