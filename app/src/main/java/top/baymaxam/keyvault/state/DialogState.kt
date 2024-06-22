package top.baymaxam.keyvault.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

/**
 * 对话框状态
 * @author John
 * @since 2024/2/23
 */
@Stable
class DialogState {

    val isShow: MutableState<Boolean> = mutableStateOf(false)

    var params: Any = Unit

    fun show(params: Any = Unit) {
        isShow.value = true
        this.params = params
    }

    fun dismiss() {
        isShow.value = false
    }

}

@Composable
fun rememberDialogState(): DialogState {
    return remember { DialogState() }
}