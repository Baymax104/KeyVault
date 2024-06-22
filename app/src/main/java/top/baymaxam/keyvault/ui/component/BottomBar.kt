package top.baymaxam.keyvault.ui.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
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
    onAddClick: () -> Unit
) {
    val tabNavigator = LocalTabNavigator.current
    BottomBarContent(
        onAddClick = onAddClick,
        isHomeSelected = tabNavigator.current == HomeTab,
        onHomeClick = { tabNavigator.current = HomeTab },
        isSettingSelected = tabNavigator.current == SettingsTab,
        onSettingClick = { tabNavigator.current = SettingsTab }
    )
}

@Composable
private fun BottomBarContent(
    isHomeSelected: Boolean = false,
    onHomeClick: () -> Unit = {},
    onAddClick: () -> Unit = {},
    isSettingSelected: Boolean = false,
    onSettingClick: () -> Unit = {}
) {
    NavigationBar {
        TabNavigationItem(
            tab = HomeTab,
            isSelected = isHomeSelected,
            onClick = onHomeClick
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
            isSelected = isSettingSelected,
            onClick = onSettingClick
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
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    )
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        BottomBarContent()
    }
}
