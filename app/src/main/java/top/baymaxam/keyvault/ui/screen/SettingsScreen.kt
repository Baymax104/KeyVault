package top.baymaxam.keyvault.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.util.LocalNavigator
import top.baymaxam.keyvault.util.currentOrThrow

/**
 * 设置页
 * @author John
 * @since 23 6月 2024
 */
@Composable
fun SettingScreen() {
    val navigator = LocalNavigator.currentOrThrow
    ContentLayout()
}

@Composable
private fun ContentLayout() {

}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        ContentLayout()
    }
}
