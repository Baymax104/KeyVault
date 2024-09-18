package top.baymaxam.keyvault.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import top.baymaxam.keyvault.state.DialogState
import top.baymaxam.keyvault.ui.theme.AppTheme

/**
 * 确认对话框
 * @author John
 * @since 2024/2/22
 */
@Composable
fun ConfirmDialog(
    state: DialogState,
    title: String = "",
    text: String = "",
    confirmText: String = "确认",
    cancelText: String = "取消",
    autoClose: Boolean = true,
    onConfirm: () -> Unit = {},
    onCancel: () -> Unit = { state.dismiss() },
) {
    if (state.isShow) {
        DialogContent(
            title = title,
            text = text,
            confirmText = confirmText,
            cancelText = cancelText,
            onCancel = onCancel,
            onDismissRequest = { state.isShow = false },
            onConfirm = {
                if (autoClose) {
                    state.dismiss()
                }
                onConfirm()
            },
        )
    }
}

@Composable
private fun DialogContent(
    title: String = "",
    text: String = "",
    confirmText: String = "",
    cancelText: String = "",
    onConfirm: () -> Unit = {},
    onCancel: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            shape = RoundedCornerShape(15.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(bottom = 5.dp, top = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = title, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    TextButton(
                        onClick = onCancel,
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier.width(100.dp)
                    ) {
                        Text(text = cancelText, style = MaterialTheme.typography.bodyMedium)
                    }

                    TextButton(
                        onClick = onConfirm,
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier.width(100.dp)
                    ) {
                        Text(text = confirmText, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        DialogContent()
    }
}
