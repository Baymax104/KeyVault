package top.baymaxam.keyvault.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import cafe.adriel.voyager.core.model.ScreenModel
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.KeyType

/**
 * ItemsListScreenModel
 * @author John
 * @since 07 8月 2024
 */
@Stable
class ItemListScreenModel : ScreenModel {


    val webItems = mutableStateListOf<ItemSelectedState<KeyItem>>()

    val cardItems = mutableStateListOf<ItemSelectedState<KeyItem>>()

    val authItems = mutableStateListOf<ItemSelectedState<KeyItem>>()


    suspend fun loadItems(page: Int) {
        val list = listOf(
            ItemSelectedState(KeyItem(id = 0, name = "test0-web", type = KeyType.Website)),
            ItemSelectedState(KeyItem(id = 1, name = "test1-web", type = KeyType.Website)),
            ItemSelectedState(KeyItem(id = 2, name = "test2-web", type = KeyType.Website)),
            ItemSelectedState(KeyItem(id = 3, name = "test3-web", type = KeyType.Website)),
            ItemSelectedState(KeyItem(id = 4, name = "test4-web", type = KeyType.Website)),
        )
        val list1 = listOf(
            ItemSelectedState(KeyItem(id = 0, name = "test0-card", type = KeyType.Card)),
            ItemSelectedState(KeyItem(id = 1, name = "test1-card", type = KeyType.Card)),
            ItemSelectedState(KeyItem(id = 2, name = "test2-card", type = KeyType.Card)),
            ItemSelectedState(KeyItem(id = 3, name = "test3-card", type = KeyType.Card)),
            ItemSelectedState(KeyItem(id = 4, name = "test4-card", type = KeyType.Card)),
        )
        val list2 = listOf(
            ItemSelectedState(KeyItem(id = 0, name = "test0-auth", type = KeyType.Authorization)),
            ItemSelectedState(KeyItem(id = 1, name = "test1-auth", type = KeyType.Authorization)),
            ItemSelectedState(KeyItem(id = 2, name = "test2-auth", type = KeyType.Authorization)),
            ItemSelectedState(KeyItem(id = 3, name = "test3-auth", type = KeyType.Authorization)),
            ItemSelectedState(KeyItem(id = 4, name = "test4-auth", type = KeyType.Authorization)),
        )
        when (page) {
            0 -> if (webItems.isEmpty()) {
                webItems.addAll(list)
            }

            1 -> if (cardItems.isEmpty()) {
                cardItems.addAll(list1)
            }

            2 -> if (authItems.isEmpty()) {
                authItems.addAll(list2)
            }
        }
    }
}