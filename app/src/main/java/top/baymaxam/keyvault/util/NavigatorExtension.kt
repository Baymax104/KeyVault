package top.baymaxam.keyvault.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow

/**
 * NavigatorExtension
 * @author John
 * @since 18 9月 2024
 */

val ProvidableCompositionLocal<Navigator?>.root: Navigator
    @Composable
    get() {
        var current = currentOrThrow
        while (current.level != 0) {
            current = current.parent ?: error("CompositionLocal is null")
        }
        return current
    }
