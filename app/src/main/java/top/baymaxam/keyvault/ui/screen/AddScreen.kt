package top.baymaxam.keyvault.ui.screen

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition

/**
 * 添加页
 * @author John
 * @since 23 6月 2024
 */
class AddScreen : Screen {

    override val key: ScreenKey
        get() = "Add-Screen"

    @Composable
    override fun Content() {
        Navigator(AddInputScreen()) { navigator ->
            SlideTransition(navigator)
        }
    }

}
