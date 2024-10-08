package top.baymaxam.keyvault.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.launch
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.KeyType
import top.baymaxam.keyvault.model.domain.Tag
import top.baymaxam.keyvault.model.domain.toKeyType
import top.baymaxam.keyvault.state.AddScreenModel
import top.baymaxam.keyvault.state.SelectedState
import top.baymaxam.keyvault.ui.component.InfoField
import top.baymaxam.keyvault.ui.component.SearchField
import top.baymaxam.keyvault.ui.component.SelectableTag
import top.baymaxam.keyvault.ui.component.SelectionButton
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.util.errorToast
import top.baymaxam.keyvault.util.infoToast
import top.baymaxam.keyvault.util.successToast
import java.util.Date

/**
 * 添加页填写页
 * @author John
 * @since 03 8月 2024
 */
class AddInputScreen : Screen {

    override val key: ScreenKey
        get() = "Add-Input-Screen"

    @Composable
    override fun Content() {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val navigator = LocalNavigator.currentOrThrow
        val vm = navigator.koinNavigatorScreenModel<AddScreenModel>()
        val tagListState = rememberLazyListState()
        val scope = rememberCoroutineScope()

        val searchState = rememberSaveable { mutableStateOf("") }
        val nameContentState = rememberSaveable { mutableStateOf("") }
        val usernameContentState = rememberSaveable { mutableStateOf("") }
        val passwordContentState = rememberSaveable { mutableStateOf("") }
        val commentContentState = rememberSaveable { mutableStateOf("") }
        val typeSelectedState = rememberSaveable { mutableIntStateOf(0) }

        if (searchState.value.isEmpty()) {
            LaunchedEffect(Unit) {
                vm.tags.refreshState()
                tagListState.scrollToItem(0)
            }
        }

        ContentLayout(
            searchContentState = searchState,
            tags = vm.tags.state,
            typeSelectedState = typeSelectedState,
            nameContentState = nameContentState,
            usernameContentState = usernameContentState,
            passwordContentState = passwordContentState,
            commentContentState = commentContentState,
            selectedItemState = vm.selectedPassItem,
            tagListState = tagListState,
            onSearch = { vm.searchTag(searchState.value) },
            onCancel = { bottomSheetNavigator.hide() },
            onSelectAuth = { navigator += AddAuthScreen() },
            onConfirm = onConfirm@{
                if (typeSelectedState.intValue == 2 && vm.selectedPassItem.value == null) {
                    infoToast("授权条目未设置")
                    return@onConfirm
                }
                val type = typeSelectedState.intValue.toKeyType() ?: error("Invalid type.")
                val item: KeyItem = when (type) {
                    KeyType.Website -> KeyItem(
                        name = nameContentState.value,
                        username = usernameContentState.value,
                        password = passwordContentState.value,
                        comment = commentContentState.value,
                        type = KeyType.Website,
                        createDate = Date(),
                    )

                    KeyType.Card -> KeyItem(
                        name = nameContentState.value,
                        username = usernameContentState.value,
                        password = passwordContentState.value,
                        comment = commentContentState.value,
                        type = KeyType.Card,
                        createDate = Date(),
                    )

                    KeyType.Authorization -> KeyItem(
                        name = nameContentState.value,
                        comment = commentContentState.value,
                        authId = vm.selectedPassItem.value!!.id,
                        authName = vm.selectedPassItem.value!!.name,
                        type = KeyType.Authorization,
                        createDate = Date(),
                    )
                }
                scope.launch {
                    vm.addItem(item)
                        .onFailure { errorToast(it.message) }
                        .onSuccess {
                            successToast("添加条目成功")
                            bottomSheetNavigator.hide()
                        }
                }
            }
        )
    }
}

@Composable
private fun ContentLayout(
    searchContentState: MutableState<String> = mutableStateOf(""),
    tags: List<SelectedState<Tag>> = mutableStateListOf(),
    typeSelectedState: MutableIntState = mutableIntStateOf(0),
    nameContentState: MutableState<String> = mutableStateOf(""),
    usernameContentState: MutableState<String> = mutableStateOf(""),
    passwordContentState: MutableState<String> = mutableStateOf(""),
    commentContentState: MutableState<String> = mutableStateOf(""),
    selectedItemState: MutableState<KeyItem?> = mutableStateOf(null),
    tagListState: LazyListState = rememberLazyListState(),
    onSearch: () -> Unit = {},
    onConfirm: () -> Unit = {},
    onCancel: () -> Unit = {},
    onSelectAuth: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .align(Alignment.TopCenter)
        ) {
            LineHeader(
                modifier = Modifier
                    .padding(vertical = 15.dp)
                    .height(5.dp)
                    .fillMaxWidth(0.4f)
            )

            SearchField(
                contentState = searchContentState,
                placeholder = { Text("搜索标签") },
                onSearch = onSearch,
                modifier = Modifier.fillMaxWidth()
            )
            if (tags.isNotEmpty()) {
                LazyRow(
                    state = tagListState,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    contentPadding = PaddingValues(horizontal = 5.dp),
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .fillMaxWidth()
                ) {
                    items(
                        items = tags,
                        key = { it.value.id }
                    ) {
                        val (item) = it
                        SelectableTag(
                            text = item.name,
                            selected = it.selected,
                            shape = RoundedCornerShape(30),
                            onClick = { it.selected = !it.selected },
                            modifier = Modifier
                                .height(40.dp)
                                .width(80.dp)
                        )
                    }
                }
            } else {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    Text(text = "未找到标签", color = MaterialTheme.colorScheme.onBackground)
                }
            }
            Row(
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "条目类型：", color = MaterialTheme.colorScheme.onBackground)
                TypeSelection(
                    text = "网站",
                    selected = typeSelectedState.intValue == 0,
                    onClick = {
                        typeSelectedState.intValue = 0
                        nameContentState.value = ""
                        usernameContentState.value = ""
                        passwordContentState.value = ""
                        commentContentState.value = ""
                    }
                )
                TypeSelection(
                    text = "卡片",
                    selected = typeSelectedState.intValue == 1,
                    onClick = {
                        typeSelectedState.intValue = 1
                        nameContentState.value = ""
                        usernameContentState.value = ""
                        passwordContentState.value = ""
                        commentContentState.value = ""
                    }
                )
                TypeSelection(
                    text = "授权",
                    selected = typeSelectedState.intValue == 2,
                    onClick = {
                        typeSelectedState.intValue = 2
                        nameContentState.value = ""
                        commentContentState.value = ""
                        selectedItemState.value = null
                    }
                )
            }
            InfoFields(
                typeIndex = typeSelectedState,
                nameState = nameContentState,
                usernameState = usernameContentState,
                passwordState = passwordContentState,
                commentState = commentContentState,
                selectedItemState = selectedItemState,
                onSelectAuth = onSelectAuth
            )
            Spacer(modifier = Modifier.height(5.dp))
        }
        SelectionButton(
            onConfirm = onConfirm,
            onCancel = onCancel,
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun InfoFields(
    typeIndex: MutableIntState = mutableIntStateOf(2),
    nameState: MutableState<String> = mutableStateOf(""),
    usernameState: MutableState<String> = mutableStateOf(""),
    passwordState: MutableState<String> = mutableStateOf(""),
    commentState: MutableState<String> = mutableStateOf(""),
    selectedItemState: MutableState<KeyItem?> = mutableStateOf(null),
    onSelectAuth: () -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        InfoField(
            contentState = nameState,
            placeholder = {
                when (typeIndex.intValue) {
                    0 -> Text("网站名称")
                    1 -> Text("卡片名称")
                    2 -> Text("授权名称")
                }
            },
            leadingIcon = {
                val icon = when (typeIndex.intValue) {
                    0 -> Icons.Rounded.Language
                    1 -> Icons.Rounded.CreditCard
                    2 -> Icons.Rounded.Person
                    else -> null
                }
                if (icon != null) {
                    Icon(imageVector = icon, contentDescription = null)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        if (typeIndex.intValue != 2) {
            InfoField(
                contentState = usernameState,
                placeholder = {
                    when (typeIndex.intValue) {
                        0 -> Text("用户名")
                        1 -> Text("卡号")
                    }
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Rounded.Person, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth()
            )
            InfoField(
                contentState = passwordState,
                placeholder = { Text("密码") },
                leadingIcon = {
                    Icon(imageVector = Icons.Rounded.Key, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
        InfoField(
            contentState = commentState,
            placeholder = { Text("备注") },
            leadingIcon = {
                Icon(imageVector = Icons.Rounded.Description, contentDescription = null)
            },
            modifier = Modifier.fillMaxWidth()
        )
        if (typeIndex.intValue == 2) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .clickable(onClick = onSelectAuth)
            ) {
                OutlinedTextField(
                    value = if (selectedItemState.value != null) selectedItemState.value!!.name else "授权",
                    onValueChange = {},
                    enabled = false,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(50),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedLeadingIconColor = MaterialTheme.colorScheme.onBackground,
                        focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                        disabledBorderColor = MaterialTheme.colorScheme.onBackground,
                        disabledLeadingIconColor = MaterialTheme.colorScheme.onBackground,
                        disabledTextColor = MaterialTheme.colorScheme.onBackground
                    ),
                    leadingIcon = {
                        Icon(imageVector = Icons.Rounded.Language, contentDescription = null)
                    },
                )
            }

        }
    }
}

@Composable
private fun TypeSelection(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(selected = selected, onClick = onClick)
        Text(text = text, color = MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
private fun LineHeader(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val start = Offset(0f, size.height / 2)
        val end = Offset(size.width, size.height / 2)
        drawLine(Color.Gray, start, end, size.height, StrokeCap.Round)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    AppTheme {
        val tags = listOf(
            SelectedState(Tag(name = "Hello0")),
            SelectedState(Tag(name = "Hello1")),
            SelectedState(Tag(name = "Hello2")),
            SelectedState(Tag(name = "Hello3")),
        )
        ContentLayout(
            tags = tags
        )
    }
}

