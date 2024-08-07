package top.baymaxam.keyvault.util

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlin.properties.Delegates

/**
 * 列表扩展
 * @author John
 * @since 07 8月 2024
 */

typealias StateList<E> = SnapshotStateList<E>

fun <E> MutableList<E>.replaceAllBy(items: Collection<E>) {
    clear()
    addAll(items)
}


class CachedStateList<E> {
    val state = mutableStateListOf<E>()
    private val cache by Delegates.observable(mutableListOf<E>()) { _, _, value ->
        if (value.isNotEmpty()) {
            state.replaceAllBy(value)
        }
    }

    val originList: List<E> get() = cache

    fun refreshState(elements: Collection<E>? = null) = state.replaceAllBy(elements ?: cache)

    fun addAll(elements: Collection<E>) = cache.addAll(elements)
}
