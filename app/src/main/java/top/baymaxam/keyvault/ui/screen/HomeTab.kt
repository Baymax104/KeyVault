package top.baymaxam.keyvault.ui.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

/**
 * 首页
 * @author John
 * @since 22 6月 2024
 */
object HomeTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(image = Icons.Filled.Home)
            return remember {
                TabOptions(
                    index = 0u,
                    title = "首页",
                    icon = icon
                )
            }
        }

    private fun readResolve(): Any = HomeTab

    @Composable
    override fun Content() {
        ContentLayout()
    }

    @Composable
    private fun ContentLayout() {
        Text(text = "Hello")
    }
}
