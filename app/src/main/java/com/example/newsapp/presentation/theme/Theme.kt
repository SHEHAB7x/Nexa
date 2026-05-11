package com.example.newsapp.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


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
    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}
