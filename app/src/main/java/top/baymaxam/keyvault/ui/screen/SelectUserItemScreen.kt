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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.bottomsheet.spec.DestinationStyleBottomSheet
import com.ramcosta.composedestinations.result.ResultBackNavigator
import org.koin.androidx.compose.koinViewModel
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.UserItem
import top.baymaxam.keyvault.state.AddAuthViewModel
import top.baymaxam.keyvault.ui.component.FillIcon
import top.baymaxam.keyvault.ui.component.SearchField
import top.baymaxam.keyvault.ui.component.TitleHeader
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.ui.theme.IconColors
import top.baymaxam.keyvault.util.AddItemGraph

/**
 * 添加页选择授权页
 * @author John
 * @since 03 8月 2024
 */
@Destination<AddItemGraph>
@Destination<RootGraph>(style = DestinationStyleBottomSheet::class)
@Composable
fun SelectUserItemScreen(navigator: ResultBackNavigator<UserItem>) {
    val vm = koinViewModel<AddAuthViewModel>()
    val searchContentState = remember { mutableStateOf("") }
    val userItemListState = rememberLazyListState()

    if (searchContentState.value.isEmpty()) {
        LaunchedEffect(Unit) {
            vm.candidateUserItems.refreshState()
            userItemListState.scrollToItem(0)
        }
    }

    ContentLayout(
        searchContentState = searchContentState,
        items = vm.candidateUserItems.state,
        onSearch = { vm.searchUserItem(searchContentState.value) },
        onBack = { navigator.navigateBack() },
        onUserItemClick = { navigator.navigateBack(it) }
    )
}

@Composable
private fun ContentLayout(
    searchContentState: MutableState<String> = mutableStateOf(""),
    items: List<KeyItem> = mutableStateListOf(),
    itemListState: LazyListState = rememberLazyListState(),
    onBack: () -> Unit = {},
    onSearch: () -> Unit = {},
    onUserItemClick: (UserItem) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
            .background(MaterialTheme.colorScheme.background)
    ) {
        TitleHeader(
            title = "选择授权",
            leadingIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null)
                }
            }
        )

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
            onItemClick = onUserItemClick
        )
    }
}

@Composable
fun AddAuthList(
    items: List<KeyItem>,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    onItemClick: (UserItem) -> Unit = {}
) {
    LazyColumn(
        state = state,
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(
            items = items,
            key = { it.id }
        ) {
            AddAuthItem(
                item = it as UserItem,
                onClick = onItemClick
            )
        }
    }
}

@Composable
private fun AddAuthItem(
    item: UserItem,
    onClick: (UserItem) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable { onClick(item) }
            .padding(vertical = 10.dp, horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        FillIcon(
            icon = Icons.Rounded.CreditCard,
            shape = RoundedCornerShape(20),
            modifier = Modifier.size(40.dp),
            colors = IconColors.UserItem
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp)
        ) {
            Text(
                text = item.name,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = item.username,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        val list = remember {
            mutableStateListOf(
                UserItem(name = "测试", username = "username"),
                UserItem(name = "TestCard", username = "code")
            )
        }
        ContentLayout(
            items = list
        )
    }
}
