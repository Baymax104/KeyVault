package top.baymaxam.keyvault.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.bottomsheet.spec.DestinationStyleBottomSheet
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.compose.koinInject
import top.baymaxam.keyvault.ui.component.PasswordField
import top.baymaxam.keyvault.ui.component.TitleHeader
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.util.errorToast
import top.baymaxam.keyvault.util.successToast
import top.baymaxam.keyvault.vm.PreferenceStateHolder

/**
 * ModifyAuthScreen
 * @author John
 * @since 11 2月 2025
 */
@Destination<RootGraph>(style = DestinationStyleBottomSheet::class)
@Composable
fun ModifyAuthScreen(navigator: DestinationsNavigator) {
    val preferenceStateHolder = koinInject<PreferenceStateHolder>()
    val oldPasswordState = remember { mutableStateOf("") }
    val newPasswordState = remember { mutableStateOf("") }
    val repeatState = remember { mutableStateOf("") }
    var isOldError by remember { mutableStateOf(false) }
    var isNewError by remember { mutableStateOf(false) }
    var isRepeatError by remember { mutableStateOf(false) }

    ContentLayout(
        oldPasswordState = oldPasswordState,
        newPasswordState = newPasswordState,
        repeatState = repeatState,
        isOldError = isOldError,
        isNewError = isNewError,
        isRepeatError = isRepeatError,
        onBack = { navigator.navigateUp() },
        onDone = {
            if (oldPasswordState.value.isEmpty()) {
                isOldError = true
                errorToast("输入不能为空")
                return@ContentLayout
            }
            if (newPasswordState.value.isEmpty()) {
                isNewError = true
                errorToast("输入不能为空")
                return@ContentLayout
            }
            if (repeatState.value.isEmpty()) {
                isRepeatError = true
                errorToast("输入不能为空")
                return@ContentLayout
            }
            if (newPasswordState.value != repeatState.value) {
                isRepeatError = true
                errorToast("两次输入密钥不一致")
                return@ContentLayout
            }
            if (!preferenceStateHolder.matchKey(oldPasswordState.value)) {
                isOldError = true
                errorToast("旧密钥错误")
                return@ContentLayout
            }
            preferenceStateHolder.setAuthorization(newPasswordState.value)
            successToast("修改成功")
            navigator.navigateUp()
        }
    )
}

@Composable
private fun ContentLayout(
    oldPasswordState: MutableState<String> = mutableStateOf(""),
    newPasswordState: MutableState<String> = mutableStateOf(""),
    repeatState: MutableState<String> = mutableStateOf(""),
    isOldError: Boolean = false,
    isNewError: Boolean = false,
    isRepeatError: Boolean = false,
    onBack: () -> Unit = {},
    onDone: () -> Unit = {},
) {
    val oldVisualState = remember { mutableStateOf(false) }
    val newVisualState = remember { mutableStateOf(false) }
    val repeatVisualState = remember { mutableStateOf(false) }
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
        ) {
            TitleHeader(
                title = "修改密钥",
                leadingIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Rounded.Close, contentDescription = null)
                    }
                },
                trailingIcon = {
                    IconButton(onClick = onDone) {
                        Icon(Icons.Rounded.Done, contentDescription = null)
                    }
                }
            )

            Spacer(Modifier.height(20.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                PasswordField(
                    contentState = oldPasswordState,
                    visualState = oldVisualState,
                    placeholder = { Text("输入旧密钥") },
                    isError = isOldError,
                    modifier = Modifier.fillMaxWidth()
                )

                PasswordField(
                    contentState = newPasswordState,
                    visualState = newVisualState,
                    placeholder = { Text("输入新密钥") },
                    isError = isNewError,
                    modifier = Modifier.fillMaxWidth()
                )

                PasswordField(
                    contentState = repeatState,
                    visualState = repeatVisualState,
                    placeholder = { Text("再次输入新密钥") },
                    isError = isRepeatError,
                    modifier = Modifier.fillMaxWidth()
                )
            }
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
