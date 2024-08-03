package top.baymaxam.keyvault.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import top.baymaxam.keyvault.R
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.PassKeyItem
import top.baymaxam.keyvault.ui.component.CatalogBlock
import top.baymaxam.keyvault.ui.component.ResentUsedList
import top.baymaxam.keyvault.ui.component.SearchField
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.ui.theme.robotoFont
import top.baymaxam.keyvault.ui.theme.surfaceVariantLight

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
            return remember {
                TabOptions(
                    index = 0u,
                    title = "首页",
                    icon = icon
                )
            }
        }

    private fun readResolve(): Any = HomeTab

    @Composable
    override fun Content() {
        val list = remember {
            mutableStateListOf<KeyItem>(
                PassKeyItem(id = 0, name = "测试", username = "username"),
                PassKeyItem(id = 1, name = "TestCard", username = "code")
            )
        }

        ContentLayout(keyItems = list)
    }
}


@Composable
private fun ContentLayout(
    searchContent: MutableState<String> = mutableStateOf(""),
    onSearch: () -> Unit = {},
    onPasswordClick: () -> Unit = {},
    onTagClick: () -> Unit = {},
    keyItems: SnapshotStateList<KeyItem> = mutableStateListOf(),
    onItemCopy: (PassKeyItem) -> Unit = {},
    onItemClick: (KeyItem) -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(
            searchContent = searchContent,
            onSearch = onSearch,
            onPasswordClick = onPasswordClick,
            onTagClick = onTagClick
        )

        ResentUsed(
            keyItems = keyItems,
            onItemCopy = onItemCopy,
            onItemClick = onItemClick
        )
    }
}

@Composable
private fun Header(
    searchContent: MutableState<String> = mutableStateOf(""),
    onSearch: () -> Unit = {},
    passwordCount: MutableIntState = mutableIntStateOf(0),
    tagCount: MutableIntState = mutableIntStateOf(0),
    onPasswordClick: () -> Unit = {},
    onTagClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(surfaceVariantLight)
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
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth()
        )

        SearchField(
            contentState = searchContent,
            placeholder = { Text(text = "搜索条目") },
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
    keyItems: SnapshotStateList<KeyItem>,
    onItemCopy: (PassKeyItem) -> Unit,
    onItemClick: (KeyItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
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
                letterSpacing = 2.sp
            )
        )
        ResentUsedList(
            keyItems = keyItems,
            onItemCopy = onItemCopy,
            onItemClick = onItemClick,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    val list = remember {
        mutableStateListOf<KeyItem>(
            PassKeyItem(id = 0, name = "TestWeb", username = "username"),
            PassKeyItem(id = 1, name = "TestCard", username = "code")
        )
    }
    AppTheme {
        ContentLayout(
            keyItems = list
        )
    }
}
