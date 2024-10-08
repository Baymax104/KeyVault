package top.baymaxam.keyvault.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.KeyType
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.ui.theme.IconColors

/**
 * 授权条目列表
 * @author John
 * @since 03 8月 2024
 */
@Composable
fun AddAuthList(
    items: List<KeyItem>,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    onItemClick: (KeyItem) -> Unit = {}
) {
    LazyColumn(
        state = state,
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(
            items = items,
            key = { it.id }
        ) {
            AddAuthItem(
                item = it,
                onClick = onItemClick
            )
        }
    }
}

@Composable
private fun AddAuthItem(
    item: KeyItem,
    onClick: (KeyItem) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable { onClick(item) }
            .padding(vertical = 10.dp, horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        FillIcon(
            icon = when (item.type) {
                KeyType.Website -> Icons.Rounded.Language
                KeyType.Card -> Icons.Rounded.CreditCard
                else -> throw IllegalArgumentException("Item is authorization type.")
            },
            shape = RoundedCornerShape(20),
            modifier = Modifier.size(40.dp),
            colors = when (item.type) {
                KeyType.Website -> IconColors.WebItem
                KeyType.Card -> IconColors.CardItem
                else -> throw IllegalArgumentException("Item is authorization type.")
            }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp)
        ) {
            Text(
                text = item.name,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = item.username,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        val list = remember {
            mutableStateListOf(
                KeyItem(name = "测试", username = "username"),
                KeyItem(name = "TestCard", username = "code")
            )
        }
        AddAuthList(items = list)
    }
}

