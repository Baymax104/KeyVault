package top.baymaxam.keyvault.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.bottomsheet.spec.DestinationStyleBottomSheet
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import top.baymaxam.keyvault.model.domain.AuthItem
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.Tag
import top.baymaxam.keyvault.model.domain.UserItem
import top.baymaxam.keyvault.state.SelectItemViewModel
import top.baymaxam.keyvault.state.SelectedState
import top.baymaxam.keyvault.ui.component.SearchField
import top.baymaxam.keyvault.ui.component.SelectKeyItemLayout
import top.baymaxam.keyvault.ui.component.TitleHeader
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.util.errorToast
import top.baymaxam.keyvault.util.successToast

/**
 * AddTagItemScreen
 * @author John
 * @since 06 2月 2025
 */
@Destination<RootGraph>(style = DestinationStyleBottomSheet::class)
@Composable
fun AddTagItemScreen(
    navigator: DestinationsNavigator,
    tag: Tag
) {
    val searchState = remember { mutableStateOf("") }
    val vm = koinViewModel<SelectItemViewModel> { parametersOf(tag) }
    val itemListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    if (searchState.value.isEmpty()) {
        LaunchedEffect(Unit) {
            vm.candidateItems.refreshState()
            itemListState.scrollToItem(0)
        }
    }

    ContentLayout(
        items = vm.candidateItems.state,
        searchState = searchState,
        itemListState = itemListState,
        onBack = { navigator.navigateUp() },
        onSearch = { vm.search(searchState.value) },
        onDone = {
            scope.launch {
                vm.addItem()
                    .onFailure { errorToast(it.message) }
                    .onSuccess {
                        successToast("添加成功")
                        navigator.navigateUp()
                    }
            }
        }
    )
}

@Composable
private fun ContentLayout(
    items: List<SelectedState<KeyItem>> = emptyList(),
    searchState: MutableState<String> = mutableStateOf(""),
    itemListState: LazyListState = rememberLazyListState(),
    onBack: () -> Unit = {},
    onDone: () -> Unit = {},
    onSearch: () -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
            .background(MaterialTheme.colorScheme.background)
    ) {
        TitleHeader(
            title = "添加条目",
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

        SearchField(
            contentState = searchState,
            placeholder = { Text("搜索条目") },
            onSearch = onSearch,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 5.dp)
                .fillMaxWidth()
        )

        LazyColumn(
            state = itemListState,
            modifier = Modifier.weight(1f)
        ) {
            items(
                items = items,
                key = { it.value.id }
            ) { item ->
                SelectKeyItemLayout(
                    item = item,
                    onClick = { it.selected = !it.selected },
                    onSelected = { it.selected = !it.selected }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    AppTheme {
        ContentLayout(
            items = listOf(
                SelectedState(UserItem(name = "Hello")),
                SelectedState(AuthItem(name = "Hello1"))
            )
        )
    }
}
