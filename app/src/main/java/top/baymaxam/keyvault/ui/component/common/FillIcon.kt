package top.baymaxam.keyvault.ui.component.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * 圆形图标
 * @author John
 * @since 28 6月 2024
 */

@Composable
fun FillIcon(
    icon: Painter,
    iconColor: Color,
    iconBackgroundColor: Color,
    shape: Shape,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .clip(shape)
            .background(iconBackgroundColor)
            .padding(5.dp)
            .then(modifier)
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun FillIcon(
    icon: ImageVector,
    iconColor: Color,
    iconBackgroundColor: Color,
    shape: Shape,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .clip(shape)
            .background(iconBackgroundColor)
            .padding(5.dp)
            .then(modifier)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.fillMaxSize()
        )
    }
}