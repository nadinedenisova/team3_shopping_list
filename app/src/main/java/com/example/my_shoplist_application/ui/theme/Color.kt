package com.example.my_shoplist_application.ui.theme

import androidx.compose.ui.graphics.Color

val Blue = Color(0xFF007AFF)
val White = Color(0xFFFFFFFF)
val Black = Color(0XFF000000)
val LightGrey = Color(0XFFE1E2E2)
val Gray = Color(0XFFAAAAAA)
val BackgroundDark = Color(0xFF1C1C1E)
val LabelColor=Color(0xFF3C3C4399)



data class CustomColor(
    val textColor: Color,
    val blueColor: Color,
    val background: Color,
    val white: Color,
    val grey: Color,
    val textColorWhiteGrey: Color,
val textColorLabe: Color
//    val blueColor:Color
)

val LightCustomColor = CustomColor(
    blueColor = Blue,
    textColor = Black,
    background = White,
    white = White,
    grey = LightGrey,
    textColorWhiteGrey = White,
    textColorLabe = LabelColor
)

val DarkCustomColor = CustomColor(
    blueColor = Blue,
    textColor = White,
    background = BackgroundDark,
    white = White,
    grey = LightGrey,
    textColorWhiteGrey = Gray,
    textColorLabe = LabelColor
)