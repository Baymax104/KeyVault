package top.baymaxam.keyvault.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import cafe.adriel.voyager.core.model.ScreenModel
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.KeyType
import top.baymaxam.keyvault.util.StateList

/**
 * ItemsListScreenModel
 * @author John
 * @since 07 8月 2024
 */
@Stable
class ItemListScreenModel : ScreenModel {

    val items = listOf<StateList<ItemSelectedState<KeyItem>>>(
        mutableStateListOf(),
        mutableStateListOf(),
        mutableStateListOf(),
    )

    suspend fun loadPage(page: Int) {
        val list0 = listOf(
            ItemSelectedState(KeyItem(name = "test0-web", type = KeyType.Website)),
            ItemSelectedState(KeyItem(name = "test1-web", type = KeyType.Website)),
            ItemSelectedState(KeyItem(name = "test2-web", type = KeyType.Website)),
            ItemSelectedState(KeyItem(name = "test3-web", type = KeyType.Website)),
            ItemSelectedState(KeyItem(name = "test4-web", type = KeyType.Website)),
        )
        val list1 = listOf(
            ItemSelectedState(KeyItem(name = "test0-card", type = KeyType.Card)),
            ItemSelectedState(KeyItem(name = "test1-card", type = KeyType.Card)),
            ItemSelectedState(KeyItem(name = "test2-card", type = KeyType.Card)),
            ItemSelectedState(KeyItem(name = "test3-card", type = KeyType.Card)),
            ItemSelectedState(KeyItem(name = "test4-card", type = KeyType.Card)),
        )
        val list2 = listOf(
            ItemSelectedState(KeyItem(name = "test0-auth", type = KeyType.Authorization)),
            ItemSelectedState(KeyItem(name = "test1-auth", type = KeyType.Authorization)),
            ItemSelectedState(KeyItem(name = "test2-auth", type = KeyType.Authorization)),
            ItemSelectedState(KeyItem(name = "test3-auth", type = KeyType.Authorization)),
            ItemSelectedState(KeyItem(name = "test4-auth", type = KeyType.Authorization)),
        )
        val list = when (page) {
            0 -> list0
            1 -> list1
            2 -> list2
            else -> null
        }
        if (items[page].isEmpty() && list != null) {
            items[page].addAll(list)
        }
    }

    suspend fun removePages() {

    }
}