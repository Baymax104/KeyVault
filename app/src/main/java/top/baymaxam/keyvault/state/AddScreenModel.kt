package top.baymaxam.keyvault.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.PassKeyItem
import top.baymaxam.keyvault.model.domain.PassType
import top.baymaxam.keyvault.model.domain.Tag
import kotlin.properties.Delegates

/**
 * AddScreenViewModel
 * @author John
 * @since 01 8月 2024
 */
@Stable
class AddScreenModel : ScreenModel {

    val tags = mutableStateListOf<TagItemState>()
    private val tagsCache by Delegates.observable(mutableListOf<TagItemState>()) { _, _, newValue ->
        if (newValue.isNotEmpty()) {
            tags.addAll(newValue)
        }
    }

    val passItems = mutableStateListOf<PassKeyItem>()
    private val passItemsCache by Delegates.observable(mutableListOf<PassKeyItem>()) { _, _, newValue ->
        if (newValue.isNotEmpty()) {
            passItems.addAll(newValue)
        }
    }

    val selectedPassItem = mutableStateOf<PassKeyItem?>(null)

    init {
        tagsCache.addAll(
            listOf(
                TagItemState(Tag(id = 0, name = "Hello")),
                TagItemState(Tag(id = 1, name = "Hello1")),
                TagItemState(Tag(id = 2, name = "Hello2")),
                TagItemState(Tag(id = 3, name = "Hello3")),
                TagItemState(Tag(id = 4, name = "Hello4")),
            )
        )

        passItemsCache.addAll(
            listOf(
                PassKeyItem(id = 0, name = "test1", type = PassType.Website, username = "Hello"),
                PassKeyItem(id = 1, name = "test2", type = PassType.Card, username = "Hello"),
                PassKeyItem(id = 2, name = "test3", type = PassType.Website, username = "Hello"),
                PassKeyItem(id = 3, name = "test4", type = PassType.Website, username = "Hello"),
                PassKeyItem(id = 4, name = "test5", type = PassType.Card, username = "Hello"),
            )
        )
    }

    fun searchTag(content: String) {
        tagsCache.filter { it.value.name.contains(content, true) }
            .let { tags.refresh(it) }
    }

    fun searchPassItem(content: String) {
        passItemsCache.filter { it.name.contains(content, true) }
            .let { passItems.refresh(it) }
    }

    fun refreshTags() = tags.refresh(tagsCache)

    fun refreshPassItems() = passItems.refresh(passItemsCache)

    private fun <E> MutableList<E>.refresh(items: Collection<E>) {
        clear()
        addAll(items)
    }

    fun addItem(keyItem: KeyItem): Result<Unit> {
        return Result.success(Unit)
    }
}