package top.baymaxam.keyvault.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.navOptions
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.InitScreenDestination
import com.ramcosta.composedestinations.generated.destinations.MainScreenDestination
import com.ramcosta.composedestinations.generated.destinations.SelectExpiryScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultRecipient
import com.ramcosta.composedestinations.result.onResult
import org.koin.compose.koinInject
import top.baymaxam.keyvault.R
import top.baymaxam.keyvault.model.domain.Expiry
import top.baymaxam.keyvault.ui.component.EntryButton
import top.baymaxam.keyvault.ui.component.PasswordField
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.ui.theme.robotoFont
import top.baymaxam.keyvault.util.SlideTransitions
import top.baymaxam.keyvault.util.errorToast
import top.baymaxam.keyvault.util.successToast
import top.baymaxam.keyvault.vm.PreferenceStateHolder

/**
 * 初始化页
 * @author John
 * @since 07 2月 2025
 */
@Destination<RootGraph>(style = SlideTransitions::class)
@Composable
fun InitScreen(
    navigator: DestinationsNavigator,
    expiryRecipient: ResultRecipient<SelectExpiryScreenDestination, Expiry>
) {
    val passwordState = remember { mutableStateOf("") }
    val repeatState = remember { mutableStateOf("") }
    val expiryState = remember { mutableStateOf(Expiry.ThirtyMinutes) }
    var isError by remember { mutableStateOf(false) }
    val preferenceStateHolder = koinInject<PreferenceStateHolder>()

    expiryRecipient.onResult { expiryState.value = it }

    ContentLayout(
        passwordState = passwordState,
        repeatState = repeatState,
        expiryState = expiryState,
        isError = isError,
        onExpiryClick = { navigator.navigate(SelectExpiryScreenDestination) },
        onConfirm = {
            if (passwordState.value.isEmpty()) {
                isError = true
                errorToast("密钥不能为空")
                return@ContentLayout
            }
            if (passwordState.value != repeatState.value) {
                isError = true
                errorToast("两次输入密钥不一致")
                return@ContentLayout
            }
            preferenceStateHolder.setAuthorization(passwordState.value)
            preferenceStateHolder.setExpiry(expiryState.value)
            val options = navOptions {
                launchSingleTop = true
                popUpTo(route = InitScreenDestination.route) { inclusive = true }
            }
            successToast("设置成功")
            navigator.navigate(MainScreenDestination, options)
        }
    )
}

@Composable
private fun ContentLayout(
    passwordState: MutableState<String> = mutableStateOf(""),
    repeatState: MutableState<String> = mutableStateOf(""),
    expiryState: MutableState<Expiry> = mutableStateOf(Expiry.Forever),
    isError: Boolean = false,
    onExpiryClick: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    val passwordVisualState = remember { mutableStateOf(false) }
    val repeatVisualState = remember { mutableStateOf(false) }
    Surface(color = MaterialTheme.colorScheme.background) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            Text(
                text = stringResource(R.string.app_name),
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = robotoFont,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 20.dp, top = 10.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(
                    "设置密钥",
                    fontFamily = robotoFont,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 5.sp
                )
                PasswordField(
                    contentState = passwordState,
                    visualState = passwordVisualState,
                    placeholder = { Text("输入密钥") },
                    modifier = Modifier.fillMaxWidth(0.8f),
                    isError = isError
                )
                PasswordField(
                    contentState = repeatState,
                    visualState = repeatVisualState,
                    placeholder = { Text("再次输入密钥") },
                    modifier = Modifier.fillMaxWidth(0.8f),
                    isError = isError,
                )

                EntryButton(
                    value = expiryState.value.label,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    leadingIcon = { Icon(Icons.Rounded.Schedule, contentDescription = null) },
                    onClick = onExpiryClick
                )

                Button(
                    onClick = onConfirm,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp)
                ) {
                    Text(
                        "确认",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 15.sp
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        ContentLayout()
    }
}
