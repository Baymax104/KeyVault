package top.baymaxam.keyvault.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import top.baymaxam.keyvault.R
import top.baymaxam.keyvault.ui.component.TopBackBar
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.ui.theme.robotoFont
import top.baymaxam.keyvault.util.SlideTransitions

/**
 * InfoScreen
 * @author John
 * @since 19 2月 2025
 */
@Destination<RootGraph>(style = SlideTransitions::class)
@Composable
fun InfoScreen(navigator: DestinationsNavigator) {
    ContentLayout(
        onBack = { navigator.navigateUp() }
    )
}


@Composable
private fun ContentLayout(
    onBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopBackBar(onBack = onBack) {
                Text("关于")
            }
        }
    ) { paddingValues ->
        Surface(color = MaterialTheme.colorScheme.surface) {
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Image(
                        painter = painterResource(R.drawable.img_logo),
                        contentDescription = null,
                        modifier = Modifier.size(150.dp)
                    )
                    Text(
                        text = stringResource(R.string.app_name),
                        fontFamily = robotoFont,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(Modifier.height(5.dp))
                    Text("v1.0", color = MaterialTheme.colorScheme.onSurfaceVariant)
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
