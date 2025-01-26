package top.baymaxam.keyvault.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.AddItemScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import top.baymaxam.keyvault.ui.component.BottomBar
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.util.MainTabDestination
import top.baymaxam.keyvault.util.NavigatorProvider

/**
 * 主页面
 * @author John
 * @since 23 6月 2024
 */
@Destination<RootGraph>(
    start = true,
)
@Composable
fun MainScreen(navigator: DestinationsNavigator) {
    val pagerState = rememberPagerState { 2 }
    val scope = rememberCoroutineScope()
    NavigatorProvider(navigator) {
        ContentLayout(
            pagerState = pagerState,
            onAddClick = { navigator.navigate(AddItemScreenDestination) },
            isNavigationSelected = { pagerState.currentPage == it.index },
            onNavigationItemClick = { scope.launch { pagerState.scrollToPage(it.index) } }
        )
    }
}

@Composable
private fun ContentLayout(
    pagerState: PagerState = rememberPagerState { 2 },
    onAddClick: () -> Unit = {},
    isNavigationSelected: (MainTabDestination) -> Boolean = { false },
    onNavigationItemClick: (MainTabDestination) -> Unit = {},
) {
    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            modifier = Modifier.weight(1f)
        ) {
            when (it) {
                0 -> HomeScreen()
                1 -> Text("Page2")
            }
        }
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
