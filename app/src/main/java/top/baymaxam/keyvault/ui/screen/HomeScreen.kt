package top.baymaxam.keyvault.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.generated.destinations.ItemInfoScreenDestination
import com.ramcosta.composedestinations.generated.destinations.ItemListScreenDestination
import com.ramcosta.composedestinations.generated.destinations.TagListScreenDestination
import kotlinx.coroutines.flow.map
import org.koin.compose.koinInject
import top.baymaxam.keyvault.R
import top.baymaxam.keyvault.model.domain.AuthItem
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.UserItem
import top.baymaxam.keyvault.model.entity.asItem
import top.baymaxam.keyvault.repo.KeyRepository
import top.baymaxam.keyvault.repo.TagRepository
import top.baymaxam.keyvault.ui.component.FillIcon
import top.baymaxam.keyvault.ui.component.FillIconColors
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.ui.theme.IconColors
import top.baymaxam.keyvault.ui.theme.robotoFont
import top.baymaxam.keyvault.util.LocalNavigator
import top.baymaxam.keyvault.util.currentOrThrow

/**
 * 首页
 * @author John
 * @since 22 6月 2024
 */
@Composable
fun HomeScreen() {
    val navigator = LocalNavigator.currentOrThrow
    val keyDao = koinInject<KeyRepository>()
    val tagDao = koinInject<TagRepository>()
    val tagCountState = tagDao.queryCount().collectAsState(0)
    val passwordCountState = keyDao.queryCount().collectAsState(0)
    val resentUsedItems = keyDao.queryOrderedByResentDate()
        .map { l -> l.map { it.asItem() } }
        .collectAsState(emptyList())

    ContentLayout(
        resentUsedItems = resentUsedItems.value,
        passwordCountState = passwordCountState,
        tagCountState = tagCountState,
        onSearch = {},
        onItemClick = { navigator.navigate(ItemListScreenDestination) },
        onTagClick = { navigator.navigate(TagListScreenDestination) },
        onResentItemClick = { navigator.navigate(ItemInfoScreenDestination(it)) }
    )
}


@Composable
private fun ContentLayout(
    resentUsedItems: List<KeyItem> = mutableStateListOf(),
    passwordCountState: State<Int> = mutableIntStateOf(0),
    tagCountState: State<Int> = mutableIntStateOf(0),
    onSearch: () -> Unit = {},
    onItemClick: () -> Unit = {},
    onTagClick: () -> Unit = {},
    onResentItemClick: (KeyItem) -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(
            onSearch = onSearch,
            onItemClick = onItemClick,
            onTagClick = onTagClick,
            itemCount = passwordCountState.value,
            tagCount = tagCountState.value
        )

        ResentItemList(
            keyItems = resentUsedItems,
            onItemClick = onResentItemClick
        )
    }
}

@Composable
private fun Header(
    onSearch: () -> Unit = {},
    itemCount: Int = 0,
    tagCount: Int = 0,
    onItemClick: () -> Unit = {},
    onTagClick: () -> Unit = {}
) {
    Surface(color = MaterialTheme.colorScheme.surfaceVariant) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .statusBarsPadding()
        ) {

            Box(
                modifier = Modifier.fillMaxWidth()
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
                    modifier = Modifier.align(Alignment.Center)
                )
                IconButton(
                    onClick = onSearch,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = null
                    )
                }
            }

            IndexView(
                itemCount = itemCount,
                tagCount = tagCount,
                onItemClick = onItemClick,
                onTagClick = onTagClick
            )
        }
    }
}


@Composable
private fun ResentItemList(
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
            text = "最近查看",
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
        ResentList(
            keyItems = keyItems,
            modifier = Modifier.fillMaxWidth(),
            state = resentUsedListState,
            onItemClick = onItemClick,
        )
    }
}

@Composable
fun IndexView(
    itemCount: Int = 0,
    tagCount: Int = 0,
    onItemClick: () -> Unit = {},
    onTagClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .padding(vertical = 15.dp)
            .fillMaxWidth(),
    ) {
        IndexCard(
            modifier = Modifier
                .height(110.dp)
                .weight(1f),
            icon = R.drawable.ic_key,
            iconColors = IconColors.IndexKey,
            text = "${itemCount}个条目",
            onClick = onItemClick
        )

        Spacer(modifier = Modifier.width(10.dp))

        IndexCard(
            modifier = Modifier
                .height(110.dp)
                .weight(1f),
            icon = R.drawable.ic_tag,
            iconColors = IconColors.IndexTag,
            text = "${tagCount}个标签",
            onClick = onTagClick
        )
    }
}

@Composable
private fun IndexCard(
    modifier: Modifier = Modifier,
    icon: Int,
    iconColors: FillIconColors,
    text: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        onClick = onClick,
        tonalElevation = 0.dp,
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
                shape = RoundedCornerShape(50),
                modifier = Modifier.size(40.dp),
                colors = iconColors
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
fun ResentList(
    keyItems: List<KeyItem>,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    onItemClick: (KeyItem) -> Unit = {},
) {
    LazyColumn(
        state = state,
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(vertical = 10.dp)
    ) {
        items(
            items = keyItems,
            key = { it.id }
        ) {
            RecentItem(
                item = it,
                onClick = onItemClick
            )
        }
    }
}


@Composable
private fun RecentItem(
    item: KeyItem = UserItem(),
    onClick: (KeyItem) -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 0.dp,
        shadowElevation = 1.dp,
        onClick = { onClick(item) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FillIcon(
                    icon = when (item) {
                        is UserItem -> Icons.Rounded.CreditCard
                        is AuthItem -> Icons.Rounded.Person
                    },
                    shape = RoundedCornerShape(20),
                    modifier = Modifier.size(45.dp),
                    colors = when (item) {
                        is UserItem -> IconColors.UserItem
                        is AuthItem -> IconColors.AuthItem
                    }
                )

                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .height(50.dp)
                        .padding(horizontal = 10.dp)
                ) {
                    Text(
                        text = item.name,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    )
                    Text(
                        text = when (item) {
                            is UserItem -> item.username
                            is AuthItem -> item.authName
                        },
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Gray
                        )
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    val list = remember {
        mutableStateListOf(
            UserItem(name = "TestWeb", username = "username"),
            UserItem(name = "TestCard", username = "code")
        )
    }
    AppTheme {
        ContentLayout(
            resentUsedItems = list
        )
    }
}
