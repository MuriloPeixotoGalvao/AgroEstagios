package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val VagaGreen: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF2AAE3B) else Color(0xFF006B1A)

val VagaGreenDark: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF005312) else Color(0xFF005312)

val VagaGreenLight: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF8CE08A) else Color(0xFF2AAE3B)

val AccentRose: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFFFC6091) else Color(0xFFFC6091)

val AccentRoseDark: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFFB02459) else Color(0xFFB02459)

val SurfaceBg: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF121212) else Color(0xFFFAFAFA)

val SurfaceDim: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF141414) else Color(0xFFE3E2E6)

val SurfaceBright: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF1E1E1E) else Color(0xFFFAFAFA)

val SurfaceContainerLowest: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF0F0F0F) else Color(0xFFFFFFFF)

val SurfaceContainerLow: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF1A1A1A) else Color(0xFFF3F3F4)

val SurfaceContainer: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF222222) else Color(0xFFEEEEF0)

val SurfaceContainerHigh: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF2A2A2A) else Color(0xFFE8E8EA)

val SurfaceContainerHighest: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF333333) else Color(0xFFE2E2E5)

val OnSurfaceText: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFFE6E1E5) else Color(0xFF1C1B1F)

val OnSurfaceVariantText: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFFCAC4D0) else Color(0xFF49454F)

val InverseSurfaceBg: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFFFAFAFA) else Color(0xFF313033)

val InverseOnSurfaceText: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF1C1B1F) else Color(0xFFE6E1E5)

val OutlineColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF938F99) else Color(0xFF79747E)

val OutlineVariantColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF49454F) else Color(0xFFC4C6D0)

val ErrorColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFFCF6679) else Color(0xFFBA1A1A)

val ErrorContainerColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFFB00020) else Color(0xFFFFDAD6)

val OnErrorColor: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF000000) else Color(0xFFFFFFFF)
