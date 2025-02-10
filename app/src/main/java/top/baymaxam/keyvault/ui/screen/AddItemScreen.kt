package top.baymaxam.keyvault.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.AssistChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.bottomsheet.spec.DestinationStyleBottomSheet
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.AddItemAddTagScreenDestination
import com.ramcosta.composedestinations.generated.destinations.AddItemSelectUserItemScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultRecipient
import com.ramcosta.composedestinations.result.onResult
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import top.baymaxam.keyvault.model.domain.KeyType
import top.baymaxam.keyvault.model.domain.Tag
import top.baymaxam.keyvault.model.domain.UserItem
import top.baymaxam.keyvault.state.SelectedState
import top.baymaxam.keyvault.ui.component.CommentField
import top.baymaxam.keyvault.ui.component.EntryButton
import top.baymaxam.keyvault.ui.component.InputField
import top.baymaxam.keyvault.ui.component.LineHeader
import top.baymaxam.keyvault.ui.component.SearchField
import top.baymaxam.keyvault.ui.component.SelectionButton
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.util.AddItemGraph
import top.baymaxam.keyvault.util.LocalNavigator
import top.baymaxam.keyvault.util.NavigatorProvider
import top.baymaxam.keyvault.util.errorToast
import top.baymaxam.keyvault.util.successToast
import top.baymaxam.keyvault.vm.AddItemViewModel

/**
 * 添加页
 * @author John
 * @since 03 8月 2024
 */
@Destination<RootGraph>(style = DestinationStyleBottomSheet::class)
@Composable
fun AddItemScreen(navigator: DestinationsNavigator) {
    NavigatorProvider(navigator) {
        DestinationsNavHost(NavGraphs.addItem)
    }
}

@Destination<AddItemGraph>(start = true)
@Composable
fun AddInputScreen(
    navigator: DestinationsNavigator,
    authRecipient: ResultRecipient<AddItemSelectUserItemScreenDestination, UserItem>
) {
    val rootNavigator = LocalNavigator.current
    val vm = koinViewModel<AddItemViewModel>()
    val tagListState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val searchState = rememberSaveable { mutableStateOf("") }

    authRecipient.onResult { vm.selectedUserItem = it }

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
        selectedUserItem = vm.selectedUserItem,
        tagListState = tagListState,
        onSearch = { vm.searchTag(searchState.value) },
        onCancel = { rootNavigator.navigateUp() },
        onSelectAuth = { navigator.navigate(AddItemSelectUserItemScreenDestination) },
        onTagAddClick = { navigator.navigate(AddItemAddTagScreenDestination) },
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
    selectedUserItem: UserItem? = null,
    tagListState: LazyListState = rememberLazyListState(),
    onSearch: () -> Unit = {},
    onConfirm: () -> Unit = {},
    onCancel: () -> Unit = {},
    onSelectAuth: () -> Unit = {},
    onTagAddClick: () -> Unit = {},
) {
    val pagerState = rememberPagerState { 2 }
    val scope = rememberCoroutineScope()
    Surface(color = MaterialTheme.colorScheme.background) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
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
                LazyRow(
                    state = tagListState,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    contentPadding = PaddingValues(horizontal = 5.dp),
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    item {
                        AssistChip(
                            onClick = onTagAddClick,
                            label = { Icon(Icons.Rounded.Add, contentDescription = null) }
                        )
                    }
                    items(
                        items = tags,
                        key = { it.value.id }
                    ) {
                        val (item) = it
                        FilterChip(
                            selected = it.selected,
                            onClick = { it.selected = !it.selected },
                            label = { Text(item.name) }
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(bottom = 15.dp)
                        .fillMaxWidth(),
                ) {
                    Text(text = "条目类型：", color = MaterialTheme.colorScheme.onBackground)
                    SingleChoiceSegmentedButtonRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp)
                    ) {
                        SegmentedButton(
                            selected = typeSelectedState.value == KeyType.User,
                            shape = SegmentedButtonDefaults.itemShape(0, 2),
                            onClick = {
                                typeSelectedState.value = KeyType.User
                                scope.launch { pagerState.animateScrollToPage(0) }
                            }
                        ) {
                            Text("用户")
                        }

                        SegmentedButton(
                            selected = typeSelectedState.value == KeyType.Authorization,
                            shape = SegmentedButtonDefaults.itemShape(1, 2),
                            onClick = {
                                typeSelectedState.value = KeyType.Authorization
                                scope.launch { pagerState.animateScrollToPage(1) }
                            }
                        ) {
                            Text("授权")
                        }
                    }
                }
                HorizontalPager(
                    state = pagerState,
                    userScrollEnabled = false,
                    verticalAlignment = Alignment.Top,
                    pageSpacing = 10.dp
                ) {
                    when (it) {
                        0 -> UserInfoFields(
                            nameState = nameContentState,
                            usernameState = usernameContentState,
                            passwordState = passwordContentState,
                            commentState = commentContentState
                        )

                        1 -> AuthInfoFields(
                            nameState = nameContentState,
                            commentState = commentContentState,
                            selectedUserItem = selectedUserItem,
                            onSelectAuth = onSelectAuth
                        )
                    }
                }
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
}

@Composable
private fun UserInfoFields(
    nameState: MutableState<String> = mutableStateOf(""),
    usernameState: MutableState<String> = mutableStateOf(""),
    passwordState: MutableState<String> = mutableStateOf(""),
    commentState: MutableState<String> = mutableStateOf(""),
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        InputField(
            contentState = nameState,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("条目名称") },
            leadingIcon = { Icon(Icons.Rounded.CreditCard, contentDescription = null) }
        )
        InputField(
            contentState = usernameState,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("用户名") },
            leadingIcon = { Icon(Icons.Rounded.Person, contentDescription = null) }
        )
        InputField(
            contentState = passwordState,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("密码") },
            leadingIcon = { Icon(Icons.Rounded.Key, contentDescription = null) }
        )
        CommentField(
            contentState = commentState,
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth()
        )
    }
}


@Composable
private fun AuthInfoFields(
    nameState: MutableState<String> = mutableStateOf(""),
    commentState: MutableState<String> = mutableStateOf(""),
    selectedUserItem: UserItem? = null,
    onSelectAuth: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        InputField(
            contentState = nameState,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("授权名称") },
            leadingIcon = { Icon(Icons.Rounded.Person, contentDescription = null) }
        )
        EntryButton(
            value = selectedUserItem?.name ?: "选择授权",
            onClick = onSelectAuth,
            leadingIcon = { Icon(Icons.Rounded.CreditCard, contentDescription = null) }
        )
        CommentField(
            contentState = commentState,
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth()
        )
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

