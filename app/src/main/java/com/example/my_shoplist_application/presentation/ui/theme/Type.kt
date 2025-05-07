package com.example.my_shoplist_application.presentation.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

data class CustomTypography(
    val h1: TextStyle,
    val h3: TextStyle,
    val h2: TextStyle
)
val Typography = CustomTypography(
    h1 = TextStyle(
        fontFamily = sfProDisplayFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,
        lineHeight = 41.sp,
        letterSpacing = 0.37.sp
    ),
    h2 = TextStyle(
        fontFamily = sfProDisplayFontFamily /*FontFamily(Font(R.font.sf_pro_display_regular))*/,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.41).sp
    ),
    h3 = TextStyle(
        fontFamily = sfProDisplayFontFamily /*FontFamily(Font(R.font.sf_pro_display_regular))*/,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.41).sp
    )
)
