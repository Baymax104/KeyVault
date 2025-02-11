package top.baymaxam.keyvault.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.generated.destinations.AuthScreenDestination
import com.ramcosta.composedestinations.generated.destinations.DarkModeScreenDestination
import org.koin.compose.koinInject
import top.baymaxam.keyvault.R
import top.baymaxam.keyvault.model.domain.DarkMode
import top.baymaxam.keyvault.ui.component.ContainerButton
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.ui.theme.robotoFont
import top.baymaxam.keyvault.util.LocalNavigator
import top.baymaxam.keyvault.vm.PreferenceStateHolder

/**
 * 设置页
 * @author John
 * @since 23 6月 2024
 */
@Composable
fun SettingScreen() {
    val navigator = LocalNavigator.current
    val preferenceStateHolder = koinInject<PreferenceStateHolder>()
    val darkModeState = preferenceStateHolder.darkModeFlow.collectAsState()
    ContentLayout(
        darkModeState = darkModeState,
        onKeyClick = { navigator.navigate(AuthScreenDestination) },
        onDarkClick = { navigator.navigate(DarkModeScreenDestination) }
    )
}

@Composable
private fun ContentLayout(
    darkModeState: State<DarkMode> = mutableStateOf(DarkMode.System),
    onKeyClick: () -> Unit = {},
    onDarkClick: () -> Unit = {},
    onInfoClick: () -> Unit = {}
) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Surface(color = MaterialTheme.colorScheme.surfaceVariant) {
                Box(
                    modifier = Modifier
                        .statusBarsPadding()
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = stringResource(R.string.app_name),
                        fontFamily = robotoFont,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Start,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            ContainerButton(
                label = { Text("密钥管理") },
                leading = { Icon(Icons.Rounded.Lock, contentDescription = null) },
                onClick = onKeyClick
            )
            ContainerButton(
                label = { Text("深色模式") },
                leading = { Icon(Icons.Rounded.DarkMode, contentDescription = null) },
                trailing = { Text(darkModeState.value.label) },
                onClick = onDarkClick
            )
            ContainerButton(
                label = { Text("关于") },
                leading = { Icon(Icons.Rounded.Info, contentDescription = null) },
                onClick = onInfoClick
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    AppTheme {
        ContentLayout()
    }
}
