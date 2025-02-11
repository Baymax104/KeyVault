package top.baymaxam.keyvault.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.bottomsheet.spec.DestinationStyleBottomSheet
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.AddItemTagAddTagScreenDestination
import com.ramcosta.composedestinations.generated.destinations.SelectTagScreenDestination
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.Tag
import top.baymaxam.keyvault.model.domain.UserItem
import top.baymaxam.keyvault.state.SelectedState
import top.baymaxam.keyvault.ui.component.TitleHeader
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.util.AddItemTagGraph
import top.baymaxam.keyvault.util.LocalNavigator
import top.baymaxam.keyvault.util.NavigatorProvider
import top.baymaxam.keyvault.util.SlideTransitions
import top.baymaxam.keyvault.util.errorToast
import top.baymaxam.keyvault.util.successToast
import top.baymaxam.keyvault.vm.AddItemTagViewModel

/**
 * 添加条目标签页
 * @author John
 * @since 29 1月 2025
 */
@Destination<RootGraph>(style = DestinationStyleBottomSheet::class)
@Composable
fun AddItemTagScreen(
    navigator: DestinationsNavigator,
    keyItem: KeyItem,
) {
    NavigatorProvider(navigator) {
        DestinationsNavHost(NavGraphs.addItemTag) {
            composable(SelectTagScreenDestination) {
                SelectTagScreen(
                    navigator = destinationsNavigator,
                    keyItem = keyItem
                )
            }
        }
    }
}

@Destination<AddItemTagGraph>(start = true, style = SlideTransitions::class)
@Composable
fun SelectTagScreen(
    navigator: DestinationsNavigator,
    keyItem: KeyItem = UserItem(),
) {
    val rootNavigator = LocalNavigator.current
    val vm = koinInject<AddItemTagViewModel> { parametersOf(keyItem) }
    val scope = rememberCoroutineScope()

    ContentLayout(
        tags = vm.tags,
        onBack = { rootNavigator.navigateUp() },
        onToggleTag = { it.selected = !it.selected },
        onAddTagClick = { navigator.navigate(AddItemTagAddTagScreenDestination) },
        onDone = {
            scope.launch {
                vm.updateTags()
                    .onFailure { errorToast(it.message) }
                    .onSuccess {
                        successToast("设置标签成功")
                        rootNavigator.navigateUp()
                    }
            }
        }
    )
}

@Composable
private fun ContentLayout(
    tags: List<SelectedState<Tag>> = emptyList(),
    onBack: () -> Unit = {},
    onToggleTag: (SelectedState<Tag>) -> Unit = {},
    onAddTagClick: () -> Unit = {},
    onDone: () -> Unit = {},
) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
        ) {
            TitleHeader(
                title = "设置标签",
                leadingIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Rounded.Close, contentDescription = null)
                    }
                },
                trailingIcon = {
                    IconButton(onClick = onDone) {
                        Icon(Icons.Rounded.Done, contentDescription = null)
                    }
                }
            )

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 15.dp, vertical = 10.dp)
            ) {
                Text("已选标签：")
                FlowSelectableTags(
                    items = tags.filter { it.selected },
                    modifier = Modifier.fillMaxWidth(),
                    maxItemsInEachRow = 5,
                    onCloseClick = onToggleTag,
                    onItemClick = onToggleTag
                )

                Spacer(Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("可选标签：")
                    Text(
                        "新建",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable(onClick = onAddTagClick)
                    )
                }
                FlowSelectableTags(
                    items = tags.filter { !it.selected },
                    modifier = Modifier.fillMaxWidth(),
                    maxItemsInEachRow = 5,
                    onItemClick = onToggleTag
                )
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
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


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    AppTheme {
        ContentLayout(
            tags = listOf(
                SelectedState(Tag(name = "hello1")).apply { selected = true },
                SelectedState(Tag(name = "hello2")).apply { selected = false },
                SelectedState(Tag(name = "hello3")).apply { selected = true },
                SelectedState(Tag(name = "hello4")).apply { selected = false },
                SelectedState(Tag(name = "hello5")).apply { selected = true },
            )
        )
    }
}
