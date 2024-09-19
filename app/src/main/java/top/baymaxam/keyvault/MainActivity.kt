package top.baymaxam.keyvault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.baymaxam.keyvault.ui.screen.MainScreen
import top.baymaxam.keyvault.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setupSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Navigator(MainScreen()) {
                    SlideTransition(it)
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
