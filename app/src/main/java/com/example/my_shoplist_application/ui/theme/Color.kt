package com.example.my_shoplist_application.ui.theme

import androidx.compose.ui.graphics.Color
import com.example.my_shoplist_application.R

val Blue = Color(0xFF007AFF)
val Red = Color(0xFFFF3B30)
val Pink80 = Color(0xFFEFB8C8)
val backgroundLight = Color(0xFFFFFFFF)
val White = Color(0xFFFFFFFF)
val Black = Color(0XFF000000)

val backgroundDark = Color(0xFF1C1C1E)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)


data class CustomColor(
    val textColor: Color,
    val blueColor: Color,
    val background: Color,
    val white: Color
//    val background:Color,
//    val blueColor:Color
)

val LightCustomColor = CustomColor(
    blueColor = Blue,
    textColor = Black,
    background = White,
    white = White
)

val DarkCustomColor = CustomColor(
    blueColor = Blue,
    textColor = White,
    background = backgroundDark,
    white = White
)