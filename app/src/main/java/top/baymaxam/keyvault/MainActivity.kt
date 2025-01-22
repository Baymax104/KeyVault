package top.baymaxam.keyvault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.util.BottomSheetNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setupSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                BottomSheetNavigation {
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        navController = it,
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
