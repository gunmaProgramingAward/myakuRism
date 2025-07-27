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
    myakuRismuBackgroundColor = Color(0xFFF9FAFB),
    myakuRismuCardColor = Color(0xFFEDEDED),
    healthDetailMoveThemeColor = Color(0xFFFF1F61),
    healthDetailMoveDistanceThemeColor = Color(0xFFFFC100),
    healthDetailHeartRateThemeColor = Color(0xFFFF4AA3),
    healthDetailSleepThemeColor = Color(0xFF00C3E0),
    healthDetailWalkThemeColor = Color(0xFF0ECB9C),
    healthDetailPeriodTabColor = Color(0xFFE8E8E8),
    healthDetailSelectedPeriodTabColor = Color(0xFFF5F5F5),
    settingScreenBackgroundColor = Color(0xFFF9FAFB),
    settingScreenCommonColor = Color(0x991F2937),
    onSelectedButtonOverlay = Color(0x1A4F378B),
    bottomNavigationBarBackgroundColor = Color(0xFFEEEEEE),
    bottomNavigationBarSelectedColor = Color(0xFFFF1F61),
    bottomNavigationBarUnSelectedColor = Color(0xFF474747),
    settingScreenNormalTextColor = Color(0x801F2937),
    appTextColor = Color(0xFF000000),
    musicDetailMiniMusicPlayerBackgroundColor = Color(0xFFFFFFFF),
    musicDetailMusicPlayerFavorite = Color(0xFFFF1F61),
    musicDetailMusicPlayerShuffle = Color(0xFF1DD95D),
    musicDetailSettingTarget = Color(0xFF2B80CB),
    disabledBackgroundColor = Color(0xAAF8F8F8),
    buttonBackgroundColor = Color(0xFFF7F8F9),
    homeLowBpmColor = Color(0xFFA6E5EE),
    homeMediumBpmColor = Color(0xFFF9B4DA),
    homeHighBpmColor = Color(0xFFFDA5BF),
    homeHeartRateBarColorFaded = Color(0x4DFF1F61),
    homeMoveBarColorFaded = Color(0x4DFF4AA3),
    homeWalkBarColorFaded = Color(0x4D0ECB9C),
    homeMoveDistanceBarColorFaded = Color(0x4DFFC100),
    homeSleepBarColorFaded = Color(0x4D00C3E0),
    homeLowBpmRippleColor = Color(0xFF3CC9DF),
    homeMediumBpmRippleColor = Color(0xFFF686C1),
    homeHighBpmRippleColor = Color(0xFFFF5F8E),
    switchCheckedThumbColor = Color(0xFFE2E2E2),
    switchUncheckedThumbColor = Color(0xFF777777),
    switchCheckedTrackColor = Color(0xFF1F2937),
    switchUncheckedTrackColor = Color(0xFFE2E2E2)
)

val DarkCustomTheme = CustomTheme(
    myakuRismuBackgroundColor = Color(0xFFF9FAFB),
    myakuRismuCardColor = Color(0xFFEDEDED),
    healthDetailMoveThemeColor = Color(0xFFFF1F61),
    healthDetailMoveDistanceThemeColor = Color(0xFFFFC100),
    healthDetailHeartRateThemeColor = Color(0xFFFF4AA3),
    healthDetailSleepThemeColor = Color(0xFF00C3E0),
    healthDetailWalkThemeColor = Color(0xFF0ECB9C),
    healthDetailPeriodTabColor = Color(0xFFE8E8E8),
    healthDetailSelectedPeriodTabColor = Color(0xFFF5F5F5),
    settingScreenBackgroundColor = Color(0xFFF9FAFB),
    settingScreenCommonColor = Color(0x991F2937),
    onSelectedButtonOverlay = Color(0x1A4F378B),
    bottomNavigationBarBackgroundColor = Color(0xFFEEEEEE),
    bottomNavigationBarSelectedColor = Color(0xFFFF1F61),
    bottomNavigationBarUnSelectedColor = Color(0xFF474747),
    settingScreenNormalTextColor = Color(0x801F2937),
    appTextColor = Color(0xFF000000),
    musicDetailMiniMusicPlayerBackgroundColor = Color(0xFFFFFFFF),
    musicDetailMusicPlayerFavorite = Color(0xFFFF1F61),
    musicDetailMusicPlayerShuffle = Color(0xFF1DD95D),
    musicDetailSettingTarget = Color(0xFF2B80CB),
    disabledBackgroundColor = Color(0xAAF8F8F8),
    buttonBackgroundColor = Color(0xFFF7F8F9),
    homeLowBpmColor = Color(0xFFA6E5EE),
    homeMediumBpmColor = Color(0xFFF9B4DA),
    homeHighBpmColor = Color(0xFFFDA5BF),
    homeHeartRateBarColorFaded = Color(0x4DFF1F61),
    homeMoveBarColorFaded = Color(0x4DFF4AA3),
    homeWalkBarColorFaded = Color(0x4D0ECB9C),
    homeMoveDistanceBarColorFaded = Color(0x4DFFC100),
    homeSleepBarColorFaded = Color(0x4D00C3E0),
    homeLowBpmRippleColor = Color(0xFF3CC9DF),
    homeMediumBpmRippleColor = Color(0xFFF686C1),
    homeHighBpmRippleColor = Color(0xFFFF5F8E),
    switchCheckedThumbColor = Color(0xFFE2E2E2),
    switchUncheckedThumbColor = Color(0xFF777777),
    switchCheckedTrackColor = Color(0xFF1F2937),
    switchUncheckedTrackColor = Color(0xFFE2E2E2)
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