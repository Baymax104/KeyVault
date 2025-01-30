package top.baymaxam.keyvault.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.AddItemScreenDestination
import com.ramcosta.composedestinations.generated.destinations.ItemInfoScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import top.baymaxam.keyvault.R
import top.baymaxam.keyvault.model.domain.AuthItem
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.UserItem
import top.baymaxam.keyvault.state.DialogState
import top.baymaxam.keyvault.state.ItemListViewModel
import top.baymaxam.keyvault.state.SelectedState
import top.baymaxam.keyvault.state.rememberDialogState
import top.baymaxam.keyvault.ui.component.ConfirmDialog
import top.baymaxam.keyvault.ui.component.FillIcon
import top.baymaxam.keyvault.ui.component.FloatingButton
import top.baymaxam.keyvault.ui.component.TopBackBar
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.ui.theme.IconColors
import top.baymaxam.keyvault.util.errorToast
import top.baymaxam.keyvault.util.successToast

/**
 * 条目列表页
 * @author John
 * @since 06 8月 2024
 */
@Destination<RootGraph>()
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
        onItemCopy = { item ->
            clipboardManager.setText(AnnotatedString(item.password))
            successToast("复制密码成功")
        },
        onSelected = {
            isEditable = true
            it.selected = !it.selected
        },
        onDialogConfirm = {
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
    onDialogConfirm: () -> Unit = {},
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
        title = "确认删除",
        text = "确认删除选中条目？",
        onConfirm = onDialogConfirm
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
            onClick = { if (selectedNumber > 0) onDeleteClick() }
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
            ItemList(
                items = items,
                modifier = Modifier.weight(1f),
                isEditable = isEditable,
                onItemClick = onItemClick,
                onItemCopy = onItemCopy,
                onSelected = onSelected,
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.img_no_data),
                contentDescription = null,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun ItemList(
    items: List<SelectedState<KeyItem>>,
    modifier: Modifier = Modifier,
    isEditable: Boolean = false,
    onItemClick: (KeyItem) -> Unit = {},
    onItemCopy: (UserItem) -> Unit = {},
    onSelected: (SelectedState<KeyItem>) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier,
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
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun KeyItemLayout(
    item: SelectedState<KeyItem>,
    isEditable: Boolean = false,
    onClick: (KeyItem) -> Unit = {},
    onCopy: (UserItem) -> Unit = {},
    onSelected: (SelectedState<KeyItem>) -> Unit = {},
) {
    val (keyItem) = item
    Surface(
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 1.dp,
        tonalElevation = 0.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onLongClick = { if (!isEditable) onSelected(item) },
                    onClick = { if (isEditable) onSelected(item) else onClick(keyItem) }
                )
                .padding(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                FillIcon(
                    icon = when (keyItem) {
                        is UserItem -> Icons.Rounded.CreditCard
                        is AuthItem -> Icons.Rounded.Person
                    },
                    shape = RoundedCornerShape(20),
                    modifier = Modifier.size(45.dp),
                    colors = when (keyItem) {
                        is UserItem -> IconColors.UserItem
                        is AuthItem -> IconColors.AuthItem
                    }
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 10.dp)
                ) {
                    Text(
                        text = keyItem.name,
                        style = TextStyle(
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = when (keyItem) {
                            is UserItem -> keyItem.username
                            is AuthItem -> keyItem.authName
                        },
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Gray
                        )
                    )
                }

                if (isEditable) {
                    RadioButton(
                        selected = item.selected,
                        onClick = { onSelected(item) }
                    )
                } else if (keyItem is UserItem) {
                    IconButton(onClick = { onCopy(keyItem) }) {
                        Icon(imageVector = Icons.Rounded.ContentCopy, contentDescription = null)
                    }
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
