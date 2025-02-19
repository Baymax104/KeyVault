package top.baymaxam.keyvault.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.navOptions
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.InitScreenDestination
import com.ramcosta.composedestinations.generated.destinations.MainScreenDestination
import com.ramcosta.composedestinations.generated.destinations.ModifyAuthScreenDestination
import com.ramcosta.composedestinations.generated.destinations.SelectExpiryScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultRecipient
import com.ramcosta.composedestinations.result.onResult
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import top.baymaxam.keyvault.model.domain.Expiry
import top.baymaxam.keyvault.repo.KeyRepository
import top.baymaxam.keyvault.repo.TagRepository
import top.baymaxam.keyvault.repo.transaction
import top.baymaxam.keyvault.state.DialogState
import top.baymaxam.keyvault.state.rememberDialogState
import top.baymaxam.keyvault.ui.component.ConfirmDialog
import top.baymaxam.keyvault.ui.component.ContainerButton
import top.baymaxam.keyvault.ui.component.TopBackBar
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.util.SlideTransitions
import top.baymaxam.keyvault.util.successToast
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
    val keyRepository = koinInject<KeyRepository>()
    val tagRepository = koinInject<TagRepository>()
    val expiryState = preferenceStateHolder.expiryFlow.collectAsState()
    val deleteAllState = remember { mutableStateOf(false) }
    val dialogState = rememberDialogState()
    val scope = rememberCoroutineScope()

    expiryRecipient.onResult { preferenceStateHolder.setExpiry(it) }

    ContentLayout(
        dialogState = dialogState,
        expiryState = expiryState,
        deleteAllState = deleteAllState,
        onBack = { navigator.navigateUp() },
        onModifyClick = { navigator.navigate(ModifyAuthScreenDestination) },
        onExpiryClick = { navigator.navigate(SelectExpiryScreenDestination) },
        onResetClick = { dialogState.show() },
        onDialogConfirm = {
            scope.launch {
                preferenceStateHolder.clearAuthorization()
                if (deleteAllState.value) {
                    transaction {
                        keyRepository.deleteAll()
                        tagRepository.deleteAll()
                    }
                }
                val options = navOptions {
                    launchSingleTop = true
                    popUpTo(route = MainScreenDestination.route) { inclusive = true }
                }
                successToast("重置成功")
                navigator.navigate(InitScreenDestination, options)
            }
        }
    )
}

@Composable
private fun ContentLayout(
    dialogState: DialogState = rememberDialogState(false),
    expiryState: State<Expiry> = mutableStateOf(Expiry.ThirtyMinutes),
    deleteAllState: MutableState<Boolean> = mutableStateOf(false),
    onBack: () -> Unit = {},
    onModifyClick: () -> Unit = {},
    onExpiryClick: () -> Unit = {},
    onResetClick: () -> Unit = {},
    onDialogConfirm: () -> Unit = {},
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
    ConfirmDialog(
        state = dialogState,
        title = { Text("重置密钥") },
        text = {
            Column {
                Text("确认重置密钥？")
                Spacer(Modifier.height(5.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = deleteAllState.value,
                        onClick = { deleteAllState.value = !deleteAllState.value }
                    )
                    Text("同时删除所有数据")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    dialogState.dismiss()
                    onDialogConfirm()
                }
            ) {
                Text("确认")
            }
        },
        cancelButton = {
            TextButton(onClick = { dialogState.dismiss() }) {
                Text("取消")
            }
        }
    )
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        ContentLayout()
    }
}
