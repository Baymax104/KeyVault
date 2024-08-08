package top.baymaxam.keyvault.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.baymaxam.keyvault.R
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.ui.theme.robotoFont

/**
 * 主页索引块
 * @author John
 * @since 03 8月 2024
 */

@Composable
fun CatalogBlock(
    passwordCount: MutableIntState = mutableIntStateOf(0),
    tagCount: MutableIntState = mutableIntStateOf(0),
    onPasswordClick: () -> Unit = {},
    onTagClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .padding(vertical = 15.dp)
            .fillMaxWidth(),
    ) {

        CatalogCard(
            modifier = Modifier
                .height(110.dp)
                .weight(1f),
            icon = R.drawable.ic_key,
            iconColor = Color(0xff11834f),
            iconBackgroundColor = Color(0xffd5ffd3),
            text = "${passwordCount.intValue}条密码",
            onClick = onPasswordClick
        )

        Spacer(modifier = Modifier.width(10.dp))

        CatalogCard(
            modifier = Modifier
                .height(110.dp)
                .weight(1f),
            icon = R.drawable.ic_tag,
            iconColor = Color(0xffe78529),
            iconBackgroundColor = Color(0xffffe8d3),
            text = "${tagCount.intValue}个标签",
            onClick = onTagClick
        )
    }
}

@Composable
private fun CatalogCard(
    modifier: Modifier = Modifier,
    icon: Int,
    iconColor: Color,
    iconBackgroundColor: Color,
    text: String,
    onClick: () -> Unit
) {

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        onClick = onClick,
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
                iconColor = iconColor,
                iconBackgroundColor = iconBackgroundColor,
                shape = RoundedCornerShape(50),
                modifier = Modifier.size(30.dp)
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

@Preview
@Composable
private fun Preview() {
    AppTheme {
        CatalogBlock()
    }
}

