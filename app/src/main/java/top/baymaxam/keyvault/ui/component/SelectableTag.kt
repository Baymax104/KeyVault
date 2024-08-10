package top.baymaxam.keyvault.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.baymaxam.keyvault.ui.theme.AppTheme


@Composable
fun SelectableTag(
    text: String,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    fontSize: TextUnit = 14.sp,
    enabled: Boolean = true,
    selected: Boolean = false,
    onClick: () -> Unit = {},
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(shape)
            .border(1.dp, MaterialTheme.colorScheme.primary, shape)
            .background(if (selected) MaterialTheme.colorScheme.primary else Color.Transparent)
            .clickable(enabled = enabled, onClick = onClick)
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 3.dp),
            color = if (selected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.primary
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        SelectableTag(
            text = "Hello",
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .width(80.dp)
                .height(40.dp)
        )
    }
}
