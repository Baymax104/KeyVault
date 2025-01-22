package top.baymaxam.keyvault.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.baymaxam.keyvault.ui.theme.AppTheme

/**
 * 选择按钮
 * @author John
 * @since 2023/8/8
 */

@Composable
fun SelectionButton(
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onCancel,
            colors = ButtonDefaults.textButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.5f)
                .padding(10.dp)
        ) {
            Text(
                text = "取消",
                fontSize = 16.sp,
                style = MaterialTheme.typography.labelMedium,
            )
        }
        Button(
            onClick = onConfirm,
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.5f)
                .padding(10.dp)
        ) {
            Text(
                text = "确认",
                fontSize = 16.sp,
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

@Composable
fun SelectionHeader(
    title: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .height(50.dp)
    ) {
        TextButton(
            onClick = onCancel,
            colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text(
                text = "取消",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelMedium
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        TextButton(
            onClick = onConfirm,
            colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "确定",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}


@Composable
fun SelectAuthButton(
    modifier: Modifier = Modifier,
    value: String = "",
    onClick: () -> Unit = {},
    onTrailingClick: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .clickable(onClick = onClick)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            enabled = false,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.onBackground,
                focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                disabledBorderColor = MaterialTheme.colorScheme.onBackground,
                disabledLeadingIconColor = MaterialTheme.colorScheme.onBackground,
                disabledTextColor = MaterialTheme.colorScheme.onBackground,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onBackground
            ),
            leadingIcon = { Icon(Icons.Rounded.CreditCard, contentDescription = null) },
            trailingIcon = {
                if (onTrailingClick != null) {
                    IconButton(onTrailingClick) {
                        Icon(Icons.Rounded.Edit, contentDescription = null)
                    }
                }
            }
        )
    }
}

@Composable
fun SelectableTag(
    text: String,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    color: Color = MaterialTheme.colorScheme.primary,
    fontSize: TextUnit = 14.sp,
    enabled: Boolean = true,
    selected: Boolean = false,
    onClick: () -> Unit = {},
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(shape)
            .border(1.dp, MaterialTheme.colorScheme.primary, shape)
            .background(if (selected) color else Color.Transparent)
            .clickable(enabled = enabled, onClick = onClick)
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 3.dp),
            color = if (selected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.primary
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        Column {
            SelectionButton(Modifier.height(60.dp))
            SelectAuthButton()
        }
    }
}