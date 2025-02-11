package top.baymaxam.keyvault.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import top.baymaxam.keyvault.state.DialogState
import top.baymaxam.keyvault.state.rememberDialogState
import top.baymaxam.keyvault.ui.theme.AppTheme

/**
 * 确认对话框
 * @author John
 * @since 2024/2/22
 */
@Composable
fun ConfirmDialog(
    state: DialogState,
    title: @Composable () -> Unit = {},
    text: @Composable () -> Unit = {},
    confirmButton: @Composable () -> Unit = {},
    cancelButton: @Composable () -> Unit = {},
) {
    if (state.isShow) {
        AlertDialog(
            onDismissRequest = { state.dismiss() },
            confirmButton = confirmButton,
            dismissButton = cancelButton,
            title = title,
            text = text,
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        ConfirmDialog(rememberDialogState(isShow = true))
    }
}
