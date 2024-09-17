package top.baymaxam.keyvault.state

import androidx.compose.runtime.mutableStateListOf
import cafe.adriel.voyager.core.model.ScreenModel
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.Tag
import top.baymaxam.keyvault.util.StateList

/**
 * 标签列表ViewModel
 * @author John
 * @since 15 9月 2024
 */
class TagListScreenModel : ScreenModel {

    val tags = mutableStateListOf<SelectedState<Tag>>()

    private val tagItemsMap = mutableMapOf<String, StateList<KeyItem>>()

    init {
        val list = listOf(
            SelectedState(Tag(name = "Hello")),
            SelectedState(Tag(name = "Hello")),
            SelectedState(Tag(name = "Hello")),
            SelectedState(Tag(name = "Hello")),
            SelectedState(Tag(name = "Hello")),
        )
        tags.addAll(list)
        list.forEach {
            tagItemsMap[it.value.id] = mutableStateListOf(
                KeyItem(name = "Hello1"),
                KeyItem(name = "Hello2"),
                KeyItem(name = "Hello3"),
                KeyItem(name = "Hello4"),
                KeyItem(name = "Hello5"),
                KeyItem(name = "Hello5"),
                KeyItem(name = "Hello5"),
                KeyItem(name = "Hello5"),
            )
        }
    }

    fun getTagItems(tag: Tag): List<KeyItem> {
        return if (tagItemsMap.containsKey(tag.id)) tagItemsMap[tag.id]!! else emptyList()
    }

}