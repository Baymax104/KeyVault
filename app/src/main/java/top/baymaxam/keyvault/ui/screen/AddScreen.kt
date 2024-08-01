package top.baymaxam.keyvault.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import top.baymaxam.keyvault.model.domain.Tag
import top.baymaxam.keyvault.state.AddScreenModel
import top.baymaxam.keyvault.state.TagItemState
import top.baymaxam.keyvault.ui.component.SearchField
import top.baymaxam.keyvault.ui.component.common.TagItem
import top.baymaxam.keyvault.ui.theme.AppTheme

/**
 * 添加页
 * @author John
 * @since 23 6月 2024
 */
class AddScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: AddScreenModel = viewModel()

        ContentLayout(
            tags = viewModel.tagList
        )
    }

}

@Composable
private fun ContentLayout(
    searchTag: MutableState<String> = mutableStateOf(""),
    onSearch: () -> Unit = {},
    tags: SnapshotStateList<TagItemState> = mutableStateListOf(),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    ) {
        LineHeader(
            modifier = Modifier
                .padding(vertical = 15.dp)
                .height(5.dp)
                .fillMaxWidth(0.4f)
        )

        SearchField(
            placeholder = "搜索标签",
            content = searchTag,
            onSearch = onSearch
        )
        LazyRow(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 5.dp)
        ) {
            items(
                items = tags,
                key = { it.value.id }
            ) {
                TagItem(
                    tag = it,
                    onClick = { tag -> tag.selected.value = !tag.selected.value },
                    modifier = Modifier
                        .height(50.dp)
                        .width(80.dp)
                )
            }
        }
    }
}

@Composable
private fun LineHeader(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val start = Offset(0f, size.height / 2)
        val end = Offset(size.width, size.height / 2)
        drawLine(Color.Gray, start, end, size.height, StrokeCap.Round)
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        val tags = remember {
            mutableStateListOf(
                TagItemState(Tag(id = 0, name = "Hello")),
                TagItemState(Tag(id = 1, name = "Hello1")),
                TagItemState(Tag(id = 2, name = "Hello2")),
                TagItemState(Tag(id = 3, name = "Hello3")),
                TagItemState(Tag(id = 4, name = "Hello4")),
            )
        }
        ContentLayout(
            tags = tags
        )
    }
}
