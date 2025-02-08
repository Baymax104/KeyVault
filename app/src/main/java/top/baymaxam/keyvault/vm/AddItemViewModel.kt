package top.baymaxam.keyvault.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import top.baymaxam.keyvault.model.domain.AuthItem
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.KeyType
import top.baymaxam.keyvault.model.domain.Tag
import top.baymaxam.keyvault.model.domain.UserItem
import top.baymaxam.keyvault.model.domain.asEntity
import top.baymaxam.keyvault.model.entity.asItem
import top.baymaxam.keyvault.repo.KeyRepository
import top.baymaxam.keyvault.repo.TagRepository
import top.baymaxam.keyvault.state.SelectedState
import top.baymaxam.keyvault.util.CachedStateList
import java.util.Date

/**
 * AddScreenViewModel
 * @author John
 * @since 01 8月 2024
 */
class AddItemViewModel(
    private val keyRepository: KeyRepository,
    private val tagRepository: TagRepository,
) : ViewModel() {

    val nameContentState = mutableStateOf("")
    val usernameContentState = mutableStateOf("")
    val passwordContentState = mutableStateOf("")
    val commentContentState = mutableStateOf("")
    val typeSelectedState = mutableStateOf(KeyType.User)
    var selectedUserItem by mutableStateOf<UserItem?>(null)

    val tags = CachedStateList<SelectedState<Tag>>()

    init {
        viewModelScope.launch {
            tagRepository.queryAll()
                .map { l -> l.map { SelectedState(it.asItem()) } }
                .collect { tags.cacheList = it }
        }
    }

    fun searchTag(content: String) {
        tags.cacheList
            .filter { it.value.name.contains(content, true) }
            .let { tags.refreshState(it) }
    }

    suspend fun addItem(): Result<Unit> {
        return runCatching {
            if (nameContentState.value.isEmpty()) {
                throw IllegalArgumentException("名称不能为空")
            }
            val item: KeyItem = when (typeSelectedState.value) {
                KeyType.User -> UserItem(
                    name = nameContentState.value,
                    username = usernameContentState.value,
                    password = passwordContentState.value,
                    comment = commentContentState.value,
                    createDate = Date(),
                )

                KeyType.Authorization -> AuthItem(
                    name = nameContentState.value,
                    comment = commentContentState.value,
                    authId = selectedUserItem?.id ?: "",
                    authName = selectedUserItem?.name ?: "",
                    createDate = Date(),
                )
            }
            val selectedTags = tags.state.filter { it.selected }.map { it.value.asEntity() }
            keyRepository.insertWithTags(item.asEntity(), selectedTags)
        }
    }

}