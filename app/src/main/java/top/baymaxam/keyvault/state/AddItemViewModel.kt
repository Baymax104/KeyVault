package top.baymaxam.keyvault.state

import androidx.compose.runtime.mutableStateOf
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
    val selectedUserItemState = mutableStateOf<UserItem?>(null)

    val tags = CachedStateList<SelectedState<Tag>>()

    init {
        viewModelScope.launch {
            tagRepository.queryAll()
                .map { l -> l.map { SelectedState(it.asItem()) } }
                .collect { tags.cacheList = it }
        }
    }

    fun refreshInput() {
        nameContentState.value = ""
        usernameContentState.value = ""
        passwordContentState.value = ""
        selectedUserItemState.value = null
        commentContentState.value = ""
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
                    authId = selectedUserItemState.value?.id ?: "",
                    authName = selectedUserItemState.value?.name ?: "",
                    createDate = Date(),
                )
            }
            val selectedTags = tags.state.filter { it.selected }.map { it.value.asEntity() }
            keyRepository.insertWithTags(item.asEntity(), selectedTags)
        }
    }

}