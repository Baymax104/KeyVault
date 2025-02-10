package top.baymaxam.keyvault.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.bottomsheet.spec.DestinationStyleBottomSheet
import com.ramcosta.composedestinations.result.ResultBackNavigator
import top.baymaxam.keyvault.model.domain.Expiry
import top.baymaxam.keyvault.ui.component.TitleHeader
import top.baymaxam.keyvault.ui.theme.AppTheme

/**
 * SelectExpiryScreen
 * @author John
 * @since 08 2月 2025
 */
@Destination<RootGraph>(style = DestinationStyleBottomSheet::class)
@Composable
fun SelectExpiryScreen(navigator: ResultBackNavigator<Expiry>) {
    ContentLayout(
        onBack = { navigator.navigateBack() },
        onItemClick = { navigator.navigateBack(it) }
    )
}


@Composable
private fun ContentLayout(
    onBack: () -> Unit = {},
    onItemClick: (Expiry) -> Unit = {},
) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            TitleHeader(
                title = "设置过期时间",
                leadingIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Rounded.Close, contentDescription = null)
                    }
                }
            )
            Expiry.entries.forEach {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemClick(it) }
                ) {
                    Text(
                        text = it.label,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(vertical = 15.dp)
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun Preview() {
    AppTheme {
        ContentLayout()
    }
}
