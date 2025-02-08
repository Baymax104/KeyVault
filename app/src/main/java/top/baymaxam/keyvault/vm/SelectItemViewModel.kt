package top.baymaxam.keyvault.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.Tag
import top.baymaxam.keyvault.model.domain.asEntity
import top.baymaxam.keyvault.model.entity.asItem
import top.baymaxam.keyvault.repo.KeyRepository
import top.baymaxam.keyvault.state.SelectedState
import top.baymaxam.keyvault.util.CachedStateList

/**
 * SelectItemViewModel
 * @author John
 * @since 06 2月 2025
 */
class SelectItemViewModel(
    private val repository: KeyRepository,
    val tag: Tag
) : ViewModel() {

    val candidateItems = CachedStateList<SelectedState<KeyItem>>()

    init {
        viewModelScope.launch {
            repository.queryAll()
                .map { l -> l.map { SelectedState(it.asItem()) } }
                .collect { candidateItems.cacheList = it }
        }
    }

    fun search(content: String) {
        candidateItems.cacheList.filter { it.value.name.contains(content, true) }
            .let { candidateItems.refreshState(it) }
    }

    suspend fun addItem(): Result<Unit> {
        return runCatching {
            val items = candidateItems.state.filter { it.selected }.map { it.value.asEntity() }
            repository.insertTagItems(tag.asEntity(), items)
        }
    }
}