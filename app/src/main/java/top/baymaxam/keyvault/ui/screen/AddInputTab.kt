package top.baymaxam.keyvault.ui.screen

import androidx.compose.foundation.Canvas
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import top.baymaxam.keyvault.model.domain.PassItem
import top.baymaxam.keyvault.state.AddScreenModel
import top.baymaxam.keyvault.state.TagItemState
import top.baymaxam.keyvault.ui.component.InfoField
import top.baymaxam.keyvault.ui.component.SearchField
import top.baymaxam.keyvault.ui.component.SelectionButton
import top.baymaxam.keyvault.ui.component.TagItem
import top.baymaxam.keyvault.util.successToast

/**
 * 添加页填写页
 * @author John
 * @since 03 8月 2024
 */
class AddInputTab : Tab {

    override val options: TabOptions
        @Composable
        get() = TabOptions(0u, "")

    @Composable
    override fun Content() {
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val viewModel = rememberScreenModel { AddScreenModel() }
        val tagListState = rememberLazyListState()

        if (viewModel.searchState.value.isEmpty()) {
            LaunchedEffect(key1 = Unit) {
                viewModel.refreshTags()
                tagListState.scrollToItem(0)
            }
        }

        ContentLayout(
            searchState = viewModel.searchState,
            tags = viewModel.tags,
            nameState = viewModel.nameState,
            usernameState = viewModel.usernameState,
            passwordState = viewModel.passwordState,
            commentState = viewModel.commentState,
            tagListState = tagListState,
            onSearch = { viewModel.searchTag() },
            onConfirm = {},
            onCancel = { bottomSheetNavigator.hide() },
            onSelectAuth = { successToast("Hello") }
        )
    }
}

@Composable
private fun ContentLayout(
    searchState: MutableState<String> = mutableStateOf(""),
    tags: SnapshotStateList<TagItemState> = mutableStateListOf(),
    nameState: MutableState<String> = mutableStateOf(""),
    usernameState: MutableState<String> = mutableStateOf(""),
    passwordState: MutableState<String> = mutableStateOf(""),
    commentState: MutableState<String> = mutableStateOf(""),
    tagListState: LazyListState = rememberLazyListState(),
    onSearch: () -> Unit = {},
    onConfirm: () -> Unit = {},
    onCancel: () -> Unit = {},
    onSelectAuth: () -> Unit = {},
) {
    val selectedIndex = remember { mutableIntStateOf(0) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    ) {
        LineHeader(
            modifier = Modifier
                .padding(vertical = 15.dp)
                .height(5.dp)
                .fillMaxWidth(0.4f)
        )

        SearchField(
            placeholder = { Text(text = "搜索标签") },
            content = searchState,
            onSearch = onSearch
        )
        if (tags.size > 0) {
            LazyRow(
                state = tagListState,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 5.dp),
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth()
            ) {
                items(
                    items = tags,
                    key = { it.value.id }
                ) {
                    TagItem(
                        tag = it,
                        onClick = { tag -> tag.selected.value = !tag.selected.value },
                        modifier = Modifier
                            .height(50.dp)
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
                    .height(50.dp)
            ) {
                Text(text = "暂无数据")
            }
        }
        Row(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "条目类型：")
            TypeSelection(
                text = "网站",
                selected = selectedIndex.intValue == 0,
                onClick = { selectedIndex.intValue = 0 }
            )
            TypeSelection(
                text = "卡片",
                selected = selectedIndex.intValue == 1,
                onClick = { selectedIndex.intValue = 1 }
            )
            TypeSelection(
                text = "授权",
                selected = selectedIndex.intValue == 2,
                onClick = { selectedIndex.intValue = 2 }
            )
        }
        when (selectedIndex.intValue) {
            0 -> PassFields(
                selectedIndex = selectedIndex,
                nameState = nameState,
                usernameState = usernameState,
                passwordState = passwordState,
                commentState = commentState
            )

            1 -> PassFields(
                selectedIndex = selectedIndex,
                nameState = nameState,
                usernameState = usernameState,
                passwordState = passwordState,
                commentState = commentState
            )

            2 -> AuthFields(
                nameState = nameState,
                commentState = commentState,
                onSelectAuth = onSelectAuth
            )
        }
        Spacer(modifier = Modifier.height(5.dp))

        SelectionButton(
            onConfirm = onConfirm,
            onCancel = onCancel
        )
    }
}

@Composable
private fun PassFields(
    selectedIndex: MutableIntState = mutableIntStateOf(0),
    nameState: MutableState<String> = mutableStateOf(""),
    usernameState: MutableState<String> = mutableStateOf(""),
    passwordState: MutableState<String> = mutableStateOf(""),
    commentState: MutableState<String> = mutableStateOf("")
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        InfoField(
            content = nameState,
            placeholder = {
                when (selectedIndex.intValue) {
                    0 -> Text(text = "网站名称")
                    1 -> Text(text = "卡片名称")
                }
            },
            leadingIcon = {
                val icon = when (selectedIndex.intValue) {
                    0 -> Icons.Rounded.Language
                    1 -> Icons.Rounded.CreditCard
                    else -> null
                }
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                    )
                }
            }
        )
        InfoField(
            content = usernameState,
            placeholder = {
                when (selectedIndex.intValue) {
                    0 -> Text(text = "用户名")
                    1 -> Text(text = "卡号")
                }
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = null,
                )
            }
        )
        InfoField(
            content = passwordState,
            placeholder = { Text(text = "密码") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Key,
                    contentDescription = null,
                )
            }
        )
        InfoField(
            content = commentState,
            placeholder = { Text(text = "备注") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Description,
                    contentDescription = null,
                )
            }
        )
    }
}

@Composable
private fun AuthFields(
    nameState: MutableState<String> = mutableStateOf(""),
    commentState: MutableState<String> = mutableStateOf(""),
    authState: MutableState<PassItem?> = mutableStateOf(null),
    onSelectAuth: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        InfoField(
            content = nameState,
            placeholder = {
                Text(text = "授权名称")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = null,
                )
            }
        )
        InfoField(
            content = commentState,
            placeholder = { Text(text = "备注") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Description,
                    contentDescription = null,
                )
            }
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .clickable(onClick = onSelectAuth)
        ) {
            OutlinedTextField(
                value = if (authState.value != null) authState.value!!.name else "授权网站",
                onValueChange = {},
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedLeadingIconColor = Color.Black,
                    focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                    disabledBorderColor = Color.Black,
                    disabledLeadingIconColor = Color.Black,
                    disabledTextColor = Color.Black
                ),
                singleLine = true,
                leadingIcon = {
                    Icon(imageVector = Icons.Rounded.Language, contentDescription = null)
                },
            )
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
        Text(text = text)
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
