package top.baymaxam.keyvault.ui.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import top.baymaxam.keyvault.ui.theme.AppTheme

/**
 * 设置页
 * @author John
 * @since 23 6月 2024
 */
object SettingsTab : Tab {

    override val key: ScreenKey
        get() = "Setting-Tab"

    override val options: TabOptions
        @Composable get() {
            val icon = rememberVectorPainter(image = Icons.Filled.Settings)
            return remember { TabOptions(index = 1u, title = "设置", icon = icon) }
        }

    private fun readResolve(): Any = SettingsTab

    @Composable
    override fun Content() {
        ContentLayout()
    }
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
