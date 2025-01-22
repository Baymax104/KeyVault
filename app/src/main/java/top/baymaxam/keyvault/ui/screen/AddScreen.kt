package top.baymaxam.keyvault.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.bottomsheet.spec.DestinationStyleBottomSheet
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.AddSelectAuthScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultRecipient
import com.ramcosta.composedestinations.result.onResult
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import top.baymaxam.keyvault.model.domain.KeyType
import top.baymaxam.keyvault.model.domain.Tag
import top.baymaxam.keyvault.model.domain.UserItem
import top.baymaxam.keyvault.state.AddInputViewModel
import top.baymaxam.keyvault.state.SelectedState
import top.baymaxam.keyvault.ui.component.CommentField
import top.baymaxam.keyvault.ui.component.InfoField
import top.baymaxam.keyvault.ui.component.SearchField
import top.baymaxam.keyvault.ui.component.SelectAuthButton
import top.baymaxam.keyvault.ui.component.SelectableTag
import top.baymaxam.keyvault.ui.component.SelectionButton
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.util.AddGraph
import top.baymaxam.keyvault.util.LocalNavigator
import top.baymaxam.keyvault.util.NavigatorProvider
import top.baymaxam.keyvault.util.currentOrThrow
import top.baymaxam.keyvault.util.errorToast
import top.baymaxam.keyvault.util.successToast

/**
 * 添加页
 * @author John
 * @since 03 8月 2024
 */
@Destination<RootGraph>(style = DestinationStyleBottomSheet::class)
@Composable
fun AddScreen(navigator: DestinationsNavigator) {
    NavigatorProvider(navigator) {
        DestinationsNavHost(NavGraphs.add)
    }
}

@Destination<AddGraph>(start = true)
@Composable
fun AddInputScreen(
    navigator: DestinationsNavigator,
    authRecipient: ResultRecipient<AddSelectAuthScreenDestination, UserItem>
) {
    val rootNavigator = LocalNavigator.currentOrThrow
    val vm = koinViewModel<AddInputViewModel>()
    val tagListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val searchState = rememberSaveable { mutableStateOf("") }

    authRecipient.onResult { vm.selectedUserItemState.value = it }

    if (searchState.value.isEmpty()) {
        LaunchedEffect(Unit) {
            vm.tags.refreshState()
            tagListState.scrollToItem(0)
        }
    }

    ContentLayout(
        searchContentState = searchState,
        tags = vm.tags.state,
        typeSelectedState = vm.typeSelectedState,
        nameContentState = vm.nameContentState,
        usernameContentState = vm.usernameContentState,
        passwordContentState = vm.passwordContentState,
        commentContentState = vm.commentContentState,
        selectedItemState = vm.selectedUserItemState,
        tagListState = tagListState,
        onSearch = { vm.searchTag(searchState.value) },
        onCancel = { rootNavigator.navigateUp() },
        onSelectAuth = { navigator.navigate(AddSelectAuthScreenDestination) },
        onConfirm = {
            scope.launch {
                vm.addItem()
                    .onFailure { errorToast(it.message) }
                    .onSuccess {
                        successToast("添加条目成功")
                        rootNavigator.navigateUp()
                    }
            }
        }
    )
}

@Composable
private fun ContentLayout(
    searchContentState: MutableState<String> = mutableStateOf(""),
    tags: List<SelectedState<Tag>> = mutableStateListOf(),
    typeSelectedState: MutableState<KeyType> = mutableStateOf(KeyType.User),
    nameContentState: MutableState<String> = mutableStateOf(""),
    usernameContentState: MutableState<String> = mutableStateOf(""),
    passwordContentState: MutableState<String> = mutableStateOf(""),
    commentContentState: MutableState<String> = mutableStateOf(""),
    selectedItemState: MutableState<UserItem?> = mutableStateOf(null),
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
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TypeSelection(
                        text = "用户",
                        selected = typeSelectedState.value == KeyType.User,
                        onClick = {
                            typeSelectedState.value = KeyType.User
                            nameContentState.value = ""
                            usernameContentState.value = ""
                            passwordContentState.value = ""
                            commentContentState.value = ""
                        }
                    )

                    TypeSelection(
                        text = "授权",
                        selected = typeSelectedState.value == KeyType.Authorization,
                        onClick = {
                            typeSelectedState.value = KeyType.Authorization
                            nameContentState.value = ""
                            commentContentState.value = ""
                            selectedItemState.value = null
                        }
                    )
                }
            }
            InfoFields(
                selectedType = typeSelectedState.value,
                nameState = nameContentState,
                usernameState = usernameContentState,
                passwordState = passwordContentState,
                commentState = commentContentState,
                selectedItemState = selectedItemState,
                onSelectAuth = onSelectAuth
            )
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
    selectedType: KeyType = KeyType.User,
    nameState: MutableState<String> = mutableStateOf(""),
    usernameState: MutableState<String> = mutableStateOf(""),
    passwordState: MutableState<String> = mutableStateOf(""),
    commentState: MutableState<String> = mutableStateOf(""),
    selectedItemState: MutableState<UserItem?> = mutableStateOf(null),
    onSelectAuth: () -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        InfoField(
            contentState = nameState,
            placeholder = {
                when (selectedType) {
                    KeyType.User -> Text("条目名称")
                    KeyType.Authorization -> Text("授权名称")
                }
            },
            leadingIcon = {
                Icon(
                    imageVector = when (selectedType) {
                        KeyType.User -> Icons.Rounded.CreditCard
                        KeyType.Authorization -> Icons.Rounded.Person
                    },
                    contentDescription = null
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        if (selectedType == KeyType.User) {
            InfoField(
                contentState = usernameState,
                placeholder = {
                    Text("用户名")
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
        } else {
            SelectAuthButton(
                value = selectedItemState.value?.name ?: "选择授权",
                onClick = onSelectAuth
            )
        }
        CommentField(
            contentState = commentState,
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth()
        )
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

