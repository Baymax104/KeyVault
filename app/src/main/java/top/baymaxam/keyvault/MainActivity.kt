package top.baymaxam.keyvault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import top.baymaxam.keyvault.ui.screen.MainScreen
import top.baymaxam.keyvault.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                BottomSheetNavigator(
                    sheetShape = RoundedCornerShape(15.dp),
                ) {
                    Navigator(screen = MainScreen())
                }
            }
        }
    }
}
