package top.baymaxam.keyvault.state

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import top.baymaxam.keyvault.model.domain.AuthItem
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.UserItem
import top.baymaxam.keyvault.model.domain.asEntity
import top.baymaxam.keyvault.repo.KeyDao
import top.baymaxam.keyvault.repo.transaction
import java.util.Date

/**
 * ItemViewModel
 * @author John
 * @since 18 9月 2024
 */
class ItemViewModel(private val dao: KeyDao, val item: KeyItem) : ViewModel() {

    val nameState = mutableStateOf(item.name)
    val commentState = mutableStateOf(item.comment)
    val usernameState = mutableStateOf("")
    val passwordState = mutableStateOf("")

    init {
        when (item) {
            is UserItem -> {
                usernameState.value = item.username
                passwordState.value = item.password
            }

            is AuthItem -> {
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

    fun isUserItemEquals(): Boolean {
        item as UserItem
        return item.name == nameState.value &&
                item.username == usernameState.value &&
                item.password == passwordState.value &&
                item.comment == commentState.value
    }

    private suspend fun updateUserItem() {
        item as UserItem
        transaction {
            if (item.name != nameState.value) {
                dao.updateAuthName(item.id, nameState.value)
            }
            item.apply {
                name = nameState.value
                username = usernameState.value
                password = passwordState.value
                comment = commentState.value
            }.let {
                dao.update(it.asEntity())
            }
        }
    }

    private suspend fun updateAuthItem() {
        item as AuthItem

    }


    fun updateItemResentDate() {
        viewModelScope.launch {
            item.apply { resentDate = Date() }.let { dao.update(it.asEntity()) }
        }
    }
}