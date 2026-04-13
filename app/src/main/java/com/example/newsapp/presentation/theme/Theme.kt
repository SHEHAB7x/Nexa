package com.example.newsapp.presentation.theme

import androidx.compose.material3.MaterialTheme
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

@Composable
fun NewsAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography  = Typography,
        content     = content
    )
}
