@file:OptIn(ExperimentalMaterial3Api::class)

package top.baymaxam.keyvault.ui.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.util.MainTabDestination

/**
 * 顶部标题栏
 * @author John
 * @since 06 8月 2024
 */
@Composable
fun TopBackBar(
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = content,
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = null)
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.inverseOnSurface
        )
    )
}

@Composable
fun BottomBar(
    onAddClick: () -> Unit = {},
    isNavigationSelected: (MainTabDestination) -> Boolean = { false },
    onNavigationItemClick: (MainTabDestination) -> Unit = {}
) {
    NavigationBar {
        TabNavigationItem(
            tab = MainTabDestination.Home,
            isSelected = isNavigationSelected(MainTabDestination.Home),
            onClick = { onNavigationItemClick(MainTabDestination.Home) }
        )
        IconButton(
            onClick = onAddClick,
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Icon(
                imageVector = Icons.Filled.AddCircle,
                contentDescription = "添加",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
        }
        TabNavigationItem(
            tab = MainTabDestination.Settings,
            isSelected = isNavigationSelected(MainTabDestination.Settings),
            onClick = { onNavigationItemClick(MainTabDestination.Settings) }
        )
    }
}

@Composable
private fun RowScope.TabNavigationItem(
    tab: MainTabDestination,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    NavigationBarItem(
        selected = isSelected,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = tab.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(30.dp)
            )
        }
    )
}


@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    AppTheme {
        TopBackBar(onBack = {}) { Text("Hello") }
    }
}
