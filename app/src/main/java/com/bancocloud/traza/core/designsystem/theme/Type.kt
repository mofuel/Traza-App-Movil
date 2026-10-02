package com.bancocloud.traza.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val TrazaSans = FontFamily.SansSerif

val TrazaTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = TrazaSans,
        fontWeight = FontWeight.Bold,
        fontSize = 42.sp,
        lineHeight = 43.sp,
        letterSpacing = (-1.89).sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = TrazaSans,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 38.sp,
        letterSpacing = (-0.64).sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = TrazaSans,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.6).sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = TrazaSans,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 29.sp,
        letterSpacing = (-0.48).sp,
    ),
    titleLarge = TextStyle(
        fontFamily = TrazaSans,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = (-0.33).sp,
    ),
    titleMedium = TextStyle(
        fontFamily = TrazaSans,
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
        lineHeight = 24.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = TrazaSans,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        lineHeight = 21.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = TrazaSans,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        lineHeight = 29.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = TrazaSans,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 24.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = TrazaSans,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = TrazaSans,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 15.sp,
        lineHeight = 20.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = TrazaSans,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        lineHeight = 18.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = TrazaSans,
        fontWeight = FontWeight.Black,
        fontSize = 11.sp,
        lineHeight = 13.sp,
        letterSpacing = 0.5.sp,
    ),
)

object TrazaTextStyles {
    val Numeric = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    )
}
