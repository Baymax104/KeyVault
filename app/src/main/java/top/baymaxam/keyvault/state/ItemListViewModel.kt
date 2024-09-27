package top.baymaxam.keyvault.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.map
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.asEntity
import top.baymaxam.keyvault.model.domain.toKeyType
import top.baymaxam.keyvault.model.entity.asItem
import top.baymaxam.keyvault.repo.KeyDao
import top.baymaxam.keyvault.util.StateList
import top.baymaxam.keyvault.util.replaceAllBy

/**
 * ItemsListScreenModel
 * @author John
 * @since 07 8月 2024
 */
@Stable
class ItemListViewModel(private val dao: KeyDao) : ViewModel() {

    var selectedNumber by mutableIntStateOf(0)

    val isItemsLoading = listOf(
        mutableStateOf(true),
        mutableStateOf(true),
        mutableStateOf(true)
    )

    val items = listOf<StateList<SelectedState<KeyItem>>>(
        mutableStateListOf(),
        mutableStateListOf(),
        mutableStateListOf(),
    )

    fun clearSelectedState(page: Int) {
        selectedNumber = 0
        items[page].forEach { it.selected = false }
    }

    suspend fun getPageItems(page: Int) {
        if (items[page].isNotEmpty()) return
        val type = page.toKeyType() ?: error("Invalid page: $page")
        dao.queryByType(type)
            .map { l -> l.map { SelectedState(it.asItem()) } }
            .collect {
                items[page].replaceAllBy(it)
                isItemsLoading[page].value = false
            }
    }

    suspend fun removeSelectedItems(page: Int): Result<Unit> {
        return runCatching {
            val removedItems = items[page].filter { it.selected }
            if (removedItems.isEmpty()) {
                return Result.success(Unit)
            }
            removedItems.map { it.value.asEntity() }.let { dao.delete(it) }
        }
    }
}