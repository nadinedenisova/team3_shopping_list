package com.example.my_shoplist_application.presentation.ui.theme

import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color

val Blue = Color(0xFF007AFF)
val White = Color(0xFFFFFFFF)
val Black = Color(0XFF000000)
val LightGrey = Color(0XFFE1E2E2)
val Gray = Color(0XFFAAAAAA)
val Rad = Color(0xFFFF3B30)
val BackgroundDark = Color(0xFF1C1C1E)
val LabelColorLight = Color(0xFF808083)
val LabelColorNight = Color(0xFF98989F)
val ColorUncheckedTrackColorNight = Color(0xFF39393D)
val ColorUncheckedTrackColorLight = Color(0xFFE9E9EA)
val ColorTrackTintCheck = Color(0xFF34C759)
val TextColorCrossed = Color(0xFFAAAAAA)
val CheckBoxColorLight = Color(0xFF9DD8EC)
val CheckBoxColorNight = Color(0xFF7DD0F4)


data class CustomColor(
    val textColor: Color,
    val blueColor: Color,
    val background: Color,
    val white: Color,
    val grey: Color,
    val red: Color,
    val textColorWhiteGrey: Color,
    val textColorLabe: Color,
    val colorTrackTint: Color,
    val ucheckedTrackColor: Color,
    val colorTrackTintCheck: Color,
    val textColorCrossed: Color,
    val checkBoxColor: Color,
    val textColorWhiteBlue: Color
)

val LightCustomColor = CustomColor(
    blueColor = Blue,
    textColor = Black,
    background = White,
    white = White,
    grey = LightGrey,
    red = Rad,
    textColorWhiteGrey = White,
    textColorLabe = LabelColorLight,
    colorTrackTint = White,
    ucheckedTrackColor = ColorUncheckedTrackColorLight,
    colorTrackTintCheck = ColorTrackTintCheck,
    textColorCrossed = TextColorCrossed,
    checkBoxColor = CheckBoxColorLight,
    textColorWhiteBlue = White
)

val DarkCustomColor = CustomColor(
    blueColor = Blue,
    textColor = White,
    background = BackgroundDark,
    white = White,
    grey = LightGrey,
    red = Rad,
    textColorWhiteGrey = Gray,
    textColorLabe = LabelColorNight,
    colorTrackTint = White,
    ucheckedTrackColor = ColorUncheckedTrackColorNight,
    colorTrackTintCheck = ColorTrackTintCheck,
    textColorCrossed = TextColorCrossed,
    checkBoxColor = CheckBoxColorNight,
    textColorWhiteBlue = Blue
)