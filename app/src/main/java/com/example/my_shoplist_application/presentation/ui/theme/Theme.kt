package com.example.my_shoplist_application.presentation.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext


val LocalTypography = staticCompositionLocalOf<CustomTypography> {
    error("No Typography provided")
}

val LocalCustomColor = staticCompositionLocalOf<CustomColor> {
    error("No Typography provided")

}

@Composable
fun My_ShopList_ApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkCustomColor
        else -> LightCustomColor
    }

    val customColors = if (darkTheme) DarkCustomColor else LightCustomColor

    CompositionLocalProvider(
        LocalTypography provides Typography,
        LocalCustomColor provides customColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme as ColorScheme,
            content = content
        )
    }


}