package top.baymaxam.keyvault.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.map
import top.baymaxam.keyvault.model.domain.KeyItem
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
class ItemListScreenModel(private val dao: KeyDao) : ScreenModel {

    val items = listOf<StateList<ItemSelectedState<KeyItem>>>(
        mutableStateListOf(),
        mutableStateListOf(),
        mutableStateListOf(),
    )

    suspend fun getPageItems(page: Int) {
        val type = page.toKeyType() ?: error("Invalid page: $page")
        dao.queryByType(type)
            .map { l -> l.map { ItemSelectedState(it.asItem()) } }
            .collect(items[page]::replaceAllBy)
    }

    suspend fun removePages() {

    }
}