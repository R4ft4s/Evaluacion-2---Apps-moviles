package com.example.gestion_opycorp.ui.theme

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

private val LightColorScheme = lightColorScheme(
    primary = SlateBlue,                         // #4C6793
    onPrimary = Color.White,
    primaryContainer = SlateBlueContainer,
    onPrimaryContainer = OnSlateBlueContainer,

    secondary = DeepPurple,                      // #5C2E7E
    onSecondary = Color.White,
    secondaryContainer = DeepPurpleContainer,
    onSecondaryContainer = OnDeepPurpleContainer,

    tertiary = SoftTealBlue,                     // #8BBCCC
    onTertiary = Color(0xFF072733),
    tertiaryContainer = SoftTealBlueContainer,
    onTertiaryContainer = Color(0xFF001E2B),

    background = LightGrayBackground,
    onBackground = PureBlack,
    surface = LightGraySurface,
    onSurface = PureBlack,
    surfaceVariant = LightGraySurfaceVariant,
    onSurfaceVariant = Color(0xFF3B4350)
)

private val DarkColorScheme = darkColorScheme(
    primary = SlateBlueLight,
    onPrimary = Color(0xFF10213B),
    primaryContainer = SlateBlue,
    onPrimaryContainer = SlateBlueContainer,

    secondary = DeepPurpleLight,
    onSecondary = Color(0xFF230338),
    secondaryContainer = DeepPurple,
    onSecondaryContainer = DeepPurpleContainer,

    tertiary = SoftTealBlue,
    onTertiary = Color(0xFF003547),
    tertiaryContainer = Color(0xFF214E60),
    onTertiaryContainer = SoftTealBlueContainer,

    background = PureBlack,
    onBackground = Color(0xFFE5ECF4),
    surface = DarkGreySurface,
    onSurface = Color(0xFFE5ECF4),
    surfaceVariant = Color(0xFF29303A),
    onSurfaceVariant = Color(0xFFC5CED8)
)

@Composable
fun Gestion_OpYCorpTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
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