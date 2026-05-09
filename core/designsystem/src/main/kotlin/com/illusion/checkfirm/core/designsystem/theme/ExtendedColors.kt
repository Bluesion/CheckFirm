package com.illusion.checkfirm.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class ExtendedColors(
    val toolbarText: Color,
    val toolbarIconTint: Color,
    val settingsDescription: Color,
    val tipCardBackground: Color,
    val tipText: Color,
    val aboutPageButtonText: Color,
    val aboutPageButtonBackground: Color,
    val aboutPageButtonRipple: Color,
    val searchAddButtonBackground: Color,
    val sherlockWarning: Color,
    val sherlockFail: Color,
    val sherlockNormal: Color,
    val sherlockSuccess: Color,
    val sherlockLoading: Color,
    val menuPopupBackground: Color,
    val menuPopupStroke: Color,
    val dialogBackground: Color,
)

val LightExtendedColors = ExtendedColors(
    toolbarText = Color(0xFF262628),
    toolbarIconTint = Color(0xFF010101),
    settingsDescription = Color(0xFF848386),
    tipCardBackground = Color(0xFFE3EAF0),
    tipText = Color(0xFF0074D4),
    aboutPageButtonText = Color(0xFF252525),
    aboutPageButtonBackground = Color(0xFFE1E1E1),
    aboutPageButtonRipple = Color(0xFFDFDFDF),
    searchAddButtonBackground = Color(0xFFE9E9EC),
    sherlockWarning = Color(0xFFFF9B17),
    sherlockFail = Color(0xFFBA1B1B),
    sherlockNormal = Color(0xFF0381FE),
    sherlockSuccess = Color(0xFF8DD483),
    sherlockLoading = Color(0xFFC1C1C1),
    menuPopupBackground = Color(0xFFFCFCFC),
    menuPopupStroke = Color(0xFFFCFCFC),
    dialogBackground = Color(0xFFFCFCFF),
)

val DarkExtendedColors = ExtendedColors(
    toolbarText = Color(0xFFE5E5E5),
    toolbarIconTint = Color(0xFFFAFAFA),
    settingsDescription = Color(0xFF959597),
    tipCardBackground = Color(0xFF053053),
    tipText = Color(0xFF038BFB),
    aboutPageButtonText = Color(0xFFFAFAFA),
    aboutPageButtonBackground = Color(0xFF515151),
    aboutPageButtonRipple = Color(0xFF404040),
    searchAddButtonBackground = Color(0xFF2D2D30),
    sherlockWarning = Color(0xFFFF9B17),
    sherlockFail = Color(0xFFFFB4A9),
    sherlockNormal = Color(0xFF0381FE),
    sherlockSuccess = Color(0xFF8DD483),
    sherlockLoading = Color(0xFFC1C1C1),
    menuPopupBackground = Color(0xFF252525),
    menuPopupStroke = Color(0xFF252525),
    dialogBackground = Color(0xFF17171A),
)

val LocalExtendedColors = staticCompositionLocalOf { LightExtendedColors }
