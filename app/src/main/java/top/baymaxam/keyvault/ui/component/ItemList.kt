package top.baymaxam.keyvault.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.baymaxam.keyvault.model.domain.AuthItem
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.UserItem
import top.baymaxam.keyvault.state.SelectedState
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.ui.theme.IconColors

/**
 * 条目列表
 * @author John
 * @since 07 8月 2024
 */
@Composable
fun ItemList(
    items: List<SelectedState<KeyItem>>,
    modifier: Modifier = Modifier,
    isEditable: Boolean = false,
    onItemClick: (KeyItem) -> Unit = {},
    onItemCopy: (UserItem) -> Unit = {},
    onSelected: (SelectedState<KeyItem>) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(vertical = 10.dp, horizontal = 5.dp)
    ) {
        items(
            items = items,
            key = { it.value.id }
        ) {
            KeyItemLayout(
                item = it,
                isEditable = isEditable,
                onClick = onItemClick,
                onCopy = onItemCopy,
                onSelected = onSelected
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun KeyItemLayout(
    item: SelectedState<KeyItem>,
    isEditable: Boolean = false,
    onClick: (KeyItem) -> Unit = {},
    onCopy: (UserItem) -> Unit = {},
    onSelected: (SelectedState<KeyItem>) -> Unit = {},
) {
    val (keyItem) = item
    val select = {
        item.selected = !item.selected
        onSelected(item)
    }
    Surface(
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 1.dp,
        tonalElevation = 0.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onLongClick = { if (!isEditable) select() },
                    onClick = { if (isEditable) select() else onClick(keyItem) }
                )
                .padding(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                FillIcon(
                    icon = when (keyItem) {
                        is UserItem -> Icons.Rounded.CreditCard
                        is AuthItem -> Icons.Rounded.Person
                    },
                    shape = RoundedCornerShape(20),
                    modifier = Modifier.size(45.dp),
                    colors = when (keyItem) {
                        is UserItem -> IconColors.UserItem
                        is AuthItem -> IconColors.AuthItem
                    }
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 10.dp)
                ) {
                    Text(
                        text = keyItem.name,
                        style = TextStyle(
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = when (keyItem) {
                            is UserItem -> keyItem.username
                            is AuthItem -> keyItem.authName
                        },
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Gray
                        )
                    )
                }

                if (isEditable) {
                    RadioButton(
                        selected = item.selected,
                        onClick = select
                    )
                } else if (keyItem is UserItem) {
                    IconButton(onClick = { onCopy(keyItem) }) {
                        Icon(imageVector = Icons.Rounded.ContentCopy, contentDescription = null)
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        val list = listOf(
            SelectedState(UserItem(name = "Hello")),
            SelectedState(UserItem(name = "Hello")),
            SelectedState(AuthItem(name = "Hello")),
            SelectedState(AuthItem(name = "Hello")),
            SelectedState(UserItem(name = "Hello")),
            SelectedState(AuthItem(name = "Hello")),
        )
        ItemList(items = list)
    }
}


