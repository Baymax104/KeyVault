package top.baymaxam.keyvault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.InitScreenDestination
import com.ramcosta.composedestinations.generated.destinations.MainScreenDestination
import com.ramcosta.composedestinations.generated.destinations.VerifyScreenDestination
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import top.baymaxam.keyvault.model.domain.DarkMode
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.util.BottomSheetNavigation
import top.baymaxam.keyvault.vm.PreferenceStateHolder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setupSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val preferenceStateHolder = koinInject<PreferenceStateHolder>()
            val darkModeState = preferenceStateHolder.darkModeFlow.collectAsState()
            val darkTheme = if (darkModeState.value == DarkMode.System) {
                isSystemInDarkTheme()
            } else {
                darkModeState.value == DarkMode.Dark
            }
            AppTheme(darkTheme) {
                BottomSheetNavigation {
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        navController = it,
                        start = when {
                            !preferenceStateHolder.hasAuthorization -> InitScreenDestination
                            preferenceStateHolder.isExpired -> VerifyScreenDestination
                            else -> MainScreenDestination
                        }
                    )
                }
            }
        }
    }
}

fun ComponentActivity.setupSplashScreen() {
    var keep = true
    installSplashScreen().setKeepOnScreenCondition { keep }
    lifecycleScope.launch {
        delay(600)
        keep = false
    }
}
