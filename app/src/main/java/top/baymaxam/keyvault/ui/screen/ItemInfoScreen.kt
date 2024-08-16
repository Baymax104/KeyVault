package top.baymaxam.keyvault.ui.screen

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.EditOff
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.KeyType
import top.baymaxam.keyvault.ui.component.FillIcon
import top.baymaxam.keyvault.ui.component.InfoField
import top.baymaxam.keyvault.ui.component.TopBackBar
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.ui.theme.IconColors
import top.baymaxam.keyvault.ui.theme.outlinedTextFieldColor
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

        val nameState = remember { mutableStateOf(item.name) }
        val usernameState = remember { mutableStateOf(item.username) }
        val passwordState = remember { mutableStateOf(item.password) }
        val commentState = remember { mutableStateOf(item.comment) }
        val editableState = remember { mutableStateOf(false) }

        BackHandler(editableState.value) {
            editableState.value = false
        }

        ContentLayout(
            item = item,
            nameState = nameState,
            usernameState = usernameState,
            passwordState = passwordState,
            commentState = commentState,
            editableState = editableState,
            onBack = { navigator.pop() },
            onCopy = { }
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
    editableState: MutableState<Boolean> = mutableStateOf(false),
    onBack: () -> Unit = {},
    onCopy: (String) -> Unit = {}
) {
    Scaffold(
        topBar = { TopBackBar(title = "条目详情", onBack = onBack) }
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
                    icon = when (item.type) {
                        KeyType.Website -> Icons.Rounded.Language
                        KeyType.Card -> Icons.Rounded.CreditCard
                        KeyType.Authorization -> Icons.Rounded.Person
                    }, modifier = Modifier.size(70.dp),
                    shape = RoundedCornerShape(20),
                    colors = when (item.type) {
                        KeyType.Website -> IconColors.WebItem
                        KeyType.Card -> IconColors.CardItem
                        KeyType.Authorization -> IconColors.AuthItem
                    }
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = nameState.value,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
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

                IconToggleButton(
                    checked = editableState.value,
                    onCheckedChange = { editableState.value = it }
                ) {
                    Icon(
                        imageVector = if (editableState.value) Icons.Rounded.EditOff else Icons.Rounded.Edit,
                        contentDescription = null
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
                    isEdit = editableState.value,
                    onCopy = onCopy
                )

                KeyType.Card -> CardItemInfo(
                    nameState = nameState,
                    usernameState = usernameState,
                    passwordState = passwordState,
                    commentState = commentState,
                    isEdit = editableState.value,
                    onCopy = onCopy
                )

                KeyType.Authorization -> {}
            }
        }
    }
}

@Composable
private fun ItemInfo(
    contentState: MutableState<String> = mutableStateOf(""),
    hintText: String = "",
    isEdit: Boolean = false,
    onCopy: (String) -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        if (isEdit) {
            InfoField(
                contentState = contentState,
                placeholder = { Text(text = hintText) },
                modifier = Modifier.weight(1f),
                label = { Text(text = hintText) }
            )
        } else {
            Text(
                text = "$hintText：",
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
            Text(text = contentState.value, modifier = Modifier.weight(1f))
        }
        if (!isEdit) {
            IconButton(onClick = { onCopy(contentState.value) }) {
                Icon(imageVector = Icons.Rounded.ContentCopy, contentDescription = null)
            }
        }
    }
}

@Composable
private fun CommentInfo(
    contentState: MutableState<String> = mutableStateOf(""),
    isEdit: Boolean = false
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 60.dp)
    ) {
        if (isEdit) {
            OutlinedTextField(
                value = contentState.value,
                onValueChange = { contentState.value = it },
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(15.dp),
                colors = MaterialTheme.outlinedTextFieldColor,
                label = { Text(text = "备注") },
                placeholder = { Text(text = "备注") },
            )
        } else {
            Text(
                text = "备注：",
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
            Text(text = contentState.value, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun WebItemInfo(
    nameState: MutableState<String> = mutableStateOf(""),
    usernameState: MutableState<String> = mutableStateOf(""),
    passwordState: MutableState<String> = mutableStateOf(""),
    commentState: MutableState<String> = mutableStateOf(""),
    isEdit: Boolean = false,
    onCopy: (String) -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        ItemInfo(
            contentState = nameState,
            hintText = "网站名称",
            isEdit = isEdit,
            onCopy = onCopy
        )
        ItemInfo(
            contentState = usernameState,
            hintText = "用户名",
            isEdit = isEdit,
            onCopy = onCopy
        )
        ItemInfo(
            contentState = passwordState,
            hintText = "密码",
            isEdit = isEdit,
            onCopy = onCopy
        )
        CommentInfo(
            contentState = commentState,
            isEdit = isEdit
        )
    }
}


@Composable
private fun CardItemInfo(
    nameState: MutableState<String> = mutableStateOf(""),
    usernameState: MutableState<String> = mutableStateOf(""),
    passwordState: MutableState<String> = mutableStateOf(""),
    commentState: MutableState<String> = mutableStateOf(""),
    isEdit: Boolean = false,
    onCopy: (String) -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        ItemInfo(
            contentState = nameState,
            hintText = "卡片名称",
            isEdit = isEdit,
            onCopy = onCopy
        )
        ItemInfo(
            contentState = usernameState,
            hintText = "卡号",
            isEdit = isEdit,
            onCopy = onCopy
        )
        ItemInfo(
            contentState = passwordState,
            hintText = "密码",
            isEdit = isEdit,
            onCopy = onCopy
        )
        CommentInfo(
            contentState = commentState,
            isEdit = isEdit
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

