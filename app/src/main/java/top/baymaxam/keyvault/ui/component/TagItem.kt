package top.baymaxam.keyvault.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.baymaxam.keyvault.model.domain.Tag
import top.baymaxam.keyvault.state.ItemSelectedState
import top.baymaxam.keyvault.ui.theme.AppTheme

/**
 * 标签文字
 * @author John
 * @since 28 6月 2024
 */

@Composable
fun TagTextItem(
    tag: Tag,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Box(
        modifier = Modifier
            .border(1.dp, color, RoundedCornerShape(50))
            .padding(vertical = 5.dp, horizontal = 10.dp)
    ) {
        Text(
            text = tag.name,
            style = TextStyle(
                fontSize = fontSize,
                fontWeight = FontWeight.Normal,
                color = color
            ),
            modifier = modifier.align(Alignment.Center)
        )
    }
}


@Composable
fun TagItem(
    tag: ItemSelectedState<Tag>,
    modifier: Modifier = Modifier,
    onClick: (ItemSelectedState<Tag>) -> Unit = {}
) {
    val borderModifier = if (tag.selected.value) {
        modifier.border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(15.dp))
    } else {
        modifier
    }
    ElevatedCard(
        shape = RoundedCornerShape(15.dp),
        onClick = { onClick(tag) },
        modifier = borderModifier
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = tag.value.name,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                ),
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        TagItem(
            tag = ItemSelectedState(Tag(name = "Hello")),
            modifier = Modifier
                .height(50.dp)
                .width(80.dp),
            onClick = {
                it.selected.value = !it.selected.value
            }
        )
    }
}
