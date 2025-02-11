package top.baymaxam.keyvault.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Icon
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
import com.ramcosta.composedestinations.generated.destinations.ModifyAuthScreenDestination
import com.ramcosta.composedestinations.generated.destinations.SelectExpiryScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultRecipient
import com.ramcosta.composedestinations.result.onResult
import org.koin.compose.koinInject
import top.baymaxam.keyvault.model.domain.Expiry
import top.baymaxam.keyvault.ui.component.ContainerButton
import top.baymaxam.keyvault.ui.component.TopBackBar
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.util.SlideTransitions
import top.baymaxam.keyvault.vm.PreferenceStateHolder

/**
 * AuthScreen
 * @author John
 * @since 11 2月 2025
 */
@Destination<RootGraph>(style = SlideTransitions::class)
@Composable
fun AuthScreen(
    navigator: DestinationsNavigator,
    expiryRecipient: ResultRecipient<SelectExpiryScreenDestination, Expiry>
) {
    val preferenceStateHolder = koinInject<PreferenceStateHolder>()
    val expiryState = preferenceStateHolder.expiryFlow.collectAsState()

    expiryRecipient.onResult { preferenceStateHolder.setExpiry(it) }

    ContentLayout(
        expiryState = expiryState,
        onBack = { navigator.navigateUp() },
        onModifyClick = { navigator.navigate(ModifyAuthScreenDestination) },
        onExpiryClick = { navigator.navigate(SelectExpiryScreenDestination) },
        onResetClick = {}
    )
}

@Composable
private fun ContentLayout(
    expiryState: State<Expiry> = mutableStateOf(Expiry.ThirtyDays),
    onBack: () -> Unit = {},
    onModifyClick: () -> Unit = {},
    onExpiryClick: () -> Unit = {},
    onResetClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopBackBar(onBack = onBack) {
                Text("密钥管理")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            ContainerButton(
                label = { Text("修改密钥") },
                leading = { Icon(Icons.Rounded.Lock, contentDescription = null) },
                onClick = onModifyClick
            )
            ContainerButton(
                label = { Text("修改过期时间") },
                leading = { Icon(Icons.Rounded.Schedule, contentDescription = null) },
                trailing = { Text(expiryState.value.label) },
                onClick = onExpiryClick
            )
            ContainerButton(
                label = { Text("重置密钥") },
                leading = { Icon(Icons.Rounded.Refresh, contentDescription = null) },
                onClick = onResetClick
            )
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
