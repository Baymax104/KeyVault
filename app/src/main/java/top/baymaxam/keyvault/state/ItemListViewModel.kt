package top.baymaxam.keyvault.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.map
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.asEntity
import top.baymaxam.keyvault.model.entity.asItem
import top.baymaxam.keyvault.repo.KeyDao
import top.baymaxam.keyvault.util.replaceAllBy

/**
 * ItemsListScreenModel
 * @author John
 * @since 07 8月 2024
 */
class ItemListViewModel(private val dao: KeyDao) : ViewModel() {

    var isInitialized by mutableStateOf(false)

    val items = mutableStateListOf<SelectedState<KeyItem>>()


    suspend fun getItems() {
        dao.queryAll()
            .map { l -> l.map { SelectedState(it.asItem()) } }
            .collect {
                items.replaceAllBy(it)
                isInitialized = true
            }
    }

    suspend fun removeSelectedItems(): Result<Unit> {
        return runCatching {
            val removedItems = items.filter { it.selected }
            if (removedItems.isEmpty()) {
                return Result.success(Unit)
            }
            removedItems.map { it.value.asEntity() }.let { dao.delete(it) }
        }
    }
}