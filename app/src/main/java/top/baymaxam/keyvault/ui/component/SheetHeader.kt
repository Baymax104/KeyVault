package top.baymaxam.keyvault.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 底部弹窗头部
 * @author John
 * @since 26 1月 2025
 */
@Composable
fun TitleHeader(
    title: String,
    leadingIcon: @Composable BoxScope.() -> Unit = {},
    trailingIcon: @Composable BoxScope.() -> Unit = {},
) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
        ) {
            Box(
                modifier = Modifier.align(Alignment.CenterStart),
                content = leadingIcon
            )
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.Center)
            )
            Box(
                modifier = Modifier.align(Alignment.CenterEnd),
                content = trailingIcon
            )
        }
    }
}

@Composable
fun LineHeader(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val start = Offset(0f, size.height / 2)
        val end = Offset(size.width, size.height / 2)
        drawLine(Color.Gray, start, end, size.height, StrokeCap.Round)
    }
}
