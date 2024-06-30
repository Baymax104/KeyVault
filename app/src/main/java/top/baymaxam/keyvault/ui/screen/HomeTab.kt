package top.baymaxam.keyvault.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import top.baymaxam.keyvault.R
import top.baymaxam.keyvault.model.domain.CardItem
import top.baymaxam.keyvault.model.domain.PassItem
import top.baymaxam.keyvault.model.domain.WebItem
import top.baymaxam.keyvault.ui.component.ResentUsedList
import top.baymaxam.keyvault.ui.component.SearchField
import top.baymaxam.keyvault.ui.component.common.FillIcon
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.ui.theme.robotoFont
import top.baymaxam.keyvault.ui.theme.surfaceVariantLight

/**
 * 首页
 * @author John
 * @since 22 6月 2024
 */
object HomeTab : Tab {
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
            mutableStateListOf(
                WebItem(id = 0, name = "测试", username = "username"),
                CardItem(id = 1, name = "TestCard", username = "code")
            )
        }

        ContentLayout(items = list)
    }
}


@Composable
private fun ContentLayout(
    searchContent: MutableState<String> = mutableStateOf(""),
    onSearch: () -> Unit = {},
    onPasswordClick: () -> Unit = {},
    onTagClick: () -> Unit = {},
    items: SnapshotStateList<PassItem> = mutableStateListOf(),
    onItemCopy: (PassItem) -> Unit = {},
    onItemClick: (PassItem) -> Unit = {},
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
            items = items,
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
            content = searchContent,
            onSearch = onSearch
        )

        CatalogList(
            passwordCount = passwordCount,
            tagCount = tagCount,
            onPasswordClick = onPasswordClick,
            onTagClick = onTagClick
        )
    }
}


@Composable
private fun CatalogList(
    passwordCount: MutableIntState = mutableIntStateOf(0),
    tagCount: MutableIntState = mutableIntStateOf(0),
    onPasswordClick: () -> Unit = {},
    onTagClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .padding(vertical = 15.dp)
            .fillMaxWidth(),
    ) {

        CatalogCard(
            modifier = Modifier
                .height(110.dp)
                .weight(1f),
            icon = R.drawable.ic_key,
            iconColor = Color(0xff11834f),
            iconBackgroundColor = Color(0xffd5ffd3),
            text = "${passwordCount.intValue}条密码",
            onClick = onPasswordClick
        )

        Spacer(modifier = Modifier.width(10.dp))

        CatalogCard(
            modifier = Modifier
                .height(110.dp)
                .weight(1f),
            icon = R.drawable.ic_tag,
            iconColor = Color(0xffe78529),
            iconBackgroundColor = Color(0xffffe8d3),
            text = "${tagCount.intValue}个标签",
            onClick = onTagClick
        )
    }
}

@Composable
private fun CatalogCard(
    modifier: Modifier = Modifier,
    icon: Int,
    iconColor: Color,
    iconBackgroundColor: Color,
    text: String,
    onClick: () -> Unit
) {

    ElevatedCard(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White
        ),
        onClick = onClick,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
        ) {

            FillIcon(
                icon = painterResource(id = icon),
                iconColor = iconColor,
                iconBackgroundColor = iconBackgroundColor,
                shape = RoundedCornerShape(50),
                modifier = Modifier.size(30.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = text,
                    style = TextStyle(
                        fontFamily = robotoFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = null
                )
            }
        }
    }
}


@Composable
private fun ResentUsed(
    items: SnapshotStateList<PassItem>,
    onItemCopy: (PassItem) -> Unit,
    onItemClick: (PassItem) -> Unit
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
            items = items,
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
        mutableStateListOf(
            WebItem(id = 0, name = "TestWeb", username = "username"),
            CardItem(id = 1, name = "TestCard", username = "code")
        )
    }
    AppTheme {
        ContentLayout(
            items = list
        )
//        Header()
//        CategoryList()
    }
}
