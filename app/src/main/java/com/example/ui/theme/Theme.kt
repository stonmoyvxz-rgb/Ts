package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = RadiantRose,
    secondary = BrightGold,
    tertiary = MutedSlate,
    background = DeepObsidian,
    surface = DarkLeather,
    onPrimary = DeepObsidian,
    onSecondary = DeepObsidian,
    onTertiary = LightParchment,
    onBackground = LightParchment,
    onSurface = LightParchment,
    primaryContainer = SoftWine,
    onPrimaryContainer = LightParchment
)

private val LightColorScheme = lightColorScheme(
    primary = CrimsonRay,
    secondary = DullGold,
    tertiary = SlateBrown,
    background = AntiqueWhite,
    surface = ParchmentCream,
    onPrimary = OffWhite,
    onSecondary = OffWhite,
    onTertiary = InkCharcoal,
    onBackground = InkCharcoal,
    onSurface = InkCharcoal,
    primaryContainer = SoftRose,
    onPrimaryContainer = CrimsonRay
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Set to false by default to showcase our gorgeous Bengali Literary brand colors!
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
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
