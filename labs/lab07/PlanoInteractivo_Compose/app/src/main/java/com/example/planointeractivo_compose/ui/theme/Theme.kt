package com.example.planointeractivo_compose.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightWineColorScheme = lightColorScheme(
    primary = WinePrimary,
    onPrimary = WineOnPrimary,
    primaryContainer = WinePrimaryContainer,
    onPrimaryContainer = WineOnPrimaryContainer,
    secondary = WineSecondary,
    onSecondary = WineOnSecondary,
    secondaryContainer = WineSecondaryContainer,
    onSecondaryContainer = WineOnSecondaryContainer,
    tertiary = WineTertiary,
    onTertiary = WineOnTertiary,
    tertiaryContainer = WineTertiaryContainer,
    onTertiaryContainer = WineOnTertiaryContainer,
    background = WineNeutralBackground,
    onBackground = WineNeutralOnBackground,
    surface = WineNeutralSurface,
    onSurface = WineNeutralOnSurface,
    surfaceVariant = WineSurfaceVariant,
    onSurfaceVariant = WineOnSurfaceVariant,
    outline = WineOutline,
    outlineVariant = WineOutlineVariant
)

private val DarkWineColorScheme = darkColorScheme(
    primary = WinePrimaryDark,
    onPrimary = WineOnPrimaryDark,
    primaryContainer = WinePrimaryContainerDark,
    onPrimaryContainer = WineOnPrimaryContainerDark,
    secondary = WineSecondaryDark,
    onSecondary = WineOnSecondaryDark,
    secondaryContainer = WineSecondaryContainerDark,
    onSecondaryContainer = WineOnSecondaryContainerDark,
    background = WineBackgroundDark,
    onBackground = WineOnBackgroundDark,
    surface = WineSurfaceDark,
    onSurface = WineOnSurfaceDark,
    surfaceVariant = WineSurfaceVariantDark,
    onSurfaceVariant = WineOnSurfaceVariantDark,
    outline = WineOutline,
    outlineVariant = WineOutlineVariant
)

/**
 * Tema principal de la aplicación con identidad visual basada en tonos vino (Burgundy / Wine)
 * y neutros equilibrados para máxima legibilidad, elegancia y accesibilidad.
 */
@Composable
fun PlanoInteractivo_ComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Desactivado por defecto para preservar la identidad vino institucional
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkWineColorScheme
        else -> LightWineColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}