package top.baymaxam.keyvault.state

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.Tag
import top.baymaxam.keyvault.util.StateList

/**
 * 标签列表ViewModel
 * @author John
 * @since 15 9月 2024
 */
class TagListViewModel : ViewModel() {

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
    }

    fun getTagItems(tag: Tag): List<KeyItem> {
        return if (tagItemsMap.containsKey(tag.id)) tagItemsMap[tag.id]!! else emptyList()
    }

}