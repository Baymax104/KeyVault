package top.baymaxam.keyvault.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.KeyType
import top.baymaxam.keyvault.model.entity.asItem
import top.baymaxam.keyvault.repo.KeyRepository
import top.baymaxam.keyvault.util.CachedStateList

/**
 * 添加授权页ViewModel
 * @author John
 * @since 22 1月 2025
 */
class SelectUserItemViewModel(private val repository: KeyRepository) : ViewModel() {

    val candidateItems = CachedStateList<KeyItem>()

    init {
        viewModelScope.launch {
            repository.queryByType(KeyType.User)
                .map { l -> l.map { it.asItem() } }
                .collect { candidateItems.cacheList = it }
        }
    }

    fun search(content: String) {
        candidateItems.cacheList.filter { it.name.contains(content, true) }
            .let { candidateItems.refreshState(it) }
    }
}