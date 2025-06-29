package com.example.myaku_rismu.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

)

val LightCustomTheme = CustomTheme(
    moveThemeColor = Color(0xFFFF1F61),
    musicDetailPeriodTabColor = Color(0xFFE8E8E8),
    musicDetailSelectedPeriodTabColor = Color(0xFFF5F5F5),
    settingScreenTextColor = Color(0xFF1F2937),
    settingScreenOnSurfaceColor = Color(0xFFFFFFFF),
    settingScreenSurfaceColor = Color(0xFFF9FAFB),
    settingScreenCommonColor = Color(0x991F2937)
)

val DarkCustomTheme = CustomTheme(
    moveThemeColor = Color(0xFFFF1F61),
    musicDetailPeriodTabColor = Color(0xFFE8E8E8),
    musicDetailSelectedPeriodTabColor = Color(0xFFF5F5F5),
    settingScreenTextColor = Color(0xFF1F2937),
    settingScreenOnSurfaceColor = Color(0xFFFFFFFF),
    settingScreenSurfaceColor = Color(0xFFF9FAFB),
    settingScreenCommonColor = Color(0x991F2937)
)

val LocalCustomTheme = staticCompositionLocalOf {
    LightCustomTheme
}

val MaterialTheme.customTheme: CustomTheme
    @Composable
    @ReadOnlyComposable
    get() = LocalCustomTheme.current

@Composable
fun Myaku_rismuTheme(
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

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}