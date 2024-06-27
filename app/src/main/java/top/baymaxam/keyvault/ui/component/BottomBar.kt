package top.baymaxam.keyvault.ui.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import top.baymaxam.keyvault.ui.screen.HomeTab
import top.baymaxam.keyvault.ui.screen.SettingsTab
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.ui.theme.MainColor

/**
 * 底部导航栏
 * @author John
 * @since 22 6月 2024
 */

@Composable
fun BottomBar(
    onAddClick: () -> Unit = {},
    isNavigationSelected: (Tab) -> Boolean = { false },
    onNavigationItemClick: (Tab) -> Unit = {}
) {
    NavigationBar {
        TabNavigationItem(
            tab = HomeTab,
            isSelected = isNavigationSelected(HomeTab),
            onClick = { onNavigationItemClick(HomeTab) }
        )
        IconButton(
            onClick = onAddClick,
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Icon(
                imageVector = Icons.Filled.AddCircle,
                contentDescription = "添加",
                tint = MainColor,
                modifier = Modifier.size(40.dp)
            )
        }
        TabNavigationItem(
            tab = SettingsTab,
            isSelected = isNavigationSelected(SettingsTab),
            onClick = { onNavigationItemClick(SettingsTab) }
        )
    }
}

@Composable
private fun RowScope.TabNavigationItem(
    tab: Tab,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    NavigationBarItem(
        selected = isSelected,
        onClick = onClick,
        icon = {
            if (tab.options.icon != null) {
                Icon(
                    painter = tab.options.icon!!,
                    contentDescription = tab.options.title,
                    tint = MainColor,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    )
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        BottomBar()
    }
}
