package io.github.sd155.bego.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
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
 * A filled button component with accent color that follows the Bego design system.
 * This button is based on Material 3 Filled Button and uses the accent color scheme.
 * Use this button for primary actions that need to stand out.
 *
 * @param modifier The modifier to be applied to the button.
 * @param onClick The callback to be invoked when the button is clicked.
 * @param label The text to be displayed on the button.
 * @param enabled Whether the button is enabled for interaction.
 */
@Composable
fun BegoAccentFilledButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: String,
    enabled: Boolean = true,
) = TextButton(
    modifier = modifier
        .width(BegoTheme.sizes.buttonWidth),
    onClick = onClick,
    label = label,
    icon = null,
    enabled = enabled,
    buttonColors = ButtonDefaults.buttonColors(
        backgroundColor = BegoTheme.palette.accent,
        disabledBackgroundColor = BegoTheme.palette.accent,
        contentColor = BegoTheme.palette.onAccent,
        disabledContentColor = BegoTheme.palette.onAccent,
    ),
)

/**
 * A filled button component with warning color that follows the Bego design system.
 * This button is based on Material 3 Filled Button and uses the warning color scheme.
 * Use this button for destructive or cautionary actions.
 *
 * @param modifier The modifier to be applied to the button.
 * @param onClick The callback to be invoked when the button is clicked.
 * @param label The text to be displayed on the button.
 * @param enabled Whether the button is enabled for interaction.
 */
@Composable
fun BegoWarningFilledButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: String,
    enabled: Boolean = true,
) = TextButton(
    modifier = modifier
        .width(BegoTheme.sizes.buttonWidth),
    onClick = onClick,
    label = label,
    icon = null,
    enabled = enabled,
    buttonColors = ButtonDefaults.buttonColors(
        backgroundColor = BegoTheme.palette.warning,
        disabledBackgroundColor = BegoTheme.palette.warning,
        contentColor = BegoTheme.palette.onAccent,
        disabledContentColor = BegoTheme.palette.onAccent,
    ),
)

/**
 * A filled button component with primary color that follows the Bego design system.
 * This button is based on Material 3 Filled Button and uses the primary color scheme.
 * Use this button for standard actions that don't need special emphasis.
 *
 * @param modifier The modifier to be applied to the button.
 * @param onClick The callback to be invoked when the button is clicked.
 * @param label The text to be displayed on the button.
 * @param enabled Whether the button is enabled for interaction.
 */
@Composable
fun BegoPrimaryFilledButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    label: String,
    enabled: Boolean = true,
) = TextButton(
    modifier = modifier
        .width(BegoTheme.sizes.buttonWidth),
    onClick = onClick,
    label = label,
    icon = null,
    enabled = enabled,
    buttonColors = ButtonDefaults.buttonColors(
        backgroundColor = BegoTheme.palette.primary,
        disabledBackgroundColor = BegoTheme.palette.primary,
        contentColor = BegoTheme.palette.background,
        disabledContentColor = BegoTheme.palette.background,
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
            modifier = Modifier
                .padding(vertical = BegoTheme.sizes.paddingVertical),
            text = label,
            style = textStyle ?: BegoTheme.typography.label,
            maxLines = 1,
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

/**
 * A header text component that follows the Bego design system.
 * This component is used for main headings and titles.
 *
 * @param text The text content to be displayed.
 */
@Composable
fun BegoHeaderText(
    text: String,
) {
    Text(
        modifier = Modifier
            .padding(
                horizontal = BegoTheme.sizes.paddingHorizontal,
                vertical = BegoTheme.sizes.paddingVertical,
            ),
        text = text,
        color = BegoTheme.palette.primary,
        style = BegoTheme.typography.header,
    )
}

/**
 * A large body text component that follows the Bego design system.
 * This component is used for main content text with larger size.
 *
 * @param text The text content to be displayed.
 */
@Composable
fun BegoBodyLargeText(
    text: String,
) {
    Text(
        modifier = Modifier
            .padding(
                horizontal = BegoTheme.sizes.paddingHorizontal,
                vertical = BegoTheme.sizes.paddingVertical,
            ),
        text = text,
        color = BegoTheme.palette.secondary,
        style = BegoTheme.typography.bodyL,
    )
}