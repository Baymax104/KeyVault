package top.baymaxam.keyvault.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.KeyType
import top.baymaxam.keyvault.model.domain.asEntity
import top.baymaxam.keyvault.repo.KeyDao
import top.baymaxam.keyvault.state.DialogState
import top.baymaxam.keyvault.state.rememberDialogState
import top.baymaxam.keyvault.ui.component.ConfirmDialog
import top.baymaxam.keyvault.ui.component.FillIcon
import top.baymaxam.keyvault.ui.component.InfoField
import top.baymaxam.keyvault.ui.component.TopBackBar
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.ui.theme.IconColors
import top.baymaxam.keyvault.ui.theme.outlinedTextFieldColor
import top.baymaxam.keyvault.util.errorToast
import top.baymaxam.keyvault.util.successToast
import top.baymaxam.keyvault.util.toDateString

/**
 * 条目信息页
 * @author John
 * @since 08 8月 2024
 */
data class ItemInfoScreen(val item: KeyItem) : Screen {

    override val key: ScreenKey
        get() = "Item-Info-Screen-${item.hashCode()}"

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.root
        val dao = koinInject<KeyDao>()
        val nameState = remember { mutableStateOf(item.name) }
        val usernameState = remember { mutableStateOf(item.username) }
        val passwordState = remember { mutableStateOf(item.password) }
        val commentState = remember { mutableStateOf(item.comment) }
        val dialogState = rememberDialogState()
        val scope = rememberCoroutineScope()
        val clipboardManager = LocalClipboardManager.current

        fun checkEquals(): Boolean = with(item) {
            name == nameState.value && username == usernameState.value &&
                    password == passwordState.value && comment == commentState.value
        }

        suspend fun updateItem() = item.apply {
            name = nameState.value
            username = usernameState.value
            password = passwordState.value
            comment = commentState.value
        }.runCatching {
            dao.update(asEntity())
        }

        ContentLayout(
            item = item,
            nameState = nameState,
            usernameState = usernameState,
            passwordState = passwordState,
            commentState = commentState,
            dialogState = dialogState,
            onBack = { if (!checkEquals()) dialogState.show() else navigator.pop() },
            onCopy = {
                clipboardManager.setText(AnnotatedString(it))
                successToast("复制成功")
            },
            onSaveClick = {
                if (!checkEquals()) {
                    scope.launch {
                        updateItem()
                            .onSuccess { successToast("修改成功") }
                            .onFailure { errorToast(it.message) }
                    }
                }
            },
            onDialogConfirm = {
                scope.launch {
                    updateItem()
                        .onFailure { errorToast(it.message) }
                        .onSuccess {
                            successToast("修改成功")
                            navigator.pop()
                        }
                }
            },
            onDialogCancel = {
                dialogState.dismiss()
                navigator.pop()
            }
        )
    }

}


@Composable
private fun ContentLayout(
    item: KeyItem = KeyItem(),
    nameState: MutableState<String> = mutableStateOf(""),
    usernameState: MutableState<String> = mutableStateOf(""),
    passwordState: MutableState<String> = mutableStateOf(""),
    commentState: MutableState<String> = mutableStateOf(""),
    dialogState: DialogState = rememberDialogState(),
    onSaveClick: () -> Unit = {},
    onBack: () -> Unit = {},
    onCopy: (String) -> Unit = {},
    onDialogConfirm: () -> Unit = {},
    onDialogCancel: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopBackBar(title = "条目详情", onBack = onBack) {
                TextButton(onClick = onSaveClick) {
                    Text("保存")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
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
                    icon = when (item.type) {
                        KeyType.Website -> Icons.Rounded.Language
                        KeyType.Card -> Icons.Rounded.CreditCard
                        KeyType.Authorization -> Icons.Rounded.Person
                    },
                    colors = when (item.type) {
                        KeyType.Website -> IconColors.WebItem
                        KeyType.Card -> IconColors.CardItem
                        KeyType.Authorization -> IconColors.AuthItem
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
                        text = "最近使用时间：${item.lastUsedDate.toDateString("yyyy/MM/dd")}",
                        color = Color.Gray,
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            when (item.type) {
                KeyType.Website -> WebItemInfo(
                    nameState = nameState,
                    usernameState = usernameState,
                    passwordState = passwordState,
                    commentState = commentState,
                    onCopy = onCopy
                )

                KeyType.Card -> CardItemInfo(
                    nameState = nameState,
                    usernameState = usernameState,
                    passwordState = passwordState,
                    commentState = commentState,
                    onCopy = onCopy
                )

                KeyType.Authorization -> {}
            }
        }
    }
    ConfirmDialog(
        state = dialogState,
        text = "保留此次编辑？",
        confirmText = "保留",
        cancelText = "不保留",
        onConfirm = onDialogConfirm,
        onCancel = onDialogCancel
    )
}

@Composable
private fun ItemInfo(
    contentState: MutableState<String> = mutableStateOf(""),
    label: String = "",
    onCopy: ((String) -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        InfoField(
            contentState = contentState,
            modifier = Modifier.weight(1f),
            label = { Text(label) },
            trailingIcon = {
                if (onCopy != null) {
                    IconButton(onClick = { onCopy(contentState.value) }) {
                        Icon(imageVector = Icons.Rounded.ContentCopy, contentDescription = null)
                    }
                }
            }
        )
    }
}

@Composable
private fun CommentInfo(
    contentState: MutableState<String> = mutableStateOf(""),
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 60.dp)
    ) {
        OutlinedTextField(
            value = contentState.value,
            onValueChange = { contentState.value = it },
            shape = RoundedCornerShape(15.dp),
            colors = MaterialTheme.outlinedTextFieldColor,
            label = { Text("备注") },
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth(),
        )
    }
}

@Composable
private fun WebItemInfo(
    nameState: MutableState<String> = mutableStateOf(""),
    usernameState: MutableState<String> = mutableStateOf(""),
    passwordState: MutableState<String> = mutableStateOf(""),
    commentState: MutableState<String> = mutableStateOf(""),
    onCopy: (String) -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        ItemInfo(
            contentState = nameState,
            label = "网站名称",
        )
        ItemInfo(
            contentState = usernameState,
            label = "用户名",
            onCopy = onCopy
        )
        ItemInfo(
            contentState = passwordState,
            label = "密码",
            onCopy = onCopy
        )
        CommentInfo(
            contentState = commentState,
        )
    }
}


@Composable
private fun CardItemInfo(
    nameState: MutableState<String> = mutableStateOf(""),
    usernameState: MutableState<String> = mutableStateOf(""),
    passwordState: MutableState<String> = mutableStateOf(""),
    commentState: MutableState<String> = mutableStateOf(""),
    onCopy: (String) -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        ItemInfo(
            contentState = nameState,
            label = "卡片名称",
        )
        ItemInfo(
            contentState = usernameState,
            label = "卡号",
            onCopy = onCopy
        )
        ItemInfo(
            contentState = passwordState,
            label = "密码",
            onCopy = onCopy
        )
        CommentInfo(
            contentState = commentState,
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

