package top.baymaxam.keyvault.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import org.koin.androidx.compose.koinViewModel
import top.baymaxam.keyvault.model.domain.Tag
import top.baymaxam.keyvault.state.SelectedState
import top.baymaxam.keyvault.state.TagListViewModel
import top.baymaxam.keyvault.ui.component.FloatingButton
import top.baymaxam.keyvault.ui.component.TagList
import top.baymaxam.keyvault.ui.component.TopBackBar
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.util.root

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
        var isEditable by remember { mutableStateOf(false) }

        if (!isEditable) {
            vm.tags.forEach { it.selected = false }
        }

        BackHandler(isEditable) {
            isEditable = false
        }

        ContentLayout(
            items = vm.tags,
            isEditable = isEditable,
            onBack = { if (isEditable) isEditable = false else navigator.pop() },
            onAddClick = {},
            onEditClick = { isEditable = !isEditable },
            onDeleteClick = {},
            onItemClick = {},
            onItemSelected = {
                isEditable = true
                it.selected = !it.selected
            }
        )
    }
}

@Composable
private fun ContentLayout(
    isEditable: Boolean = false,
    items: List<SelectedState<Tag>> = emptyList(),
    onBack: () -> Unit = {},
    onAddClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onItemClick: (Tag) -> Unit = {},
    onItemSelected: (SelectedState<Tag>) -> Unit = {},
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
                Text("条目标签")
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
                .fillMaxSize()
        ) {
            TagList(
                items = items,
                modifier = Modifier.weight(1f),
                isEditable = isEditable,
                onItemClick = onItemClick,
                onItemSelected = onItemSelected
            )
            if (isEditable) {
                EditBar(
                    items = items,
                    onDeleteClick = onDeleteClick,
                )
            }
        }
    }
}

@Composable
private fun EditBar(
    items: List<SelectedState<Tag>> = emptyList(),
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

@Preview(showBackground = true)
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
            isEditable = true
        )
    }
}
