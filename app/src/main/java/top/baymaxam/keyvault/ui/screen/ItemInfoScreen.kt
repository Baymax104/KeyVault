package top.baymaxam.keyvault.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.AddItemTagScreenDestination
import com.ramcosta.composedestinations.generated.destinations.RootSelectUserItemScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultRecipient
import com.ramcosta.composedestinations.result.onResult
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import top.baymaxam.keyvault.model.domain.AuthItem
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.Tag
import top.baymaxam.keyvault.model.domain.UserItem
import top.baymaxam.keyvault.state.DialogState
import top.baymaxam.keyvault.state.rememberDialogState
import top.baymaxam.keyvault.ui.component.CommentField
import top.baymaxam.keyvault.ui.component.ConfirmDialog
import top.baymaxam.keyvault.ui.component.EntryButton
import top.baymaxam.keyvault.ui.component.FillIcon
import top.baymaxam.keyvault.ui.component.InputField
import top.baymaxam.keyvault.ui.component.TopBackBar
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.ui.theme.IconColors
import top.baymaxam.keyvault.util.errorToast
import top.baymaxam.keyvault.util.successToast
import top.baymaxam.keyvault.util.toDateString
import top.baymaxam.keyvault.vm.ItemViewModel

/**
 * 条目信息页
 * @author John
 * @since 08 8月 2024
 */
@Destination<RootGraph>
@Composable
fun ItemInfoScreen(
    navigator: DestinationsNavigator,
    item: KeyItem,
    authRecipient: ResultRecipient<RootSelectUserItemScreenDestination, UserItem>
) {
    val vm = koinViewModel<ItemViewModel> { parametersOf(item) }
    val dialogState = rememberDialogState()
    val scope = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current

    authRecipient.onResult { vm.authUserItem = it }

    DisposableEffect(vm) {
        onDispose { vm.updateItemResentDate() }
    }

    ContentLayout(
        item = vm.item,
        nameState = vm.nameState,
        usernameState = vm.usernameState,
        passwordState = vm.passwordState,
        commentState = vm.commentState,
        authUserItem = vm.authUserItem,
        tags = vm.keyTags,
        dialogState = dialogState,
        onBack = { if (!vm.isItemEquals()) dialogState.show() else navigator.navigateUp() },
        onSelectAuth = { navigator.navigate(RootSelectUserItemScreenDestination) },
        onTagAddClick = { navigator.navigate(AddItemTagScreenDestination(vm.item)) },
        onCopy = { text ->
            clipboardManager.setText(AnnotatedString(text))
            successToast("复制成功")
        },
        onSaveClick = {
            if (!vm.isItemEquals()) {
                scope.launch {
                    vm.updateItem()
                        .onSuccess { successToast("修改成功") }
                        .onFailure { errorToast(it.message) }
                }
            }
        },
        onDialogConfirm = {
            scope.launch {
                vm.updateItem()
                    .onFailure { errorToast(it.message) }
                    .onSuccess {
                        successToast("修改成功")
                        navigator.navigateUp()
                    }
            }
        },
        onDialogCancel = {
            dialogState.dismiss()
            navigator.navigateUp()
        }
    )
}


@Composable
private fun ContentLayout(
    item: KeyItem = UserItem(),
    nameState: MutableState<String> = mutableStateOf(""),
    usernameState: MutableState<String> = mutableStateOf(""),
    passwordState: MutableState<String> = mutableStateOf(""),
    commentState: MutableState<String> = mutableStateOf(""),
    authUserItem: UserItem? = null,
    tags: List<Tag> = emptyList(),
    dialogState: DialogState = rememberDialogState(),
    onSaveClick: () -> Unit = {},
    onBack: () -> Unit = {},
    onCopy: (String) -> Unit = {},
    onDialogConfirm: () -> Unit = {},
    onDialogCancel: () -> Unit = {},
    onSelectAuth: () -> Unit = {},
    onTagAddClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopBackBar(
                content = { Text("条目详情") },
                onBack = onBack,
                actions = {
                    TextButton(onClick = onSaveClick) {
                        Text("保存")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(start = 15.dp, end = 15.dp, top = 25.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                FillIcon(
                    shape = RoundedCornerShape(20),
                    modifier = Modifier.size(70.dp),
                    icon = when (item) {
                        is UserItem -> Icons.Rounded.CreditCard
                        is AuthItem -> Icons.Rounded.Person
                    },
                    colors = when (item) {
                        is UserItem -> IconColors.UserItem
                        is AuthItem -> IconColors.AuthItem
                    }
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = "创建时间：${item.createDate.toDateString("yyyy/MM/dd")}",
                        color = Color.Gray,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "最近查看时间：${item.resentDate.toDateString("yyyy/MM/dd HH:mm:ss")}",
                        color = Color.Gray,
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(15.dp),
            ) {
                when (item) {
                    is UserItem -> UserItemInfo(
                        nameState = nameState,
                        usernameState = usernameState,
                        passwordState = passwordState,
                        commentState = commentState,
                        tags = tags,
                        onCopy = onCopy,
                        onTagAddClick = onTagAddClick
                    )

                    is AuthItem -> AuthItemInfo(
                        nameState = nameState,
                        commentState = commentState,
                        tags = tags,
                        authUserItem = authUserItem,
                        onSelectAuth = onSelectAuth,
                        onTagAddClick = onTagAddClick
                    )
                }
            }
        }
    }
    ConfirmDialog(
        state = dialogState,
        text = { Text("保留此次编辑？") },
        confirmButton = {
            TextButton(
                onClick = {
                    dialogState.dismiss()
                    onDialogConfirm()
                }
            ) {
                Text("保留")
            }
        },
        cancelButton = {
            TextButton(onClick = onDialogCancel) {
                Text("不保留")
            }
        },
    )
}

@Composable
private fun ItemInfo(
    contentState: MutableState<String> = mutableStateOf(""),
    placeholder: (@Composable () -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    onCopy: ((String) -> Unit)? = null,
) {
    InputField(
        contentState = contentState,
        modifier = Modifier.fillMaxWidth(),
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = {
            if (onCopy != null) {
                IconButton(onClick = { onCopy(contentState.value) }) {
                    Icon(imageVector = Icons.Rounded.ContentCopy, contentDescription = null)
                }
            }
        }
    )
}

@Composable
private fun UserItemInfo(
    nameState: MutableState<String> = mutableStateOf(""),
    usernameState: MutableState<String> = mutableStateOf(""),
    passwordState: MutableState<String> = mutableStateOf(""),
    commentState: MutableState<String> = mutableStateOf(""),
    tags: List<Tag> = emptyList(),
    onCopy: (String) -> Unit = {},
    onTagAddClick: () -> Unit = {},
) {
    ItemInfo(
        contentState = nameState,
        placeholder = { Text("条目名称") },
        leadingIcon = { Icon(Icons.Rounded.CreditCard, contentDescription = null) }
    )
    ItemInfo(
        contentState = usernameState,
        placeholder = { Text("用户名") },
        leadingIcon = { Icon(Icons.Rounded.Person, contentDescription = null) },
        onCopy = onCopy
    )
    ItemInfo(
        contentState = passwordState,
        placeholder = { Text("密码") },
        leadingIcon = { Icon(Icons.Rounded.Key, contentDescription = null) },
        onCopy = onCopy
    )
    CommentField(
        contentState = commentState,
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
    )
    Column {
        Text("标签")
        FlowTags(
            items = tags,
            maxItemsInEachRow = 5,
            onAddClick = onTagAddClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun AuthItemInfo(
    nameState: MutableState<String> = mutableStateOf(""),
    commentState: MutableState<String> = mutableStateOf(""),
    tags: List<Tag> = emptyList(),
    authUserItem: UserItem? = null,
    onSelectAuth: () -> Unit = {},
    onTagAddClick: () -> Unit = {},
) {
    ItemInfo(
        contentState = nameState,
        placeholder = { Text("授权名称") },
        leadingIcon = { Icon(Icons.Rounded.Person, contentDescription = null) }
    )
    EntryButton(
        value = if (authUserItem != null) "${authUserItem.name} ${authUserItem.username}" else "选择授权",
        onClick = onSelectAuth,
        leadingIcon = { Icon(Icons.Rounded.CreditCard, contentDescription = null) }
    )
    CommentField(
        contentState = commentState,
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
    )
    Column {
        Text("标签")
        FlowTags(
            items = tags,
            maxItemsInEachRow = 5,
            onAddClick = onTagAddClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowTags(
    items: List<Tag>,
    modifier: Modifier = Modifier,
    maxItemsInEachRow: Int = Int.MAX_VALUE,
    maxLines: Int = Int.MAX_VALUE,
    onAddClick: (() -> Unit)? = null,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        modifier = modifier,
        maxItemsInEachRow = maxItemsInEachRow,
        maxLines = maxLines
    ) {
        items.forEach {
            key(it.id) {
                AssistChip(
                    onClick = {},
                    enabled = false,
                    label = { Text(it.name) },
                    border = AssistChipDefaults.assistChipBorder(true),
                    colors = AssistChipDefaults.assistChipColors(
                        disabledContainerColor = MaterialTheme.colorScheme.background,
                        disabledLabelColor = MaterialTheme.colorScheme.onBackground,
                        disabledTrailingIconContentColor = MaterialTheme.colorScheme.onBackground
                    ),
                )
            }
        }
        if (onAddClick != null) {
            AssistChip(
                onClick = onAddClick,
                label = { Icon(Icons.Rounded.Add, contentDescription = null) }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        ContentLayout(item = AuthItem())
    }
}

