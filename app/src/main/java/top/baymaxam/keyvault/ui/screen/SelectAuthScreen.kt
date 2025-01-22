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
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.bottomsheet.spec.DestinationStyleBottomSheet
import com.ramcosta.composedestinations.result.ResultBackNavigator
import org.koin.androidx.compose.koinViewModel
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.UserItem
import top.baymaxam.keyvault.state.AddAuthViewModel
import top.baymaxam.keyvault.ui.component.AddAuthList
import top.baymaxam.keyvault.ui.component.SearchField
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.util.AddGraph

/**
 * 添加页选择授权页
 * @author John
 * @since 03 8月 2024
 */
@Destination<AddGraph>()
@Destination<RootGraph>(
    style = DestinationStyleBottomSheet::class
)
@Composable
fun SelectAuthScreen(
    navigator: ResultBackNavigator<UserItem>
) {
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
        onUserItemClick = {
            navigator.navigateBack(it)
        }
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
            onItemClick = onUserItemClick
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
                UserItem(name = "测试", username = "username"),
                UserItem(name = "TestCard", username = "code")
            )
        }
        ContentLayout(
            items = list
        )
    }
}
