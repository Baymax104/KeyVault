package top.baymaxam.keyvault.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import org.koin.androidx.compose.koinViewModel
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.Tag
import top.baymaxam.keyvault.state.SelectedState
import top.baymaxam.keyvault.state.TagListViewModel
import top.baymaxam.keyvault.ui.component.FloatingButton
import top.baymaxam.keyvault.ui.component.SearchField
import top.baymaxam.keyvault.ui.component.TagList
import top.baymaxam.keyvault.ui.component.TopBackBar
import top.baymaxam.keyvault.ui.theme.AppTheme

/**
 * 标签列表页
 * @author John
 * @since 12 9月 2024
 */
class TagListScreen : Screen {

    override val key: ScreenKey
        get() = "Tag-List-Screen"

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.root
        val vm = koinViewModel<TagListViewModel>()
        val editableState = remember { mutableStateOf(false) }
        val searchContentState = remember { mutableStateOf("") }
        val selectedNumberState = remember { mutableIntStateOf(0) }

        fun clearEditState() {
            editableState.value = false
            selectedNumberState.intValue = 0
            vm.tags.forEach { it.selected = false }
        }

        BackHandler(editableState.value) {
            clearEditState()
        }

        ContentLayout(
            items = vm.tags,
            keyItemsFactory = { vm.getTagItems(it) },
            editableState = editableState,
            searchContentState = searchContentState,
            selectedNumberState = selectedNumberState,
            onSearch = {},
            onBack = {
                if (editableState.value) {
                    clearEditState()
                } else {
                    navigator.pop()
                }
            },
            onAddClick = {},
            onDeleteClick = {},
            onItemClick = {},
            onItemSelected = {
                selectedNumberState.intValue += if (it.selected) 1 else -1
            }
        )
    }
}

@Composable
private fun ContentLayout(
    searchContentState: MutableState<String> = mutableStateOf(""),
    editableState: MutableState<Boolean> = mutableStateOf(false),
    selectedNumberState: MutableIntState = mutableIntStateOf(0),
    items: List<SelectedState<Tag>> = emptyList(),
    onSearch: () -> Unit = {},
    onBack: () -> Unit = {},
    onAddClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onItemClick: (Tag) -> Unit = {},
    onItemSelected: (SelectedState<Tag>) -> Unit = {},
    keyItemsFactory: (Tag) -> List<KeyItem> = { emptyList() }
) {
    Scaffold(
        topBar = { TopBackBar(title = "条目标签", onBack = onBack) },
        floatingActionButton = {
            FloatingButton(
                icon = Icons.Rounded.Add,
                modifier = Modifier.padding(end = 15.dp, bottom = 25.dp),
                onClick = onAddClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (!editableState.value) {
                SearchField(
                    contentState = searchContentState,
                    placeholder = { Text("搜索标签") },
                    onSearch = onSearch,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.inverseOnSurface)
                        .padding(horizontal = 15.dp, vertical = 10.dp)
                        .fillMaxWidth()
                )
            } else {
                Column(
                    modifier = Modifier.background(MaterialTheme.colorScheme.inverseOnSurface)
                ) {
                    Spacer(modifier = Modifier.height(26.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .padding(vertical = 5.dp, horizontal = 10.dp)
                    ) {
                        Text(
                            text = "已选：${selectedNumberState.intValue}项，共${items.size}项",
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = onDeleteClick) {
                            Icon(
                                imageVector = Icons.Rounded.Delete,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .height(2.dp)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary)
                    )

                }
            }
            TagList(
                items = items,
                keyItemsFactory = keyItemsFactory,
                editableState = editableState,
                onItemClick = onItemClick,
                onItemSelected = onItemSelected,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    AppTheme {
        ContentLayout(
            items = listOf(
                SelectedState(Tag(name = "Hello")),
                SelectedState(Tag(name = "Hello")),
                SelectedState(Tag(name = "Hello")),
                SelectedState(Tag(name = "Hello")),
                SelectedState(Tag(name = "Hello")),
            ),
            keyItemsFactory = {
                listOf(
                    KeyItem(name = "Hello1"),
                    KeyItem(name = "Hello2"),
                    KeyItem(name = "Hello3"),
                    KeyItem(name = "Hello4"),
                    KeyItem(name = "Hello5"),
                    KeyItem(name = "Hello5"),
                    KeyItem(name = "Hello5"),
                    KeyItem(name = "Hello5"),
                )
            }
        )
    }
}
