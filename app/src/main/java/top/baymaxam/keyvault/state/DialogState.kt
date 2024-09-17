package top.baymaxam.keyvault.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable

/**
 * 对话框状态
 * @author John
 * @since 2024/2/23
 */
@Stable
class DialogState(
    isShow: Boolean = false
) {

    val isShow: MutableState<Boolean> = mutableStateOf(isShow)

    companion object {
        val Saver: Saver<DialogState, *> = listSaver(
            save = { listOf(it.isShow.value) },
            restore = { DialogState(it[0]) }
        )
    }

    fun show() {
        isShow.value = true
    }

    fun dismiss() {
        isShow.value = false
    }

}

@Composable
fun rememberDialogState(): DialogState {
    return rememberSaveable(saver = DialogState.Saver) { DialogState() }
}