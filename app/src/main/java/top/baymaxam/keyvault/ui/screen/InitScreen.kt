package top.baymaxam.keyvault.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.baymaxam.keyvault.R
import top.baymaxam.keyvault.ui.component.PasswordField
import top.baymaxam.keyvault.ui.theme.AppTheme
import top.baymaxam.keyvault.ui.theme.robotoFont

/**
 * 初始化页
 * @author John
 * @since 07 2月 2025
 */
@Composable
fun InitScreen() {
    val passwordState = remember { mutableStateOf("") }
    val repeatState = remember { mutableStateOf("") }

    ContentLayout(
        passwordState = passwordState,
        repeatState = repeatState,
        onConfirm = {}
    )
}

@Composable
private fun ContentLayout(
    passwordState: MutableState<String> = mutableStateOf(""),
    repeatState: MutableState<String> = mutableStateOf(""),
    onConfirm: () -> Unit = {},
) {
    val passwordVisualState = remember { mutableStateOf(false) }
    val repeatVisualState = remember { mutableStateOf(false) }
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
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            PasswordField(
                contentState = repeatState,
                visualState = repeatVisualState,
                placeholder = { Text("再次输入密钥") },
                modifier = Modifier.fillMaxWidth(0.8f)
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


@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        ContentLayout()
    }
}
