@file:OptIn(ExperimentalFoundationApi::class)

package top.baymaxam.keyvault.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastJoinToString
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.Tag
import top.baymaxam.keyvault.state.SelectedState
import top.baymaxam.keyvault.ui.theme.AppTheme

/**
 * 标签列表
 * @author John
 * @since 13 9月 2024
 */
@Composable
fun TagList(
    items: List<SelectedState<Tag>>,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    editableState: MutableState<Boolean> = mutableStateOf(false),
    onItemClick: (Tag) -> Unit = {},
    onItemSelected: (SelectedState<Tag>) -> Unit = {},
    keyItemsFactory: (Tag) -> List<KeyItem> = { emptyList() }
) {
    LazyColumn(
        state = state,
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 5.dp)
    ) {
        items(
            items = items,
            key = { it.value.id }
        ) {
            TagListItem(
                item = it,
                keyItems = keyItemsFactory(it.value),
                editableState = editableState,
                onClick = onItemClick,
                onSelected = onItemSelected
            )
        }
    }
}

@Composable
private fun TagListItem(
    item: SelectedState<Tag>,
    keyItems: List<KeyItem>,
    editableState: MutableState<Boolean> = mutableStateOf(false),
    onClick: (Tag) -> Unit = {},
    onSelected: (SelectedState<Tag>) -> Unit = {}
) {
    val (tag) = item
    val itemName = keyItems.map { it.name }.fastJoinToString()
    val select = {
        item.selected = !item.selected
        onSelected(item)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .combinedClickable(
                onLongClick = { if (!editableState.value) select() },
                onClick = { if (editableState.value) select() else onClick(tag) }
            )
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(horizontal = 10.dp)
        ) {
            Text(text = "${tag.name}  (${keyItems.size})", fontWeight = FontWeight.Bold)
            Text(text = itemName, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        if (editableState.value) {
            RadioButton(
                selected = item.selected,
                onClick = select
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
//        TagListItem(
//            item = ItemSelectedState(Tag(id = 0, name = "Hello")),
//            keyItems = listOf(
//                KeyItem(name = "Hello1"),
//                KeyItem(name = "Hello2"),
//                KeyItem(name = "Hello3"),
//                KeyItem(name = "Hello4"),
//                KeyItem(name = "Hello5"),
//                KeyItem(name = "Hello5"),
//                KeyItem(name = "Hello5"),
//                KeyItem(name = "Hello5"),
//            )
//        )
        TagList(
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

