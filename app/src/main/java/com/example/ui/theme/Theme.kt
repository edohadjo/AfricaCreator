package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFB59B),
    onPrimary = Color(0xFF5B1A00),
    primaryContainer = Color(0xFF812800),
    onPrimaryContainer = Color(0xFFFFDBCD),
    secondary = Color(0xFFFFDC97),
    onSecondary = Color(0xFF432C00),
    secondaryContainer = Color(0xFF604100),
    onSecondaryContainer = Color(0xFFFFDEA3),
    tertiary = Color(0xFF8CD8A7),
    onTertiary = Color(0xFF003822),
    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFD4C3B7),
    outline = DarkOutline
)

private val LightColorScheme = lightColorScheme(
    primary = AfricaTerracotta,
    onPrimary = Color.White,
    primaryContainer = AfricaTerracottaContainer,
    onPrimaryContainer = AfricaOnTerracottaContainer,
    secondary = AfricaGold,
    onSecondary = Color.White,
    secondaryContainer = AfricaGoldContainer,
    onSecondaryContainer = AfricaOnGoldContainer,
    tertiary = AfricaForestGreen,
    onTertiary = Color.White,
    background = LightBackground,
    onBackground = LightOnSurface,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = Color(0xFF51443B),
    outline = LightOutline
)

@Composable
fun AfricaCreatorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep brand identity prominent
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
