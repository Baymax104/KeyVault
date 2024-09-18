package top.baymaxam.keyvault.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import top.baymaxam.keyvault.ui.component.BottomBar
import top.baymaxam.keyvault.ui.theme.AppTheme

/**
 * 主页面
 * @author John
 * @since 23 6月 2024
 */
class MainScreen : Screen {

    override val key: ScreenKey
        get() = "Main-Screen"

    @Composable
    override fun Content() {
        BottomSheetNavigator(
            sheetShape = RoundedCornerShape(15.dp)
        ) { bottomSheetNavigator ->
            TabNavigator(HomeTab) { tabNavigator ->
                ContentLayout(
                    onAddClick = { bottomSheetNavigator.show(AddScreen()) },
                    isNavigationSelected = { tabNavigator.current == it },
                    onNavigationItemClick = { tabNavigator.current = it }
                ) {
                    CurrentTab()
                }
            }
        }
    }
}

@Composable
private fun ContentLayout(
    onAddClick: () -> Unit = {},
    isNavigationSelected: (Tab) -> Boolean = { false },
    onNavigationItemClick: (Tab) -> Unit = {},
    tabContent: @Composable BoxScope.() -> Unit = { HomeTab.Content() }
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.weight(1f),
            content = tabContent
        )
        BottomBar(
            onAddClick = onAddClick,
            isNavigationSelected = isNavigationSelected,
            onNavigationItemClick = onNavigationItemClick
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        ContentLayout()
    }
}
