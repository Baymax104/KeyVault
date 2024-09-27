@file:OptIn(ExperimentalMaterial3Api::class)

package top.baymaxam.keyvault.ui.screen

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import top.baymaxam.keyvault.R
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.Tag
import top.baymaxam.keyvault.state.DialogState
import top.baymaxam.keyvault.state.ItemListViewModel
import top.baymaxam.keyvault.state.SelectedState
import top.baymaxam.keyvault.state.rememberDialogState
import top.baymaxam.keyvault.ui.component.ConfirmDialog
import top.baymaxam.keyvault.ui.component.FloatingButton
import top.baymaxam.keyvault.ui.component.ItemList
import top.baymaxam.keyvault.ui.component.TopBackBar
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.util.errorToast
import top.baymaxam.keyvault.util.root
import top.baymaxam.keyvault.util.successToast

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
        var isEditable by remember { mutableStateOf(false) }
        val vm = koinViewModel<ItemListViewModel>()
        val pagerState = rememberPagerState { 3 }
        val settledPage by remember { derivedStateOf { pagerState.settledPage } }
        val scope = rememberCoroutineScope()
        val dialogState = rememberDialogState()
        val clipboardManager = LocalClipboardManager.current

        fun clearEditState() {
            isEditable = false
            vm.clearSelectedState(settledPage)
        }

        LaunchedEffect(settledPage) {
            vm.getPageItems(settledPage)
        }

        BackHandler(isEditable) {
            clearEditState()
        }

        BottomSheetNavigator(
            sheetShape = RoundedCornerShape(15.dp),
            sheetContent = { AddScreen().Content() }
        ) { bottomSheetNavigator ->
            ContentLayout(
                pagerState = pagerState,
                items = vm.items,
                isItemsLoading = vm.isItemsLoading,
                isEditable = isEditable,
                selectedNumber = vm.selectedNumber,
                dialogState = dialogState,
                onBack = { if (isEditable) clearEditState() else navigator.pop() },
                onEditClick = {
                    isEditable = !isEditable
                    if (!isEditable) {
                        vm.clearSelectedState(settledPage)
                    }
                },
                onItemClick = { navigator += ItemInfoScreen(it) },
                onItemCopy = { item ->
                    clipboardManager.setText(AnnotatedString(item.password))
                    successToast("复制密码成功")
                },
                onSelected = {
                    isEditable = true
                    vm.selectedNumber += if (it.selected) 1 else -1
                },
                onDialogConfirm = {
                    scope.launch {
                        vm.removeSelectedItems(settledPage)
                            .onSuccess { successToast("删除成功") }
                            .onFailure { errorToast(it.message) }
                    }
                },
                onAddClick = { bottomSheetNavigator.show(AddScreen()) },
                tagsFactory = {
                    emptyList()
                }
            )
        }
    }
}

@Composable
private fun ContentLayout(
    pagerState: PagerState = rememberPagerState { 3 },
    items: List<List<SelectedState<KeyItem>>> = emptyList(),
    isItemsLoading: List<MutableState<Boolean>> = emptyList(),
    dialogState: DialogState = rememberDialogState(),
    isEditable: Boolean = false,
    selectedNumber: Int = 0,
    onBack: () -> Unit = {},
    onItemClick: (KeyItem) -> Unit = {},
    onItemCopy: (KeyItem) -> Unit = {},
    onSelected: (SelectedState<KeyItem>) -> Unit = {},
    onEditClick: () -> Unit = {},
    onAddClick: () -> Unit = {},
    onDialogConfirm: () -> Unit = {},
    tagsFactory: (KeyItem) -> List<Tag> = { emptyList() }
) {
    val scope = rememberCoroutineScope()
    val currentPage by remember { derivedStateOf { pagerState.currentPage } }
    Scaffold(
        topBar = {
            TopBackBar(title = "密码本", onBack = onBack) {
                TextButton(onClick = onEditClick) {
                    Text(if (!isEditable) "管理" else "完成")
                }
            }
        },
        floatingActionButton = {
            if (!isEditable) {
                FloatingButton(
                    icon = Icons.Rounded.Add,
                    modifier = Modifier.padding(end = 15.dp, bottom = 25.dp),
                    onClick = onAddClick
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            if (!isEditable) {
                TabRow(
                    selectedTabIndex = currentPage,
                    modifier = Modifier.height(50.dp)
                ) {
                    listOf("网站", "卡片", "授权").forEachIndexed { index, text ->
                        Tab(
                            selected = currentPage == index,
                            text = { Text(text) },
                            onClick = { scope.launch { pagerState.animateScrollToPage(index) } }
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .height(50.dp)
                        .background(MaterialTheme.colorScheme.inverseOnSurface)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .padding(vertical = 5.dp, horizontal = 10.dp)
                    ) {
                        Text(
                            text = "已选：${selectedNumber}项，共${items[pagerState.settledPage].size}项",
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = { if (selectedNumber > 0) dialogState.show() }
                        ) {
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
                userScrollEnabled = !isEditable,
                contentPadding = PaddingValues(horizontal = 10.dp),
                pageSpacing = 10.dp
            ) {
                if (isItemsLoading[it].value) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(60.dp))
                    }
                } else {
                    if (items[it].isNotEmpty()) {
                        ItemList(
                            items = items[it],
                            modifier = Modifier.fillMaxSize(),
                            isEditable = isEditable,
                            onItemClick = onItemClick,
                            onItemCopy = onItemCopy,
                            onSelected = onSelected,
                            tagsFactory = tagsFactory
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
    ConfirmDialog(
        state = dialogState,
        title = "确认删除",
        text = "确认删除选中条目？",
        onConfirm = onDialogConfirm
    )
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        ContentLayout(
            items = listOf(
                listOf(
                    SelectedState(KeyItem(name = "hello1")),
                    SelectedState(KeyItem(name = "hello1")),
                    SelectedState(KeyItem(name = "hello1")),
                    SelectedState(KeyItem(name = "hello1")),
                    SelectedState(KeyItem(name = "hello1")),
                )
            )
        )
    }
}
