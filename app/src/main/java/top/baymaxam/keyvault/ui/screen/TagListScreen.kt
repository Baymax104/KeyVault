package top.baymaxam.keyvault.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.Tag
import top.baymaxam.keyvault.state.ItemSelectedState
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
        ContentLayout(
            items = listOf(
                ItemSelectedState(Tag(id = 0, name = "Hello")),
                ItemSelectedState(Tag(id = 1, name = "Hello")),
                ItemSelectedState(Tag(id = 2, name = "Hello")),
                ItemSelectedState(Tag(id = 3, name = "Hello")),
                ItemSelectedState(Tag(id = 4, name = "Hello")),
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
            },
            onBack = { navigator.pop() }
        )
    }
}

@Composable
private fun ContentLayout(
    searchContentState: MutableState<String> = mutableStateOf(""),
    items: List<ItemSelectedState<Tag>> = emptyList(),
    onSearch: () -> Unit = {},
    onBack: () -> Unit = {},
    onAddClick: () -> Unit = {},
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
            SearchField(
                contentState = searchContentState,
                placeholder = { Text("搜索标签") },
                onSearch = onSearch,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.inverseOnSurface)
                    .padding(horizontal = 15.dp, vertical = 10.dp)
                    .fillMaxWidth()
            )
            TagList(
                items = items,
                keyItemsFactory = keyItemsFactory,
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
                ItemSelectedState(Tag(id = 0, name = "Hello")),
                ItemSelectedState(Tag(id = 1, name = "Hello")),
                ItemSelectedState(Tag(id = 2, name = "Hello")),
                ItemSelectedState(Tag(id = 3, name = "Hello")),
                ItemSelectedState(Tag(id = 4, name = "Hello")),
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
