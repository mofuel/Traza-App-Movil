package com.bancocloud.traza.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val TrazaLightColorScheme = lightColorScheme(
    primary = TrazaColors.BrandPrimary,
    onPrimary = TrazaColors.BrandOnPrimary,
    primaryContainer = TrazaColors.BrandSoft,
    onPrimaryContainer = TrazaColors.TextPrimary,
    inversePrimary = TrazaColors.BrandPrimary,
    secondary = TrazaColors.TextPrimary,
    onSecondary = TrazaColors.TextInverse,
    secondaryContainer = TrazaColors.NeutralBackground,
    onSecondaryContainer = TrazaColors.NeutralText,
    tertiary = TrazaColors.BrandStrong,
    onTertiary = TrazaColors.TextInverse,
    tertiaryContainer = TrazaColors.WarningBackground,
    onTertiaryContainer = TrazaColors.WarningText,
    background = TrazaColors.AppBackground,
    onBackground = TrazaColors.TextPrimary,
    surface = TrazaColors.Surface,
    onSurface = TrazaColors.TextPrimary,
    surfaceVariant = TrazaColors.SurfaceMuted,
    onSurfaceVariant = TrazaColors.TextSecondary,
    surfaceTint = TrazaColors.BrandPrimary,
    inverseSurface = TrazaColors.TextPrimary,
    inverseOnSurface = TrazaColors.TextInverse,
    error = TrazaColors.ErrorText,
    onError = TrazaColors.TextInverse,
    errorContainer = TrazaColors.ErrorBackground,
    onErrorContainer = TrazaColors.ErrorText,
    outline = TrazaColors.Border,
    outlineVariant = TrazaColors.NeutralBorder,
    scrim = TrazaColors.Scrim,
    surfaceBright = TrazaColors.Surface,
    surfaceDim = TrazaColors.AppBackground,
    surfaceContainer = TrazaColors.SurfaceMuted,
    surfaceContainerHigh = TrazaColors.NeutralBackground,
    surfaceContainerHighest = TrazaColors.NeutralBackground,
    surfaceContainerLow = TrazaColors.Surface,
    surfaceContainerLowest = TrazaColors.Surface,
)

/**
 * TRAZA currently has one confirmed light visual system.
 * Dynamic color and a dark theme remain intentionally disabled.
 */
@Composable
fun TrazaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = TrazaLightColorScheme,
        typography = TrazaTypography,
        shapes = TrazaMaterialShapes,
        content = content,
    )
}
