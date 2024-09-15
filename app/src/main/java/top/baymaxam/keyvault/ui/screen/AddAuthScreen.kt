package top.baymaxam.keyvault.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.state.AddScreenModel
import top.baymaxam.keyvault.ui.component.AddAuthList
import top.baymaxam.keyvault.ui.component.SearchField
import top.baymaxam.keyvault.ui.theme.AppTheme

/**
 * 添加页选择授权页
 * @author John
 * @since 03 8月 2024
 */
class AddAuthScreen : Screen {

    override val key: ScreenKey
        get() = "Add-Auth-Screen"

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = navigator.koinNavigatorScreenModel<AddScreenModel>()
        val searchContentState = remember { mutableStateOf("") }
        val passItemListState = rememberLazyListState()

        if (searchContentState.value.isEmpty()) {
            LaunchedEffect(Unit) {
                viewModel.passItems.refreshState()
                passItemListState.scrollToItem(0)
            }
        }

        ContentLayout(
            searchContentState = searchContentState,
            items = viewModel.passItems.state,
            onSearch = { viewModel.searchPassItem(searchContentState.value) },
            onBack = { navigator.pop() },
            onPassItemClick = {
                viewModel.selectedPassItem.value = it
                navigator.pop()
            }
        )
    }
}

@Composable
private fun ContentLayout(
    searchContentState: MutableState<String> = mutableStateOf(""),
    items: List<KeyItem> = mutableStateListOf(),
    itemListState: LazyListState = rememberLazyListState(),
    onBack: () -> Unit = {},
    onSearch: () -> Unit = {},
    onPassItemClick: (KeyItem) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Header(onBack = onBack)

        SearchField(
            contentState = searchContentState,
            placeholder = { Text("搜索条目") },
            onSearch = onSearch,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 5.dp)
                .fillMaxWidth()
        )

        AddAuthList(
            state = itemListState,
            items = items,
            modifier = Modifier.weight(1f),
            onItemClick = onPassItemClick
        )
    }
}

@Composable
private fun Header(
    onBack: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp)
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.CenterStart),
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Icon(imageVector = Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null)
        }
        Text(
            text = "选择授权",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    AppTheme {
        val list = remember {
            mutableStateListOf(
                KeyItem(name = "测试", username = "username"),
                KeyItem(name = "TestCard", username = "code")
            )
        }
        ContentLayout(
            items = list
        )
    }
}
