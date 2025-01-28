package top.baymaxam.keyvault.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.entity.asItem
import top.baymaxam.keyvault.repo.KeyRepository
import top.baymaxam.keyvault.util.CachedStateList

/**
 * 添加授权页ViewModel
 * @author John
 * @since 22 1月 2025
 */
class AddAuthViewModel(private val repository: KeyRepository) : ViewModel() {

    val candidateUserItems = CachedStateList<KeyItem>()

    init {
        viewModelScope.launch {
            repository.queryUserItems()
                .map { l -> l.map { it.asItem() } }
                .collect { candidateUserItems.cacheList = it }
        }
    }

    fun searchUserItem(content: String) {
        candidateUserItems.cacheList.filter { it.name.contains(content, true) }
            .let { candidateUserItems.refreshState(it) }
    }
}