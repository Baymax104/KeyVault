package top.baymaxam.keyvault.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf

/**
 * 列表项被选中状态
 * @author John
 * @since 01 8月 2024
 */
@Stable
data class ItemSelectedState<out T>(
    val value: T,
    val selected: MutableState<Boolean> = mutableStateOf(false)
)
