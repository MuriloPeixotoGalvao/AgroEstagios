package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = Color(0xFF2AAE3B),
    onPrimary = Color(0xFF121212),
    primaryContainer = Color(0xFF006B1A),
    onPrimaryContainer = Color.White,
    secondary = Color(0xFF9BD594),
    onSecondary = Color(0xFF121212),
    secondaryContainer = Color(0xFF1F2F1D),
    onSecondaryContainer = Color(0xFFE2F3E1),
    background = Color(0xFF121212),
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF121212),
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF2A2A2A),
    onSurfaceVariant = Color(0xFFCAC4D0),
    outline = Color(0xFF938F99),
    outlineVariant = Color(0xFF49454F)
  )

private val LightColorScheme =
  lightColorScheme(
    primary = Color(0xFF006B1A),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF2AAE3B),
    onPrimaryContainer = Color(0xFF003909),
    secondary = Color(0xFF366A34),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE2F3E1),
    onSecondaryContainer = Color(0xFF1F2F1D),
    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF1C1B1F),
    surface = Color(0xFFFAFAFA),
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFEEEEF0),
    onSurfaceVariant = Color(0xFF49454F),
    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFC4C6D0),
    error = Color(0xFFBA1A1A),
    errorContainer = Color(0xFFFFDAD6),
    onError = Color(0xFFFFFFFF)
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Override dynamic color to false so our brand colors are always shown!
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
