package top.baymaxam.keyvault.state

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import top.baymaxam.keyvault.model.domain.AuthItem
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.KeyType
import top.baymaxam.keyvault.model.domain.UserItem
import top.baymaxam.keyvault.model.domain.asEntity
import top.baymaxam.keyvault.model.entity.asItem
import top.baymaxam.keyvault.repo.KeyRepository
import top.baymaxam.keyvault.repo.transaction
import java.util.Date

/**
 * ItemViewModel
 * @author John
 * @since 18 9月 2024
 */
class ItemViewModel(
    private val keyRepository: KeyRepository,
    val item: KeyItem
) : ViewModel() {

    val nameState = mutableStateOf(item.name)
    val commentState = mutableStateOf(item.comment)
    val usernameState = mutableStateOf("")
    val passwordState = mutableStateOf("")
    val authUserItem = mutableStateOf<UserItem?>(null)

    init {
        when (item) {
            is UserItem -> {
                usernameState.value = item.username
                passwordState.value = item.password
            }

            is AuthItem -> {
                if (item.authId.isNotEmpty()) {
                    viewModelScope.launch {
                        keyRepository.queryById(item.authId)
                            .takeIf { it.type == KeyType.User }
                            ?.asItem()
                            ?.let { authUserItem.value = it as UserItem }
                    }
                }
            }
        }
    }

    suspend fun updateItem(): Result<Unit> {
        return runCatching {
            when (item) {
                is UserItem -> updateUserItem()
                is AuthItem -> updateAuthItem()
            }
        }
    }

    fun isItemEquals(): Boolean {
        return when (item) {
            is UserItem -> {
                item.name == nameState.value && item.username == usernameState.value &&
                        item.password == passwordState.value && item.comment == commentState.value
            }

            is AuthItem -> {
                item.name == nameState.value && item.authId == (authUserItem.value?.id ?: "")
            }
        }
    }

    private suspend fun updateUserItem() {
        item as UserItem
        transaction {
            if (item.name != nameState.value) {
                keyRepository.updateAuthName(item.id, nameState.value)
            }
            item.apply {
                name = nameState.value
                username = usernameState.value
                password = passwordState.value
                comment = commentState.value
            }.let {
                keyRepository.update(it.asEntity())
            }
        }
    }

    private suspend fun updateAuthItem() {
        item as AuthItem

    }


    fun updateItemResentDate() {
        viewModelScope.launch {
            item.apply { resentDate = Date() }.let { keyRepository.update(it.asEntity()) }
        }
    }
}