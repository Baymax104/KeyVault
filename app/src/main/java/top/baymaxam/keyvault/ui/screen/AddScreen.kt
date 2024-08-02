package top.baymaxam.keyvault.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.TabNavigator
import top.baymaxam.keyvault.ui.theme.AppTheme

/**
 * 添加页
 * @author John
 * @since 23 6月 2024
 */
class AddScreen : Screen {

    @Composable
    override fun Content() {
        TabNavigator(tab = AddInputTab())
    }

}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {

    }
}
