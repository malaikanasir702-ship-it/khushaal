package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = TealLight,
    onPrimary = TealDeepest,
    primaryContainer = TealDarker,
    onPrimaryContainer = TealBorder,
    secondary = AmberBrand,
    onSecondary = Color.Black,
    tertiary = RoseAlert,
    background = SlateDarkBg,
    surface = Color(0xFF1E293B),
    onBackground = Color.White,
    onSurface = Color.White,
  )

private val LightColorScheme =
  lightColorScheme(
    primary = TealPrimary,
    onPrimary = Color.White,
    primaryContainer = TealSurface,
    onPrimaryContainer = TealDarker,
    secondary = AmberDark,
    onSecondary = Color.White,
    secondaryContainer = AmberLight,
    onSecondaryContainer = AmberDarker,
    tertiary = RoseAlert,
    onTertiary = Color.White,
    tertiaryContainer = RoseLight,
    onTertiaryContainer = RoseDark,
    background = CanvasCream,
    surface = SurfaceCard,
    onBackground = SlateTextPrimary,
    onSurface = SlateTextPrimary,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = SlateTextSecondary,
    outline = SlateBorder,
    outlineVariant = SlateBorderSubtle,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Preserve brand identity
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

@Composable
fun KhushhaalTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  MyApplicationTheme(darkTheme = darkTheme, dynamicColor = false, content = content)
}

