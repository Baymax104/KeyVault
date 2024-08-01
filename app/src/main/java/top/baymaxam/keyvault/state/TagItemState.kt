package top.baymaxam.keyvault.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import top.baymaxam.keyvault.model.domain.Tag

/**
 * 添加页TagItem状态
 * @author John
 * @since 01 8月 2024
 */
@Stable
data class TagItemState(
    val value: Tag,
    val selected: MutableState<Boolean> = mutableStateOf(false)
)
