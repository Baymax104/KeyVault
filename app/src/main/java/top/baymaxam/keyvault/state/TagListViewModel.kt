package top.baymaxam.keyvault.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.map
import top.baymaxam.keyvault.model.domain.Tag
import top.baymaxam.keyvault.model.domain.asEntity
import top.baymaxam.keyvault.model.entity.asItem
import top.baymaxam.keyvault.repo.TagDao
import top.baymaxam.keyvault.util.replaceAllBy

/**
 * 标签列表ViewModel
 * @author John
 * @since 15 9月 2024
 */
class TagListViewModel(private val dao: TagDao) : ViewModel() {

    val tags = mutableStateListOf<SelectedState<Tag>>()

    var isInitialized by mutableStateOf(false)

    suspend fun getTags() {
        dao.queryAll()
            .map { l -> l.map { SelectedState(it.asItem()) } }
            .collect {
                tags.replaceAllBy(it)
                isInitialized = true
            }
    }

    suspend fun addTag(tag: Tag): Result<Unit> {
        return runCatching {
            if (tag.name.isEmpty()) {
                throw IllegalArgumentException("名称不能为空")
            }
            val count = dao.queryCountByName(tag.name)
            if (count > 0) {
                throw IllegalArgumentException("存在相同标签")
            }
            dao.insert(tag.asEntity())
        }
    }

    suspend fun removeSelectedTags(): Result<Unit> {
        return runCatching {
            val removedTags = tags.filter { it.selected }
            if (removedTags.isEmpty()) {
                return Result.success(Unit)
            }
            removedTags.map { it.value.asEntity() }.let { dao.delete(it) }
        }
    }
}