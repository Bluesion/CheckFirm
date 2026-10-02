package com.illusion.checkfirm.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.illusion.checkfirm.core.designsystem.R

// Use the XML app's exact static fonts and weight aliases to preserve text metrics.
private val wantedSans = FontFamily(
    Font(R.font.wanted_sans_regular, FontWeight.W100),
    Font(R.font.wanted_sans_regular, FontWeight.W200),
    Font(R.font.wanted_sans_regular, FontWeight.W300),
    Font(R.font.wanted_sans_medium, FontWeight.W400),
    Font(R.font.wanted_sans_medium, FontWeight.W500),
    Font(R.font.wanted_sans_medium, FontWeight.W600),
    Font(R.font.wanted_sans_semibold, FontWeight.W700),
    Font(R.font.wanted_sans_semibold, FontWeight.W800),
    Font(R.font.wanted_sans_bold, FontWeight.W900),
)

fun TextStyle.asWantedSans() = copy(fontFamily = wantedSans)

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

// Material XML TextAppearances inherit android:textColorPrimary rather than colorOnSurface.
fun Typography.withXmlTextColor(color: Color) = copy(
    displayLarge = displayLarge.copy(color = color),
    displayMedium = displayMedium.copy(color = color),
    displaySmall = displaySmall.copy(color = color),
    headlineLarge = headlineLarge.copy(color = color),
    headlineMedium = headlineMedium.copy(color = color),
    headlineSmall = headlineSmall.copy(color = color),
    titleLarge = titleLarge.copy(color = color),
    titleMedium = titleMedium.copy(color = color),
    titleSmall = titleSmall.copy(color = color),
    bodyLarge = bodyLarge.copy(color = color),
    bodyMedium = bodyMedium.copy(color = color),
    bodySmall = bodySmall.copy(color = color),
)
