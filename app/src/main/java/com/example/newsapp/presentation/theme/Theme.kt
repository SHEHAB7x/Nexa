package com.example.newsapp.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


private val LightColorScheme = lightColorScheme(
    primary        = Primary,
    secondary      = Secondary,
    tertiary       = Tertiary,
    background     = White,
    surface        = White,
    onPrimary      = White,
    onSecondary    = White,
    onBackground   = DarkGray,
    onSurface      = DarkGray,
)

private val DarkColorScheme = darkColorScheme(
    primary        = Primary,
    secondary      = Secondary,
    tertiary       = Tertiary,
    background     = DarkBackground,
    surface        = DarkSurface,
    surfaceVariant = DarkCard,
    onPrimary      = White,
    onSecondary    = White,
    onBackground   = DarkTextPrimary,
    onSurface      = DarkTextPrimary,
    outline        = DarkTextSecondary
)

@Composable
fun NewsAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode){
        SideEffect {
            val window = (view.context as android.app.Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)

            insetsController.isAppearanceLightStatusBars = !darkTheme

            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}
