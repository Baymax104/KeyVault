package top.baymaxam.keyvault.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import top.baymaxam.keyvault.ui.component.FillIconDefaults

val primaryLight = Color(0xff1e90ff)
val onPrimaryLight = Color(0xFFFFFFFF)
val primaryContainerLight = Color(0xFF0075D6)
val onPrimaryContainerLight = Color(0xFFFFFFFF)
val secondaryLight = Color(0xFF415F8A)
val onSecondaryLight = Color(0xFFFFFFFF)
val secondaryContainerLight = Color(0xFFC4DBFF)
val onSecondaryContainerLight = Color(0xFF275797)
val tertiaryLight = Color(0xFF7C259B)
val onTertiaryLight = Color(0xFFFFFFFF)
val tertiaryContainerLight = Color(0xFFA54FC3)
val onTertiaryContainerLight = Color(0xFFFFFFFF)
val errorLight = Color(0xFFBA1A1A)
val onErrorLight = Color(0xFFFFFFFF)
val errorContainerLight = Color(0xFFFFDAD6)
val onErrorContainerLight = Color(0xFF410002)
val backgroundLight = Color(0xfffbfbfb)
val onBackgroundLight = Color(0xFF181C22)
val surfaceLight = Color(0xFFFFFFFF)
val onSurfaceLight = Color(0xFF181C22)
val surfaceContainerLight = Color(0xffe7f0ff)
val surfaceVariantLight = Color(0xffe7f0ff)
val onSurfaceVariantLight = Color(0xFF404753)
val outlineLight = Color(0xFF707785)
val outlineVariantLight = Color(0xFFC0C7D6)
val scrimLight = Color(0xFF000000)
val inverseSurfaceLight = Color(0xFF2D3138)
val inverseOnSurfaceLight = Color(0xFFEEF0FA)
val inversePrimaryLight = Color(0xFFA5C8FF)

val primaryDark = Color(0xFFA5C8FF)
val onPrimaryDark = Color(0xFF00315F)
val primaryContainerDark = Color(0xFF0075D6)
val onPrimaryContainerDark = Color(0xFFFFFFFF)
val secondaryDark = Color(0xFFAAC8F9)
val onSecondaryDark = Color(0xFF0C3159)
val secondaryContainerDark = Color(0xFF204069)
val onSecondaryContainerDark = Color(0xFFBDD5FF)
val tertiaryDark = Color(0xFFEEB0FF)
val onTertiaryDark = Color(0xFF53006F)
val tertiaryContainerDark = Color(0xFFA54FC3)
val onTertiaryContainerDark = Color(0xFFFFFFFF)
val errorDark = Color(0xFFFFB4AB)
val onErrorDark = Color(0xFF690005)
val errorContainerDark = Color(0xFF93000A)
val onErrorContainerDark = Color(0xFFFFDAD6)
val backgroundDark = Color(0xFF10141A)
val onBackgroundDark = Color(0xFFE0E2EC)
val surfaceDark = Color(0xff1c232e)
val onSurfaceDark = Color(0xFFE0E2EC)
val surfaceContainerDark = Color(0xFF404753)
val surfaceVariantDark = Color(0xFF404753)
val onSurfaceVariantDark = Color(0xFFC0C7D6)
val outlineDark = Color(0xFF8A919F)
val outlineVariantDark = Color(0xFF404753)
val scrimDark = Color(0xFF000000)
val inverseSurfaceDark = Color(0xFFE0E2EC)
val inverseOnSurfaceDark = Color(0xFF2D3138)
val inversePrimaryDark = Color(0xFF005FAF)


val MaterialTheme.outlinedTextFieldColor: TextFieldColors
    @Composable
    get() = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = colorScheme.background,
        focusedContainerColor = colorScheme.background,
        unfocusedLeadingIconColor = colorScheme.onBackground,
        focusedLeadingIconColor = colorScheme.primary,
    )

object IconColors {

    val CatalogKey
        @Composable
        get() = FillIconDefaults.colors(
            iconColor = Color(0xff11834f),
            backgroundColor = Color(0xffd5ffd3)
        )

    val CatalogTag
        @Composable
        get() = FillIconDefaults.colors(
            iconColor = Color(0xffe78529),
            backgroundColor = Color(0xffffe8d3)
        )

    val WebItem
        @Composable
        get() = FillIconDefaults.colors(
            iconColor = Color(0xff1e90ff),
            backgroundColor = Color(0xffd9ecff)
        )

    val CardItem
        @Composable
        get() = FillIconDefaults.colors(
            iconColor = Color(0xFF673AB7),
            backgroundColor = Color(0xffede3ff)
        )

    val AuthItem
        @Composable
        get() = FillIconDefaults.colors(
            iconColor = Color(0xFFF57C00),
            backgroundColor = Color(0xffffecdb)
        )
}

