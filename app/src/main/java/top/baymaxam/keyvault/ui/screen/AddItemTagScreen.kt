package top.baymaxam.keyvault.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import top.baymaxam.keyvault.state.AddItemTagViewModel
import top.baymaxam.keyvault.state.SelectedState
import top.baymaxam.keyvault.ui.component.FlowSelectableTags
import top.baymaxam.keyvault.ui.component.TitleHeader
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.util.AddItemTagGraph
import top.baymaxam.keyvault.util.LocalNavigator
import top.baymaxam.keyvault.util.NavigatorProvider
import top.baymaxam.keyvault.util.currentOrThrow
import top.baymaxam.keyvault.util.errorToast
import top.baymaxam.keyvault.util.successToast

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

@Destination<AddItemTagGraph>(start = true)
@Composable
fun SelectTagScreen(
    navigator: DestinationsNavigator,
    keyItem: KeyItem = UserItem(),
) {
    val rootNavigator = LocalNavigator.currentOrThrow
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
            .background(MaterialTheme.colorScheme.background)
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
