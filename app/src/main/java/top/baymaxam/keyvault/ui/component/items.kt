package top.baymaxam.keyvault.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.baymaxam.keyvault.R
import top.baymaxam.keyvault.model.domain.AuthItem
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.Tag
import top.baymaxam.keyvault.model.domain.UserItem
import top.baymaxam.keyvault.state.SelectedState
import top.baymaxam.keyvault.ui.theme.IconColors

/**
 * 列表项
 * @author John
 * @since 06 2月 2025
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TagItemLayout(
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


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun KeyItemLayout(
    item: SelectedState<KeyItem>,
    isEditable: Boolean = false,
    onClick: (KeyItem) -> Unit = {},
    onCopy: (UserItem) -> Unit = {},
    onSelected: (SelectedState<KeyItem>) -> Unit = {},
) {
    val (keyItem) = item
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
                    onLongClick = { if (!isEditable) onSelected(item) },
                    onClick = { if (isEditable) onSelected(item) else onClick(keyItem) }
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
                        onClick = { onSelected(item) }
                    )
                } else if (keyItem is UserItem) {
                    IconButton(onClick = { onCopy(keyItem) }) {
                        Icon(Icons.Rounded.ContentCopy, contentDescription = null)
                    }
                }
            }
        }
    }
}

@Composable
fun SelectUserItemLayout(
    item: UserItem,
    onClick: (UserItem) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable { onClick(item) }
            .padding(vertical = 10.dp, horizontal = 15.dp)
    ) {
        FillIcon(
            icon = Icons.Rounded.CreditCard,
            shape = RoundedCornerShape(20),
            modifier = Modifier.size(40.dp),
            colors = IconColors.UserItem
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

@Composable
fun SelectKeyItemLayout(
    item: SelectedState<KeyItem>,
    onClick: (SelectedState<KeyItem>) -> Unit = {},
    onSelected: (SelectedState<KeyItem>) -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable { onClick(item) }
            .padding(vertical = 10.dp, horizontal = 15.dp)
    ) {
        FillIcon(
            icon = when (item.value) {
                is UserItem -> Icons.Rounded.CreditCard
                is AuthItem -> Icons.Rounded.Person
            },
            shape = RoundedCornerShape(20),
            modifier = Modifier.size(40.dp),
            colors = when (item.value) {
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
                text = item.value.name,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = when (item.value) {
                    is UserItem -> item.value.username
                    is AuthItem -> item.value.authName
                },
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray,
            )
        }
        RadioButton(
            selected = item.selected,
            onClick = { onSelected(item) }
        )
    }
}