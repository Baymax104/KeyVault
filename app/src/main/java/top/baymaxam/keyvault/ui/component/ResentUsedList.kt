package top.baymaxam.keyvault.ui.component

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
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.KeyType
import top.baymaxam.keyvault.model.domain.Tag
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.ui.theme.IconColors

/**
 * 最近使用条目列表
 * @author John
 * @since 27 6月 2024
 */

@Composable
fun ResentUsedList(
    modifier: Modifier = Modifier,
    keyItems: List<KeyItem> = mutableListOf(),
    onItemClick: (KeyItem) -> Unit = {},
    onLoadTags: (KeyItem) -> List<Tag> = { emptyList() }
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(vertical = 10.dp)
    ) {
        items(
            items = keyItems,
            key = { it.id }
        ) {
            RecentUsedItem(
                item = it,
                tags = onLoadTags(it),
                onClick = onItemClick
            )
        }
    }
}


@Composable
private fun RecentUsedItem(
    item: KeyItem = KeyItem(),
    tags: List<Tag> = mutableListOf(),
    onClick: (KeyItem) -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 0.dp,
        shadowElevation = 1.dp,
        onClick = { onClick(item) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FillIcon(
                    icon = when (item.type) {
                        KeyType.Website -> Icons.Rounded.Language
                        KeyType.Card -> Icons.Rounded.CreditCard
                        KeyType.Authorization -> Icons.Rounded.Person
                    },
                    shape = RoundedCornerShape(20),
                    modifier = Modifier.size(45.dp),
                    colors = when (item.type) {
                        KeyType.Website -> IconColors.WebItem
                        KeyType.Card -> IconColors.CardItem
                        KeyType.Authorization -> IconColors.AuthItem
                    }
                )

                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .height(45.dp)
                        .padding(horizontal = 10.dp)
                ) {
                    Text(
                        text = item.name,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    )
                    Text(
                        text = when (item.type) {
                            KeyType.Authorization -> item.authorization?.name ?: ""
                            else -> item.username
                        },
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Gray
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (tags.isNotEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        tags.take(3).forEach {
                            SelectableTag(
                                text = it.name,
                                enabled = false,
                                shape = RoundedCornerShape(50),
                            )
                        }
                    }
                } else {
                    Text(text = "暂无标签", color = Color.Gray, fontSize = 14.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        val list = remember {
            mutableStateListOf(
                KeyItem(id = 0, name = "测试", username = "username", type = KeyType.Website),
                KeyItem(id = 1, name = "TestCard", username = "code", type = KeyType.Card),
                KeyItem(id = 2, name = "TestCard", username = "code", type = KeyType.Authorization),
            )
        }
        ResentUsedList(
            keyItems = list,
            onLoadTags = { emptyList() },
        )
    }
}
