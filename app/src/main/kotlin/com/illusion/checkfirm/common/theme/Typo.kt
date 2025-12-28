package com.illusion.checkfirm.common.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.illusion.checkfirm.R

fun TextStyle.asWantedSans() =
    this.copy(
        fontFamily = FontFamily(Font(R.font.wanted_sans_variable))
    )

private val defaultTypography = Typography()

val WantedSansTypography = Typography(
    displayLarge = defaultTypography.displayLarge.asWantedSans(),
    displayMedium = defaultTypography.displayMedium.asWantedSans(),
    displaySmall = defaultTypography.displaySmall.asWantedSans(),

    headlineLarge = defaultTypography.headlineLarge.asWantedSans(),
    headlineMedium = defaultTypography.headlineMedium.asWantedSans(),
    headlineSmall = defaultTypography.headlineSmall.asWantedSans(),

    titleLarge = defaultTypography.titleLarge.asWantedSans(),
    titleMedium = defaultTypography.titleMedium.asWantedSans(),
    titleSmall = defaultTypography.titleSmall.asWantedSans(),

    bodyLarge = defaultTypography.bodyLarge.asWantedSans(),
    bodyMedium = defaultTypography.bodyMedium.asWantedSans(),
    bodySmall = defaultTypography.bodySmall.asWantedSans(),

    labelLarge = defaultTypography.labelLarge.asWantedSans(),
    labelMedium = defaultTypography.labelMedium.asWantedSans(),
    labelSmall = defaultTypography.labelSmall.asWantedSans(),
)