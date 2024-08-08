package top.baymaxam.keyvault.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    icon: ImageVector,
    modifier: Modifier = Modifier,
    colors: FillIconColors = FillIconDefaults.colors(),
    shape: Shape = FillIconDefaults.shape
) {
    Box(
        modifier = modifier.defaultMinSize(
            minHeight = FillIconDefaults.minHeight,
            minWidth = FillIconDefaults.minWidth
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.backgroundColor, shape)
                .padding(5.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colors.iconColor,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}


@Composable
fun FillIcon(
    icon: Painter,
    modifier: Modifier = Modifier,
    colors: FillIconColors = FillIconDefaults.colors(),
    shape: Shape = FillIconDefaults.shape
) {
    Box(
        modifier = modifier.defaultMinSize(
            minHeight = FillIconDefaults.minHeight,
            minWidth = FillIconDefaults.minWidth
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.backgroundColor, shape)
                .padding(5.dp)
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = colors.iconColor,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

object FillIconDefaults {

    val minWidth = 35.dp

    val minHeight = 35.dp

    val shape get() = ShapeDefaults.ExtraSmall


    internal val defaultFillIconColors
        @Composable
        get() = FillIconColors(
            iconColor = MaterialTheme.colorScheme.primary,
            backgroundColor = MaterialTheme.colorScheme.surfaceVariant
        )

    @Composable
    fun colors() = defaultFillIconColors

    @Composable
    fun colors(
        iconColor: Color = Color.Unspecified,
        backgroundColor: Color = Color.Unspecified
    ) = FillIconColors(iconColor, backgroundColor)

}

class FillIconColors(
    val iconColor: Color,
    val backgroundColor: Color
)