package top.baymaxam.keyvault.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.AddItemScreenDestination
import com.ramcosta.composedestinations.generated.destinations.ItemInfoScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import top.baymaxam.keyvault.R
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.UserItem
import top.baymaxam.keyvault.state.DialogState
import top.baymaxam.keyvault.state.SelectedState
import top.baymaxam.keyvault.state.rememberDialogState
import top.baymaxam.keyvault.ui.component.ConfirmDialog
import top.baymaxam.keyvault.ui.component.FloatingButton
import top.baymaxam.keyvault.ui.component.KeyItemLayout
import top.baymaxam.keyvault.ui.component.TopBackBar
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.util.SlideTransitions
import top.baymaxam.keyvault.util.errorToast
import top.baymaxam.keyvault.util.successToast
import top.baymaxam.keyvault.vm.ItemListViewModel

/**
 * 条目列表页
 * @author John
 * @since 06 8月 2024
 */
@Destination<RootGraph>(style = SlideTransitions::class)
@Composable
fun ItemListScreen(navigator: DestinationsNavigator) {
    var isEditable by remember { mutableStateOf(false) }
    val vm = koinViewModel<ItemListViewModel>()
    val scope = rememberCoroutineScope()
    val dialogState = rememberDialogState()
    val clipboardManager = LocalClipboardManager.current

    if (!isEditable) {
        vm.items.forEach { it.selected = false }
    }

    BackHandler(isEditable) {
        isEditable = false
    }

    ContentLayout(
        items = vm.items,
        isInitialized = vm.isInitialized,
        isEditable = isEditable,
        dialogState = dialogState,
        onBack = { if (isEditable) isEditable = false else navigator.navigateUp() },
        onEditClick = { isEditable = !isEditable },
        onItemClick = { navigator.navigate(ItemInfoScreenDestination(it)) },
        onAddClick = { navigator.navigate(AddItemScreenDestination) },
        onItemCopy = {
            clipboardManager.setText(AnnotatedString(it.password))
            successToast("复制密码成功")
        },
        onSelected = {
            isEditable = true
            it.selected = !it.selected
        },
        onDeleteItem = {
            scope.launch {
                vm.removeSelectedItems()
                    .onSuccess { successToast("删除成功") }
                    .onFailure { errorToast(it.message) }
            }
        },
    )
}

@Composable
private fun ContentLayout(
    items: List<SelectedState<KeyItem>> = emptyList(),
    isInitialized: Boolean = true,
    dialogState: DialogState = rememberDialogState(),
    isEditable: Boolean = false,
    onBack: () -> Unit = {},
    onItemClick: (KeyItem) -> Unit = {},
    onItemCopy: (UserItem) -> Unit = {},
    onSelected: (SelectedState<KeyItem>) -> Unit = {},
    onEditClick: () -> Unit = {},
    onAddClick: () -> Unit = {},
    onDeleteItem: () -> Unit = {},
) {

    Scaffold(
        topBar = {
            TopBackBar(
                onBack = onBack,
                actions = {
                    TextButton(onClick = onEditClick) {
                        Text(if (!isEditable) "管理" else "完成")
                    }
                }
            ) {
                Text("密码本")
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
            ItemListView(
                items = items,
                isInitialized = isInitialized,
                isEditable = isEditable,
                onItemCopy = onItemCopy,
                onItemClick = onItemClick,
                onSelected = onSelected
            )
            if (isEditable) {
                EditBar(
                    items = items,
                    onDeleteClick = { dialogState.show() }
                )
            }
        }
    }
    ConfirmDialog(
        state = dialogState,
        title = { Text("确认删除") },
        text = { Text("确认删除选中条目？") },
        confirmButton = {
            TextButton(
                onClick = {
                    dialogState.dismiss()
                    onDeleteItem()
                }
            ) {
                Text("确认")
            }
        },
        cancelButton = {
            TextButton(onClick = { dialogState.dismiss() }) {
                Text("取消")
            }
        }
    )
}

@Composable
private fun EditBar(
    items: List<SelectedState<KeyItem>> = emptyList(),
    onDeleteClick: () -> Unit = {},
) {
    val selectedNumber by remember { derivedStateOf { items.count { it.selected } } }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .padding(vertical = 5.dp, horizontal = 15.dp)
    ) {
        Text(
            text = "已选：${selectedNumber}项，共${items.size}项",
            modifier = Modifier.weight(1f)
        )
        TextButton(
            onClick = { if (selectedNumber > 0) onDeleteClick() },
            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
        ) {
            Text("删除", color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
private fun ColumnScope.ItemListView(
    items: List<SelectedState<KeyItem>> = emptyList(),
    isInitialized: Boolean = true,
    isEditable: Boolean = false,
    onItemCopy: (UserItem) -> Unit = {},
    onItemClick: (KeyItem) -> Unit = {},
    onSelected: (SelectedState<KeyItem>) -> Unit = {}
) {
    if (!isInitialized) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(modifier = Modifier.size(60.dp))
        }
    } else {
        if (items.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 10.dp, horizontal = 5.dp)
            ) {
                items(
                    items = items,
                    key = { it.value.id }
                ) {
                    KeyItemLayout(
                        item = it,
                        isEditable = isEditable,
                        onClick = onItemClick,
                        onCopy = onItemCopy,
                        onSelected = onSelected
                    )
                }
            }
        } else {
            Image(
                painter = painterResource(id = R.drawable.img_no_data),
                contentDescription = null,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        ContentLayout(
            items = listOf(
                SelectedState(UserItem(name = "hello1")),
                SelectedState(UserItem(name = "hello1")),
                SelectedState(UserItem(name = "hello1")),
                SelectedState(UserItem(name = "hello1")),
                SelectedState(UserItem(name = "hello1")),
            ),
            isEditable = true
        )
    }
}
