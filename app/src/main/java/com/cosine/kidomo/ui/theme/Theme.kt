package com.cosine.kidomo.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorPalette = darkColors(
    primary = Lavender4,
    primaryVariant = Lavender5,
    secondary = Ocean6
)

private val LightColorPalette = lightColors(
    primary = Lavender4,
    primaryVariant = Lavender5,
    secondary = Ocean6
)

@Composable
fun KidomoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        content = content
    )
}