package top.baymaxam.keyvault.ui.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.core.screen.Screen
import top.baymaxam.keyvault.ui.theme.AppTheme

/**
 * 添加页
 * @author John
 * @since 23 6月 2024
 */
class AddScreen : Screen {

    @Composable
    override fun Content() {
        ContentLayout()
    }

    @Composable
    private fun ContentLayout() {
        Text(text = "Add")
    }

    @Preview
    @Composable
    private fun Preview() {
        AppTheme {
            ContentLayout()
        }
    }

}