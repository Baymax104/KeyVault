package top.baymaxam.keyvault.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import top.baymaxam.keyvault.R

val robotoFont = FontFamily(
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_bold, FontWeight.Bold),
    Font(R.font.roboto_black, FontWeight.Black)
)

val AppTypography = Typography(
    titleLarge = TextStyle(
        fontSize = 18.sp,
        letterSpacing = 1.sp,
        fontWeight = FontWeight.Black,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    ),
    titleMedium = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 1.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    ),
    bodyLarge = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    ),
    bodySmall = TextStyle(
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    ),
    labelMedium = TextStyle(
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    ),
    displayMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    ),
    displaySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    )
)
