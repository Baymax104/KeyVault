package top.baymaxam.keyvault.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.baymaxam.keyvault.model.domain.AuthKeyItem
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.PassKeyItem
import top.baymaxam.keyvault.model.domain.PassType
import top.baymaxam.keyvault.ui.theme.AppTheme

/**
 * 最近使用密码列表
 * @author John
 * @since 27 6月 2024
 */

@Composable
fun ResentUsedList(
    keyItems: SnapshotStateList<KeyItem>,
    onItemClick: (KeyItem) -> Unit,
    onItemCopy: (PassKeyItem) -> Unit,
    modifier: Modifier = Modifier
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
            ResentUsedItem(
                keyItem = it,
                onClick = onItemClick,
                onCopy = onItemCopy
            )
        }
    }
}

@Composable
private fun ResentUsedItem(
    keyItem: KeyItem,
    onClick: (KeyItem) -> Unit = {},
    onCopy: (PassKeyItem) -> Unit = {}
) {
    ElevatedCard(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth(),
        onClick = { onClick(keyItem) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            when (keyItem) {
                is PassKeyItem -> PassItemLayout(item = keyItem, onCopy = onCopy)
                is AuthKeyItem -> AuthItemLayout(item = keyItem)
            }
        }
    }
}

@Composable
private fun RowScope.PassItemLayout(
    item: PassKeyItem,
    onCopy: (PassKeyItem) -> Unit
) {
    FillIcon(
        icon = when (item.type) {
            PassType.Website -> Icons.Rounded.Language
            PassType.Card -> Icons.Rounded.CreditCard
        },
        iconColor = MaterialTheme.colorScheme.primary,
        iconBackgroundColor = Color(0xffd6ecff),
        shape = RoundedCornerShape(20),
        modifier = Modifier.size(30.dp)
    )

    Column(
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 10.dp)
    ) {
        Text(
            text = item.name,
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = item.username,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray
            )
        )
    }

    IconButton(onClick = { onCopy(item) }) {
        Icon(imageVector = Icons.Rounded.ContentCopy, contentDescription = null)
    }
}

@Composable
private fun RowScope.AuthItemLayout(
    item: AuthKeyItem,
) {
    FillIcon(
        icon = Icons.Rounded.Person,
        iconColor = MaterialTheme.colorScheme.primary,
        iconBackgroundColor = Color(0xffd6ecff),
        shape = RoundedCornerShape(20),
        modifier = Modifier.size(30.dp)
    )

    Column(
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 10.dp)
    ) {
        Text(
            text = item.name,
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = item.authorization.username,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        val list = remember {
            mutableStateListOf<KeyItem>(
                PassKeyItem(id = 0, name = "测试", username = "username"),
                PassKeyItem(id = 1, name = "TestCard", username = "code")
            )
        }
        ResentUsedList(
            keyItems = list,
            onItemCopy = {},
            onItemClick = {}
        )
    }
}
