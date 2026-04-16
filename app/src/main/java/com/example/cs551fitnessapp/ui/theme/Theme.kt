package com.example.cs551fitnessapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(

    primary = PrimaryDark,
    secondary = SecondaryDark,

    background = BackgroundDark,
    surface = SurfaceDark,

    onPrimary = SurfaceDark,
    onBackground = TextDark,
    onSurface = TextDark
)

private val LightColorScheme = lightColorScheme(

    primary = PrimaryLight,
    secondary = SecondaryLight,

    background = BackgroundLight,
    surface = SurfaceLight,

    onPrimary = SurfaceLight,
    onBackground = TextLight,
    onSurface = TextLight
)

@Composable
fun CS551FitnessAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colorScheme =
        if (darkTheme) DarkColorScheme
        else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}