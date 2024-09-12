@file:OptIn(ExperimentalFoundationApi::class)

package top.baymaxam.keyvault.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
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
        val selectedNumberState = remember { mutableIntStateOf(0) }
        val scope = rememberCoroutineScope()

        fun clearEditState() {
            editableState.value = false
            selectedNumberState.intValue = 0
            viewModel.items[pagerState.settledPage].forEach { it.selected.value = false }
        }

        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.settledPage }
                .collect { viewModel.loadPage(it) }
        }

        BackHandler(editableState.value) {
            clearEditState()
        }

        BottomSheetNavigator(
            sheetShape = RoundedCornerShape(15.dp)
        ) { bottomSheetNavigator ->
            ContentLayout(
                pagerState = pagerState,
                items = viewModel.items,
                editableState = editableState,
                selectedNumberState = selectedNumberState,
                onBack = {
                    if (editableState.value) {
                        clearEditState()
                    } else {
                        navigator.pop()
                    }
                },
                onItemClick = {},
                onItemCopy = {},
                onSelected = {
                    selectedNumberState.intValue += if (it.selected.value) 1 else -1
                },
                onDeleteClick = {
                    scope.launch {
                        viewModel.removePages()
                    }
                },
                onAddClick = {
                    bottomSheetNavigator.show(AddScreen())
                }
            )
        }
    }
}

@Composable
private fun ContentLayout(
    pagerState: PagerState = rememberPagerState { 3 },
    items: List<List<ItemSelectedState<KeyItem>>> = emptyList(),
    editableState: MutableState<Boolean> = mutableStateOf(true),
    selectedNumberState: MutableIntState = mutableIntStateOf(0),
    onBack: () -> Unit = {},
    onItemClick: (KeyItem) -> Unit = {},
    onItemCopy: (KeyItem) -> Unit = {},
    onSelected: (ItemSelectedState<KeyItem>) -> Unit = {},
    onAddClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = { TopBackBar(title = "密码本", onBack = onBack) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                shape = RoundedCornerShape(35),
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 5.dp,
                    pressedElevation = 5.dp,
                    focusedElevation = 5.dp,
                    hoveredElevation = 5.dp
                ),
                modifier = Modifier.padding(end = 15.dp, bottom = 25.dp),
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            if (!editableState.value) {
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    modifier = Modifier.height(50.dp)
                ) {
                    listOf("网站", "卡片", "授权").forEachIndexed { index, text ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            text = { Text(text = text) },
                            onClick = { scope.launch { pagerState.animateScrollToPage(index) } }
                        )
                    }
                }
            } else {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(vertical = 5.dp, horizontal = 10.dp)
                    ) {
                        Text(
                            text = "已选：${selectedNumberState.intValue}项，共${items[pagerState.settledPage].size}项",
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = onDeleteClick) {
                            Icon(
                                imageVector = Icons.Rounded.Delete,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .height(2.dp)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary)
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                userScrollEnabled = !editableState.value,
                contentPadding = PaddingValues(horizontal = 10.dp),
                pageSpacing = 10.dp
            ) {
                if (items[it].isNotEmpty()) {
                    ItemList(
                        items = items[it],
                        modifier = Modifier.fillMaxSize(),
                        editableState = editableState,
                        onItemClick = onItemClick,
                        onItemCopy = onItemCopy,
                        onSelected = onSelected
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
