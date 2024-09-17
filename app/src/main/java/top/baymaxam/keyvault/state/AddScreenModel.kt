package top.baymaxam.keyvault.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.Tag
import top.baymaxam.keyvault.model.domain.asEntity
import top.baymaxam.keyvault.model.entity.asItem
import top.baymaxam.keyvault.repo.KeyDao
import top.baymaxam.keyvault.util.CachedStateList

/**
 * AddScreenViewModel
 * @author John
 * @since 01 8月 2024
 */
@Stable
class AddScreenModel(private val dao: KeyDao) : ScreenModel {

    val tags = CachedStateList<SelectedState<Tag>>()

    val items = CachedStateList<KeyItem>()

    val selectedPassItem = mutableStateOf<KeyItem?>(null)

    init {
        tags.cacheList = mutableListOf(
            SelectedState(Tag(name = "Hello")),
            SelectedState(Tag(name = "Hello1")),
            SelectedState(Tag(name = "Hello2")),
            SelectedState(Tag(name = "Hello3")),
            SelectedState(Tag(name = "Hello4")),
        )

        screenModelScope.launch {
            dao.queryNonAuthItem()
                .map { l -> l.map { it.asItem() } }
                .collect { items.cacheList = it.toMutableList() }
        }
    }

    fun searchTag(content: String) {
        tags.cacheList.filter { it.value.name.contains(content, true) }
            .let { tags.refreshState(it) }
    }

    fun searchPassItem(content: String) {
        items.cacheList.filter { it.name.contains(content, true) }
            .let { items.refreshState(it) }
    }

    suspend fun addItem(item: KeyItem): Result<Unit> {
        return runCatching {
            dao.insert(item.asEntity())
        }
    }
}