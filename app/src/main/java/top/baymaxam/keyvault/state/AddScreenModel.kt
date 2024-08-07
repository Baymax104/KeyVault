package top.baymaxam.keyvault.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.KeyType
import top.baymaxam.keyvault.model.domain.Tag
import top.baymaxam.keyvault.util.replaceAll
import kotlin.properties.Delegates

/**
 * AddScreenViewModel
 * @author John
 * @since 01 8月 2024
 */
@Stable
class AddScreenModel : ScreenModel {

    val tags = mutableStateListOf<ItemSelectedState<Tag>>()
    private val tagsCache by Delegates.observable(mutableListOf<ItemSelectedState<Tag>>()) { _, _, value ->
        if (value.isNotEmpty()) {
            tags.replaceAll(value)
        }
    }

    val passItems = mutableStateListOf<KeyItem>()
    private val passItemsCache by Delegates.observable(mutableListOf<KeyItem>()) { _, _, value ->
        if (value.isNotEmpty()) {
            passItems.replaceAll(value)
        }
    }

    val selectedPassItem = mutableStateOf<KeyItem?>(null)

    init {
        tagsCache.addAll(
            listOf(
                ItemSelectedState(Tag(id = 0, name = "Hello")),
                ItemSelectedState(Tag(id = 1, name = "Hello1")),
                ItemSelectedState(Tag(id = 2, name = "Hello2")),
                ItemSelectedState(Tag(id = 3, name = "Hello3")),
                ItemSelectedState(Tag(id = 4, name = "Hello4")),
            )
        )

        passItemsCache.addAll(
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
        tagsCache.filter { it.value.name.contains(content, true) }
            .let { tags.replaceAll(it) }
    }

    fun searchPassItem(content: String) {
        passItemsCache.filter { it.name.contains(content, true) }
            .let { passItems.replaceAll(it) }
    }

    fun refreshTags() = tags.replaceAll(tagsCache)

    fun refreshPassItems() = passItems.replaceAll(passItemsCache)

    suspend fun addItem(keyItem: KeyItem): Result<Unit> {
        return Result.success(Unit)
    }
}