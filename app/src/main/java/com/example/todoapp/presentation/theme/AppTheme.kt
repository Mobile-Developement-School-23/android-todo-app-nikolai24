package com.example.todoapp.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (!useDarkTheme) {
        LightColors
    } else {
        DarkColors
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}

private val LightColors = lightColorScheme(
    primary = Black,
    secondary = Blue,
    tertiary = Red,
    background = FloralWhite,
    onBackground = White,
    surface = LightGrey
)

private val DarkColors = darkColorScheme(
    primary = White,
    secondary = White,
    tertiary = Red,
    background = Black,
    onBackground = Grey,
    surface = LightGrey
)