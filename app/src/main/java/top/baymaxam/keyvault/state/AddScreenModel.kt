package top.baymaxam.keyvault.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.KeyType
import top.baymaxam.keyvault.model.domain.Tag
import top.baymaxam.keyvault.util.CachedStateList

/**
 * AddScreenViewModel
 * @author John
 * @since 01 8月 2024
 */
@Stable
class AddScreenModel : ScreenModel {

    val tags = CachedStateList<ItemSelectedState<Tag>>()

    val passItems = CachedStateList<KeyItem>()

    val selectedPassItem = mutableStateOf<KeyItem?>(null)

    init {
        tags.replaceAll(
            listOf(
                ItemSelectedState(Tag(id = 0, name = "Hello")),
                ItemSelectedState(Tag(id = 1, name = "Hello1")),
                ItemSelectedState(Tag(id = 2, name = "Hello2")),
                ItemSelectedState(Tag(id = 3, name = "Hello3")),
                ItemSelectedState(Tag(id = 4, name = "Hello4")),
            )
        )

        passItems.replaceAll(
            listOf(
                KeyItem(id = 0, name = "test1", type = KeyType.Website, username = "Hello"),
                KeyItem(id = 1, name = "test2", type = KeyType.Card, username = "Hello"),
                KeyItem(id = 2, name = "test3", type = KeyType.Website, username = "Hello"),
                KeyItem(id = 3, name = "test4", type = KeyType.Website, username = "Hello"),
                KeyItem(id = 4, name = "test5", type = KeyType.Card, username = "Hello"),
            )
        )
    }

    fun searchTag(content: String) {
        tags.originList.filter { it.value.name.contains(content, true) }
            .let { tags.refreshState(it) }
    }

    fun searchPassItem(content: String) {
        passItems.originList.filter { it.name.contains(content, true) }
            .let { passItems.refreshState(it) }
    }

    suspend fun addItem(keyItem: KeyItem): Result<Unit> {
        return Result.success(Unit)
    }
}