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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.AddTagItemScreenDestination
import com.ramcosta.composedestinations.generated.destinations.ItemInfoScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import top.baymaxam.keyvault.R
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.Tag
import top.baymaxam.keyvault.model.domain.UserItem
import top.baymaxam.keyvault.state.DialogState
import top.baymaxam.keyvault.state.SelectedState
import top.baymaxam.keyvault.state.rememberDialogState
import top.baymaxam.keyvault.ui.component.ConfirmDialog
import top.baymaxam.keyvault.ui.component.KeyItemLayout
import top.baymaxam.keyvault.ui.component.TopBackBar
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.util.errorToast
import top.baymaxam.keyvault.util.successToast
import top.baymaxam.keyvault.vm.TagItemListViewModel

/**
 * 标签条目页
 * @author John
 * @since 31 1月 2025
 */
@Destination<RootGraph>
@Composable
fun TagItemListScreen(
    navigator: DestinationsNavigator,
    tag: Tag
) {
    val vm = koinViewModel<TagItemListViewModel> { parametersOf(tag) }
    val editableState = remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current
    val dialogState = rememberDialogState()
    val scope = rememberCoroutineScope()

    if (!editableState.value) {
        vm.items.forEach { it.selected = false }
    }

    BackHandler(editableState.value) {
        editableState.value = false
    }

    ContentLayout(
        tag = vm.tag,
        items = vm.items,
        editableState = editableState,
        dialogState = dialogState,
        isInitialized = vm.isInitialized,
        onBack = { navigator.navigateUp() },
        onItemCopy = {
            clipboardManager.setText(AnnotatedString(it.password))
            successToast("复制密码成功")
        },
        onSelected = {
            editableState.value = true
            it.selected = !it.selected
        },
        onItemClick = { navigator.navigate(ItemInfoScreenDestination(it)) },
        onAddClick = { navigator.navigate(AddTagItemScreenDestination(vm.tag)) },
        onDialogConfirm = {
            scope.launch {
                vm.removeSelectedItem()
                    .onSuccess { successToast("移出成功") }
                    .onFailure { errorToast(it.message) }
            }
        }
    )
}

@Composable
private fun ContentLayout(
    tag: Tag = Tag(),
    items: List<SelectedState<KeyItem>> = emptyList(),
    dialogState: DialogState = rememberDialogState(),
    editableState: MutableState<Boolean> = mutableStateOf(false),
    isInitialized: Boolean = true,
    onBack: () -> Unit = {},
    onItemCopy: (UserItem) -> Unit = {},
    onItemClick: (KeyItem) -> Unit = {},
    onSelected: (SelectedState<KeyItem>) -> Unit = {},
    onAddClick: () -> Unit = {},
    onDialogConfirm: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopBackBar(onBack = onBack) {
                Text(tag.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            ItemListView(
                items = items,
                isInitialized = isInitialized,
                isEditable = editableState.value,
                onItemCopy = onItemCopy,
                onItemClick = onItemClick,
                onSelected = onSelected
            )
            EditBar(
                items = items,
                isEditable = editableState,
                onAddClick = onAddClick,
                onDeleteClick = { dialogState.show() }
            )
        }
    }
    ConfirmDialog(
        state = dialogState,
        title = { Text("确认删除") },
        text = { Text("确认删除选中标签？") },
        confirmButton = {
            TextButton(
                onClick = {
                    dialogState.dismiss()
                    onDialogConfirm()
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
    isEditable: MutableState<Boolean> = mutableStateOf(false),
    onAddClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
) {
    val selectedNumber by remember { derivedStateOf { items.count { it.selected } } }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .padding(vertical = 5.dp, horizontal = 15.dp)
    ) {
        if (isEditable.value) {
            Text(
                text = "已选：${selectedNumber}项，共${items.size}项",
                modifier = Modifier.weight(1f)
            )
        } else {
            TextButton(onClick = onAddClick) {
                Text("添加", color = MaterialTheme.colorScheme.primary)
            }
        }
        TextButton(
            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
            onClick = {
                if (!isEditable.value) {
                    isEditable.value = true
                } else if (selectedNumber > 0) {
                    onDeleteClick()
                }
            }
        ) {
            Text("移出", color = MaterialTheme.colorScheme.error)
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


@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        val items = listOf(
            SelectedState(UserItem(name = "Hello1")),
            SelectedState(UserItem()),
            SelectedState(UserItem()),
            SelectedState(UserItem()),
        )
        ContentLayout(
            tag = Tag(name = "hello"),
            items = items
        )
    }
}
