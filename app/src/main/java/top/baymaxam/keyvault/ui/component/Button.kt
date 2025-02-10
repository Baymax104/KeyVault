package top.baymaxam.keyvault.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
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
fun EntryButton(
    value: String,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    onClick: () -> Unit = {},
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
            leadingIcon = leadingIcon,
            trailingIcon = {
                Surface(color = MaterialTheme.colorScheme.primary) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.defaultMinSize(
                            minHeight = InputFieldDefaults.MinHeight + 2.dp,
                            minWidth = InputFieldDefaults.MinHeight
                        )
                    ) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null)
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.onBackground,
                focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                disabledBorderColor = MaterialTheme.colorScheme.onBackground,
                disabledLeadingIconColor = MaterialTheme.colorScheme.onBackground,
                disabledTextColor = MaterialTheme.colorScheme.onBackground,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onBackground
            )
        )
    }
}

@Composable
fun FloatingButton(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        shape = RoundedCornerShape(35),
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 5.dp,
            pressedElevation = 5.dp,
            focusedElevation = 5.dp,
            hoveredElevation = 5.dp
        ),
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null
        )
    }
}

@Composable
fun ContainerButton(
    label: @Composable () -> Unit,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    onClick: () -> Unit = {},
) {
    Surface(
        shape = RectangleShape,
        contentColor = MaterialTheme.colorScheme.onBackground,
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            if (leading != null) {
                Box {
                    leading()
                }
            }
            Spacer(Modifier.width(10.dp))
            Box(modifier = Modifier.weight(1f)) {
                label()
            }
            if (trailing != null) {
                Box {
                    trailing()
                }
            }
            Spacer(Modifier.width(10.dp))
            Icon(
                Icons.AutoMirrored.Rounded.ArrowForwardIos,
                contentDescription = null,
            )
        }
    }
}

@Composable
fun SwitchButton(
    isSelected: Boolean,
    label: @Composable () -> Unit,
    onSelected: (Boolean) -> Unit = {},
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Box(
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                label()
            }
            Switch(
                checked = isSelected,
                onCheckedChange = onSelected,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        Column {
            SelectionButton(Modifier.height(60.dp))
            EntryButton("")
        }
    }
}