package top.baymaxam.keyvault.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import top.baymaxam.keyvault.R
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.state.HomeScreenModel
import top.baymaxam.keyvault.ui.component.CatalogBlock
import top.baymaxam.keyvault.ui.component.ResentUsedList
import top.baymaxam.keyvault.ui.component.SearchField
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.ui.theme.robotoFont
import top.baymaxam.keyvault.util.replaceAllBy

/**
 * 首页
 * @author John
 * @since 22 6月 2024
 */
object HomeTab : Tab {

    override val key: ScreenKey
        get() = "Home-Tab"

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(image = Icons.Filled.Home)
            return remember { TabOptions(index = 0u, title = "首页", icon = icon) }
        }

    private fun readResolve(): Any = HomeTab

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.root
        val viewModel = koinScreenModel<HomeScreenModel>()
        val searchContentState = remember { mutableStateOf("") }
        val resentUsedItems = remember { mutableStateListOf<KeyItem>() }
        val resentUsedListState = rememberLazyListState()
        val passwordCountState = remember { mutableIntStateOf(0) }
        val tagCountState = remember { mutableIntStateOf(0) }
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        if (!bottomSheetNavigator.isVisible) {
            LaunchedEffect(Unit) {
                resentUsedItems.replaceAllBy(viewModel.getResentItems())
                passwordCountState.intValue = viewModel.getItemCount()
                resentUsedListState.scrollToItem(0)
            }
        }

        ContentLayout(
            searchContentState = searchContentState,
            resentUsedItems = resentUsedItems,
            resentUsedListState = resentUsedListState,
            passwordCountState = passwordCountState,
            tagCountState = tagCountState,
            onSearch = {},
            onPasswordClick = { navigator += ItemListScreen() },
            onTagClick = { navigator += TagListScreen() },
            onItemClick = { navigator += ItemInfoScreen(it) }
        )
    }
}


@Composable
private fun ContentLayout(
    searchContentState: MutableState<String> = mutableStateOf(""),
    resentUsedItems: List<KeyItem> = mutableStateListOf(),
    resentUsedListState: LazyListState = rememberLazyListState(),
    passwordCountState: MutableIntState = mutableIntStateOf(0),
    tagCountState: MutableIntState = mutableIntStateOf(0),
    onSearch: () -> Unit = {},
    onPasswordClick: () -> Unit = {},
    onTagClick: () -> Unit = {},
    onItemClick: (KeyItem) -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(
            searchContentState = searchContentState,
            onSearch = onSearch,
            onPasswordClick = onPasswordClick,
            onTagClick = onTagClick,
            passwordCount = passwordCountState.intValue,
            tagCount = tagCountState.intValue
        )

        ResentUsed(
            resentUsedListState = resentUsedListState,
            keyItems = resentUsedItems,
            onItemClick = onItemClick
        )
    }
}

@Composable
private fun Header(
    searchContentState: MutableState<String> = mutableStateOf(""),
    onSearch: () -> Unit = {},
    passwordCount: Int = 0,
    tagCount: Int = 0,
    onPasswordClick: () -> Unit = {},
    onTagClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 15.dp)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(id = R.string.app_name),
            style = TextStyle(
                fontFamily = robotoFont,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth()
        )

        SearchField(
            contentState = searchContentState,
            placeholder = { Text("搜索条目") },
            onSearch = onSearch,
            modifier = Modifier.fillMaxWidth()
        )

        CatalogBlock(
            passwordCount = passwordCount,
            tagCount = tagCount,
            onPasswordClick = onPasswordClick,
            onTagClick = onTagClick
        )
    }
}


@Composable
private fun ResentUsed(
    resentUsedListState: LazyListState = rememberLazyListState(),
    keyItems: List<KeyItem>,
    onItemClick: (KeyItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(15.dp)
    ) {
        Text(
            text = "最近使用",
            modifier = Modifier
                .padding(bottom = 5.dp)
                .fillMaxWidth(),
            style = TextStyle(
                fontFamily = robotoFont,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                letterSpacing = 2.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        )
        ResentUsedList(
            state = resentUsedListState,
            keyItems = keyItems,
            onItemClick = onItemClick,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    val list = remember {
        mutableStateListOf(
            KeyItem(name = "TestWeb", username = "username"),
            KeyItem(name = "TestCard", username = "code")
        )
    }
    AppTheme {
        ContentLayout(
            resentUsedItems = list
        )
    }
}
