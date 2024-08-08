package top.baymaxam.keyvault.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.ui.theme.AppTheme

/**
 * 条目信息页
 * @author John
 * @since 08 8月 2024
 */
data class ItemInfoScreen(val item: KeyItem) : Screen {

    override val key: ScreenKey
        get() = "Item-Info-Screen-${item.hashCode()}"

    @Composable
    override fun Content() {

    }

}


@Composable
private fun ContentLayout(
    item: KeyItem = KeyItem()
) {

}


@Preview
@Composable
private fun Preview() {
    AppTheme {
        ContentLayout()
    }
}

