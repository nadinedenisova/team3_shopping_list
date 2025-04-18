package com.example.my_shoplist_application.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(

)

private val LightColorScheme = lightColorScheme(
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

val LocalTypography = staticCompositionLocalOf<CustomTypography> {
    error("No Typography provided")
}

val LocalCustomColor = staticCompositionLocalOf<CustomColor> {
    error("No Typography provided")
//    CustomColor(
//        blueColor = Blue,
//        textColor = Black
//    )
}

@Composable
fun My_ShopList_ApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
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