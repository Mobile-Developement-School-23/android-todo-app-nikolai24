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
        typography = typography,
        content = content
    )
}

private val LightColors = lightColorScheme(
    primary = Black,
    secondary = Blue,
    tertiary = Red,
    surface = LightGrey,
    onSurface = Black,
    background = FloralWhite,
    onBackground = White,
    onPrimary = Blue,
    onSecondary = LightBlue,
    onTertiary = White
)

private val DarkColors = darkColorScheme(
    primary = White,
    secondary = White,
    tertiary = Red,
    surface = LightGrey,
    onSurface = White,
    background = Black,
    onBackground = Grey,
    onPrimary = Blue,
    onSecondary = LightBlue,
    onTertiary = White
)