@file:OptIn(ExperimentalFoundationApi::class)

package top.baymaxam.keyvault.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import kotlinx.coroutines.launch
import top.baymaxam.keyvault.R
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.state.ItemListScreenModel
import top.baymaxam.keyvault.state.ItemSelectedState
import top.baymaxam.keyvault.ui.component.ItemList
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
        val editableState = remember { mutableStateOf(false) }
        val viewModel = koinScreenModel<ItemListScreenModel>()
        val pagerState = rememberPagerState { 3 }

        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.settledPage }
                .collect { viewModel.loadPage(it) }
        }

        ContentLayout(
            pagerState = pagerState,
            items = viewModel.items,
            editableState = editableState,
            onBack = { navigator.pop() },
            onItemClick = {},
            onItemCopy = {}
        )
    }
}

@Composable
private fun ContentLayout(
    pagerState: PagerState = rememberPagerState { 3 },
    items: List<List<ItemSelectedState<KeyItem>>> = emptyList(),
    editableState: MutableState<Boolean> = mutableStateOf(false),
    onBack: () -> Unit = {},
    onItemClick: (KeyItem) -> Unit = {},
    onItemCopy: (KeyItem) -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = { TopBackBar(title = "密码本", onBack = onBack) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            TabRow(selectedTabIndex = pagerState.currentPage) {
                listOf("网站", "卡片", "授权").forEachIndexed { index, text ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        text = { Text(text = text) },
                        onClick = { scope.launch { pagerState.animateScrollToPage(index) } }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 10.dp),
                pageSpacing = 10.dp
            ) {
                if (items[it].isNotEmpty()) {
                    ItemList(
                        items = items[it],
                        editableState = editableState,
                        onItemClick = onItemClick,
                        onItemCopy = onItemCopy,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.img_no_data),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        ContentLayout(
            items = listOf(
                listOf(
                    ItemSelectedState(KeyItem(id = 0, name = "hello1")),
                    ItemSelectedState(KeyItem(id = 1, name = "hello1")),
                    ItemSelectedState(KeyItem(id = 2, name = "hello1")),
                    ItemSelectedState(KeyItem(id = 3, name = "hello1")),
                    ItemSelectedState(KeyItem(id = 4, name = "hello1")),
                )
            )
        )
    }
}
