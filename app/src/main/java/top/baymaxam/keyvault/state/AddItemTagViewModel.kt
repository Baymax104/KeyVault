package top.baymaxam.keyvault.state

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.Tag
import top.baymaxam.keyvault.model.domain.asEntity
import top.baymaxam.keyvault.model.entity.asItem
import top.baymaxam.keyvault.repo.TagRepository
import top.baymaxam.keyvault.util.replaceAllBy

/**
 *
 * @author John
 * @since 29 1月 2025
 */
class AddItemTagViewModel(
    private val tagRepository: TagRepository,
    private val item: KeyItem
) : ViewModel() {

    val tags = mutableStateListOf<SelectedState<Tag>>()

    private val cache = mutableListOf<Tag>()

    init {
        viewModelScope.launch {
            combine(
                tagRepository.queryAll(),
                tagRepository.queryByKeyId(item.id)
            ) { allTagEntities, keyTagEntities ->
                val keyTags = keyTagEntities.map { it.asItem() }
                val allTags = allTagEntities.map { SelectedState(it.asItem()) }
                    .onEach { it.selected = keyTags.any { t -> t.id == it.value.id } }
                allTags to keyTags
            }.collect { (allTags, keyTags) ->
                cache.replaceAllBy(keyTags)
                tags.replaceAllBy(allTags)
            }
        }
    }

    suspend fun updateTags(): Result<Unit> {
        return runCatching {
            val newTags = tags.filter { it.selected }.map { it.value }.map { it.asEntity() }
            val oldTags = cache.map { it.asEntity() }
            val inserted = newTags.filterNot { i1 -> oldTags.any { i2 -> i1.id == i2.id } }
            val removed = oldTags.filterNot { i2 -> newTags.any { i1 -> i1.id == i2.id } }
            tagRepository.updateTagsByKeyId(item.id, inserted, removed)
        }
    }
}