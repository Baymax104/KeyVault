package top.baymaxam.keyvault.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

/**
 * 对话框状态
 * @author John
 * @since 2024/2/23
 */
@Stable
class DialogState(
    isShow: Boolean = false
) {
    var isShow by mutableStateOf(isShow)

    companion object {
        val Saver: Saver<DialogState, *> = listSaver(
            save = { listOf(it.isShow) },
            restore = { DialogState(it[0]) }
        )
    }

    fun show() {
        isShow = true
    }

    fun dismiss() {
        isShow = false
    }
}

@Composable
fun rememberDialogState(): DialogState {
    return rememberSaveable(saver = DialogState.Saver) { DialogState() }
}