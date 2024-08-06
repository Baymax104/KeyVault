@file:OptIn(ExperimentalFoundationApi::class)

package top.baymaxam.keyvault.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import top.baymaxam.keyvault.ui.component.TopBackBar
import top.baymaxam.keyvault.ui.theme.AppTheme

/**
 * 条目列表页
 * @author John
 * @since 06 8月 2024
 */
class ItemListScreen : Screen {

    override val key: ScreenKey
        get() = "Item-List-Screen"

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.root
        ContentLayout(
            onBack = { navigator.pop() }
        )
    }
}

@Composable
private fun ContentLayout(
    onBack: () -> Unit = {}
) {
    val pagerState = rememberPagerState { 3 }
    Scaffold(
        topBar = { TopBackBar(title = "密码本", onBack = onBack) },
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.padding(paddingValues)
        ) { page ->
            Text(
                text = "$page",
                modifier = Modifier.fillMaxSize()
            )

        }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        ContentLayout()
    }
}
