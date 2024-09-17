package top.baymaxam.keyvault.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * 列表项被选中状态
 * @author John
 * @since 01 8月 2024
 */
@Stable
data class SelectedState<out T>(val value: T) {
    var selected by mutableStateOf(false)
}
