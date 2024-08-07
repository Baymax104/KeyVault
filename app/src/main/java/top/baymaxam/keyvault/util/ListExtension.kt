package top.baymaxam.keyvault.util

import androidx.compose.runtime.mutableStateListOf
import kotlin.properties.Delegates

/**
 * 列表扩展
 * @author John
 * @since 07 8月 2024
 */

fun <E> MutableList<E>.replaceAll(items: Collection<E>) {
    clear()
    addAll(items)
}


class LazyStateList<E> {
    val state = mutableStateListOf<E>()
    val cache by Delegates.observable(mutableListOf<E>()) { _, _, value ->
        if (value.isNotEmpty()) {
            state.replaceAll(value)
        }
    }
}
