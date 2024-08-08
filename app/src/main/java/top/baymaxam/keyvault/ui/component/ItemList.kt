package top.baymaxam.keyvault.ui.component

import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.KeyType
import top.baymaxam.keyvault.state.ItemSelectedState
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.ui.theme.IconColors

/**
 * 条目列表
 * @author John
 * @since 07 8月 2024
 */
@Composable
fun ItemList(
    items: List<ItemSelectedState<KeyItem>>,
    modifier: Modifier = Modifier,
    editableState: MutableState<Boolean> = mutableStateOf(false),
    onItemClick: (KeyItem) -> Unit = {},
    onItemCopy: (KeyItem) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(vertical = 10.dp)
    ) {
        items(
            items = items,
            key = { it.value.id }
        ) { item ->
            KeyItemLayout(
                item = item,
                editableState = editableState,
                onClick = onItemClick,
                onCopy = onItemCopy
            )
        }
    }
}


@Composable
private fun KeyItemLayout(
    item: ItemSelectedState<KeyItem>,
    editableState: MutableState<Boolean> = mutableStateOf(false),
    onClick: (KeyItem) -> Unit = {},
    onCopy: (KeyItem) -> Unit = {},
) {
    val (keyItem, selectedState) = item
    Surface(
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 1.dp,
        tonalElevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        if (!editableState.value) {
                            editableState.value = true
                            selectedState.value = true
                        }
                    }
                )
            },
        onClick = {
            if (editableState.value) {
                selectedState.value = !selectedState.value
            } else {
                onClick(keyItem)
            }
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FillIcon(
                icon = when (keyItem.type) {
                    KeyType.Website -> Icons.Rounded.Language
                    KeyType.Card -> Icons.Rounded.CreditCard
                    KeyType.Authorization -> Icons.Rounded.Person
                },
                shape = RoundedCornerShape(20),
                modifier = Modifier.size(40.dp),
                colors = when (keyItem.type) {
                    KeyType.Website -> IconColors.WebItem
                    KeyType.Card -> IconColors.CardItem
                    KeyType.Authorization -> IconColors.AuthItem
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
                    text = when (keyItem.type) {
                        KeyType.Authorization -> keyItem.authorization?.name ?: ""
                        else -> keyItem.username
                    },
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    )
                )
            }

            if (editableState.value) {
                RadioButton(
                    selected = selectedState.value,
                    onClick = { selectedState.value = !selectedState.value }
                )
            } else if (keyItem.type != KeyType.Authorization) {
                IconButton(onClick = { onCopy(keyItem) }) {
                    Icon(imageVector = Icons.Rounded.ContentCopy, contentDescription = null)
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    AppTheme {
        val list = listOf(
            ItemSelectedState(KeyItem(id = 0, name = "Hello")),
            ItemSelectedState(KeyItem(id = 1, name = "Hello")),
            ItemSelectedState(KeyItem(id = 2, name = "Hello")),
            ItemSelectedState(KeyItem(id = 3, name = "Hello")),
            ItemSelectedState(KeyItem(id = 4, name = "Hello")),
            ItemSelectedState(KeyItem(id = 5, name = "Hello")),
            ItemSelectedState(KeyItem(id = 6, name = "Hello")),
            ItemSelectedState(KeyItem(id = 7, name = "Hello")),
        )
        ItemList(items = list)
    }
}


