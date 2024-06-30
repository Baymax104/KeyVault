package top.baymaxam.keyvault.ui.component.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.ui.theme.MainColor

/**
 * 标签文字
 * @author John
 * @since 28 6月 2024
 */

@Composable
fun TagItem(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified,
    color: Color = MainColor
) {
    Box(
        modifier = Modifier
            .border(1.dp, color, RoundedCornerShape(50))
            .padding(vertical = 5.dp, horizontal = 10.dp)
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = fontSize,
                fontWeight = FontWeight.Normal,
                color = color
            ),
            modifier = modifier.align(Alignment.Center)
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        TagItem(text = "Hello")
    }
}
