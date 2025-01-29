@file:OptIn(ExperimentalLayoutApi::class)

package top.baymaxam.keyvault.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import top.baymaxam.keyvault.model.domain.Tag
import top.baymaxam.keyvault.state.SelectedState
import top.baymaxam.keyvault.ui.theme.AppTheme

/**
 * FlowTags
 * @author John
 * @since 29 1月 2025
 */
@Composable
fun FlowSelectableTags(
    items: List<SelectedState<Tag>>,
    modifier: Modifier = Modifier,
    maxItemsInEachRow: Int = Int.MAX_VALUE,
    maxLines: Int = Int.MAX_VALUE,
    onAddClick: (() -> Unit)? = null,
    onCloseClick: ((SelectedState<Tag>) -> Unit)? = null,
    onItemClick: (SelectedState<Tag>) -> Unit = {},
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        modifier = modifier,
        maxItemsInEachRow = maxItemsInEachRow,
        maxLines = maxLines
    ) {
        items.forEach {
            key(it.value.id) {
                FilterChip(
                    selected = it.selected,
                    onClick = { onItemClick(it) },
                    label = { Text(it.value.name) },
                    trailingIcon = if (onCloseClick != null) {
                        {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(AssistChipDefaults.IconSize)
                                    .clickable { onCloseClick(it) }
                            )
                        }
                    } else null
                )
            }
        }
        if (onAddClick != null) {
            AssistChip(
                onClick = onAddClick,
                label = { Icon(Icons.Rounded.Add, contentDescription = null) }
            )
        }
    }
}

@Composable
fun FlowTags(
    items: List<Tag>,
    modifier: Modifier = Modifier,
    maxItemsInEachRow: Int = Int.MAX_VALUE,
    maxLines: Int = Int.MAX_VALUE,
    onAddClick: (() -> Unit)? = null,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        modifier = modifier,
        maxItemsInEachRow = maxItemsInEachRow,
        maxLines = maxLines
    ) {
        items.forEach {
            key(it.id) {
                AssistChip(
                    onClick = {},
                    enabled = false,
                    label = { Text(it.name) },
                    border = AssistChipDefaults.assistChipBorder(true),
                    colors = AssistChipDefaults.assistChipColors(
                        disabledContainerColor = MaterialTheme.colorScheme.background,
                        disabledLabelColor = MaterialTheme.colorScheme.onBackground,
                        disabledTrailingIconContentColor = MaterialTheme.colorScheme.onBackground
                    ),
                )
            }
        }
        if (onAddClick != null) {
            AssistChip(
                onClick = onAddClick,
                label = { Icon(Icons.Rounded.Add, contentDescription = null) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        FlowSelectableTags(
            items = listOf(
                SelectedState(Tag(name = "Hello")).apply { selected = true },
                SelectedState(Tag(name = "Hello")).apply { selected = true },
                SelectedState(Tag(name = "Hello")),
                SelectedState(Tag(name = "Hello")),
                SelectedState(Tag(name = "Hello")),
                SelectedState(Tag(name = "Hello")),
            ),
            onCloseClick = {}
        )
    }
}
