package top.baymaxam.keyvault.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.Tag
import top.baymaxam.keyvault.model.domain.asEntity
import top.baymaxam.keyvault.model.entity.asItem
import top.baymaxam.keyvault.repo.KeyRepository
import top.baymaxam.keyvault.util.replaceAllBy

/**
 * TagItemListViewModel
 * @author John
 * @since 31 1月 2025
 */
class TagItemListViewModel(
    private val keyRepository: KeyRepository,
    val tag: Tag
) : ViewModel() {

    val items = mutableStateListOf<SelectedState<KeyItem>>()

    var isInitialized by mutableStateOf(false)

    init {
        viewModelScope.launch {
            keyRepository.queryByTagId(tag.id)
                .map { l -> l.map { SelectedState(it.asItem()) } }
                .collect {
                    items.replaceAllBy(it)
                    isInitialized = true
                }
        }
    }

    suspend fun removeSelectedItem(): Result<Unit> {
        return runCatching {
            val selectedItems = items.filter { it.selected }.map { it.value.asEntity() }
            keyRepository.deleteTagItems(tag.asEntity(), selectedItems)
        }
    }
}