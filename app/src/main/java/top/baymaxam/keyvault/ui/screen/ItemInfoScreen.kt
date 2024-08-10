package top.baymaxam.keyvault.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.KeyType
import top.baymaxam.keyvault.ui.component.FillIcon
import top.baymaxam.keyvault.ui.component.TopBackBar
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.ui.theme.IconColors

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
    item: KeyItem = KeyItem(),
    onBack: () -> Unit = {},
) {
    Scaffold(
        topBar = { TopBackBar(title = "条目详情", onBack = onBack) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                FillIcon(
                    icon = when (item.type) {
                        KeyType.Website -> Icons.Rounded.Language
                        KeyType.Card -> Icons.Rounded.CreditCard
                        KeyType.Authorization -> Icons.Rounded.Person
                    },
                    modifier = Modifier.size(60.dp),
                    shape = RoundedCornerShape(20),
                    colors = when (item.type) {
                        KeyType.Website -> IconColors.WebItem
                        KeyType.Card -> IconColors.CardItem
                        KeyType.Authorization -> IconColors.AuthItem
                    }
                )
            }
        }
    }
}


@Preview
@Composable
private fun Preview() {
    AppTheme {
        ContentLayout()
    }
}

