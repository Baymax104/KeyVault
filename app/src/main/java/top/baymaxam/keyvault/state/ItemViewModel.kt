package top.baymaxam.keyvault.state

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.asEntity
import top.baymaxam.keyvault.repo.KeyDao

/**
 * ItemViewModel
 * @author John
 * @since 18 9月 2024
 */
class ItemViewModel(private val dao: KeyDao, val item: KeyItem) : ViewModel() {

    val nameState = mutableStateOf(item.name)
    val usernameState = mutableStateOf(item.username)
    val passwordState = mutableStateOf(item.password)
    val commentState = mutableStateOf(item.comment)

    fun checkEquals(): Boolean = with(item) {
        name == nameState.value && username == usernameState.value && password == passwordState.value && comment == commentState.value
    }

    suspend fun updateItem(): Result<Unit> {
        return runCatching {
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
}