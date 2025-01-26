@file:OptIn(ExperimentalFoundationApi::class)

package top.baymaxam.keyvault.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.baymaxam.keyvault.R
import top.baymaxam.keyvault.model.domain.Tag
import top.baymaxam.keyvault.state.SelectedState
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.ui.theme.IconColors

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
    isEditable: Boolean = false,
    onItemClick: (Tag) -> Unit = {},
    onItemSelected: (SelectedState<Tag>) -> Unit = {},
) {
    LazyColumn(
        state = state,
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = 5.dp)
    ) {
        items(
            items = items,
            key = { it.value.id }
        ) {
            TagListItem(
                item = it,
                isEditable = isEditable,
                onClick = onItemClick,
                onSelected = onItemSelected
            )
        }
    }
}

@Composable
private fun TagListItem(
    item: SelectedState<Tag>,
    isEditable: Boolean = false,
    onClick: (Tag) -> Unit = {},
    onSelected: (SelectedState<Tag>) -> Unit = {},
) {
    val (tag) = item
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onLongClick = { if (!isEditable) onSelected(item) },
                onClick = { if (isEditable) onSelected(item) else onClick(tag) }
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 10.dp, top = 15.dp, bottom = 15.dp)
                .align(Alignment.CenterStart)
        ) {
            FillIcon(
                icon = painterResource(R.drawable.ic_tag),
                colors = IconColors.IndexTag,
                shape = RoundedCornerShape(10.dp)
            )
            Text(
                text = tag.name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 10.dp)
            )
        }
        if (isEditable) {
            RadioButton(
                selected = item.selected,
                onClick = { onSelected(item) },
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        TagList(
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

