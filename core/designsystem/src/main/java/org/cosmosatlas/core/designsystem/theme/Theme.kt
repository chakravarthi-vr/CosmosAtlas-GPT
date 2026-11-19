package org.cosmosatlas.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val CosmosColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = SpaceBlack,
    primaryContainer = SpaceSurfaceVariant,
    onPrimaryContainer = NeonCyan,
    secondary = StellarAmber,
    onSecondary = SpaceBlack,
    secondaryContainer = SpaceSurfaceVariant,
    onSecondaryContainer = StellarAmber,
    tertiary = DeepViolet,
    onTertiary = TextPrimary,
    background = SpaceBlack,
    onBackground = TextPrimary,
    surface = SpaceDarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = SpaceCardSurface,
    onSurfaceVariant = TextSecondary,
    outline = SpaceBorder,
    error = SupernovaRed,
    onError = SpaceBlack
)

@Composable
fun CosmosAtlasTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CosmosColorScheme,
        typography = CosmosTypography,
        content = content
    )
}
