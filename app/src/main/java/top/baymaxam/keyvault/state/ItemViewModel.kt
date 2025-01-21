package top.baymaxam.keyvault.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import top.baymaxam.keyvault.model.domain.AuthItem
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.UserItem
import top.baymaxam.keyvault.model.domain.asEntity
import top.baymaxam.keyvault.repo.KeyDao
import java.util.Date

/**
 * ItemViewModel
 * @author John
 * @since 18 9月 2024
 */
class ItemViewModel(private val dao: KeyDao, val item: KeyItem) : ViewModel() {

    val nameState = mutableStateOf(item.name)
    val commentState = mutableStateOf(item.comment)
    val usernameState: MutableState<String>
    val passwordState: MutableState<String>

    init {
        when (item) {
            is UserItem -> {
                usernameState = mutableStateOf(item.username)
                passwordState = mutableStateOf(item.password)
            }

            is AuthItem -> {
                usernameState = mutableStateOf("")
                passwordState = mutableStateOf("")
            }
        }
    }

    fun checkEquals(): Boolean {
        return if (item is UserItem) {
            with(item) {
                name == nameState.value && username == usernameState.value && password == passwordState.value && comment == commentState.value
            }
        } else {
            false
        }
    }

    suspend fun updateItem(): Result<Unit> {
        return runCatching {
            if (item !is UserItem) {
                throw IllegalArgumentException("item is not UserItem")
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

    fun updateItemResentDate() {
        viewModelScope.launch {
            item.apply { resentDate = Date() }.let { dao.update(it.asEntity()) }
        }
    }
}