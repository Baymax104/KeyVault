package top.baymaxam.keyvault.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.compose.koinInject
import top.baymaxam.keyvault.model.domain.DarkMode
import top.baymaxam.keyvault.ui.component.SwitchButton
import top.baymaxam.keyvault.ui.component.TopBackBar
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.vm.PreferenceStateHolder

/**
 * DarkModeScreen
 * @author John
 * @since 10 2月 2025
 */
@Destination<RootGraph>
@Composable
fun DarkModeScreen(navigator: DestinationsNavigator) {
    val preferenceStateHolder = koinInject<PreferenceStateHolder>()
    val darkModeState = preferenceStateHolder.darkModeFlow.collectAsState()

    ContentLayout(
        darkModeState = darkModeState,
        onBack = { navigator.navigateUp() },
        onSystemClick = {
            val mode = if (it) DarkMode.System else DarkMode.Light
            preferenceStateHolder.setDarkMode(mode)
        },
        onDarkClick = {
            val mode = if (it) DarkMode.Dark else DarkMode.Light
            preferenceStateHolder.setDarkMode(mode)
        }
    )
}

@Composable
private fun ContentLayout(
    darkModeState: State<DarkMode> = mutableStateOf(DarkMode.Light),
    onBack: () -> Unit = {},
    onSystemClick: (Boolean) -> Unit = {},
    onDarkClick: (Boolean) -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopBackBar(onBack = onBack) {
                Text("深色模式")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            SwitchButton(
                isSelected = darkModeState.value == DarkMode.System,
                label = { Text("跟随系统") },
                onSelected = onSystemClick
            )
            SwitchButton(
                isSelected = darkModeState.value == DarkMode.Dark,
                label = { Text("深色模式") },
                onSelected = onDarkClick
            )
        }
    }
}


@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    AppTheme {
        ContentLayout()
    }
}
