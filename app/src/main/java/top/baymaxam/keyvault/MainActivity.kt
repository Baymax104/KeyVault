package top.baymaxam.keyvault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import top.baymaxam.keyvault.model.domain.VerifyState
import top.baymaxam.keyvault.state.AuthState
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.util.BottomSheetNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setupSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val authState = koinInject<AuthState>()
                BottomSheetNavigation {
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        navController = it,
                        start = when (authState.value) {
                            VerifyState.Init -> InitScreenDestination
                            VerifyState.Verify -> VerifyScreenDestination
                            VerifyState.Default -> MainScreenDestination
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
