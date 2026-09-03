package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = ModiSage,
    onPrimary = ModiForestDark,
    primaryContainer = ModiForest,
    onPrimaryContainer = ModiIvory,
    secondary = ModiRose,
    onSecondary = ModiForestDark,
    secondaryContainer = ModiRoseDark,
    onSecondaryContainer = ModiIvory,
    tertiary = ModiSageMedium,
    onTertiary = ModiForestDark,
    background = ModiForestDark,
    onBackground = ModiIvory,
    surface = Color(0xFF22312A),
    onSurface = ModiIvory,
    surfaceVariant = Color(0xFF2E3E36),
    onSurfaceVariant = ModiSageLight,
    outline = ModiSage,
  )

private val LightColorScheme =
  lightColorScheme(
    primary = ModiForest,
    onPrimary = Color.White,
    primaryContainer = ModiSageLight,
    onPrimaryContainer = ModiForest,
    secondary = ModiRose,
    onSecondary = Color.White,
    secondaryContainer = ModiRoseLight,
    onSecondaryContainer = ModiForest,
    tertiary = ModiSage,
    onTertiary = Color.White,
    background = ModiIvory,
    onBackground = ModiCharcoal,
    surface = ModiSurfaceCard,
    onSurface = ModiCharcoal,
    surfaceVariant = ModiSand,
    onSurfaceVariant = ModiTextSecondary,
    outline = ModiBorder,
  )

@Composable
fun ModiTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
