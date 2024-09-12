package top.baymaxam.keyvault.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.KeyType

/**
 * 主页ViewModel
 * @author John
 * @since 12 9月 2024
 */
@Stable
class HomeScreenModel : ScreenModel {

    val resentUsedItems = mutableStateListOf<KeyItem>()

    init {
        screenModelScope.launch {
            val list = mutableStateListOf(
                KeyItem(
                    id = 0,
                    name = "测试",
                    username = "wzy1048168235@bjut.edu.cn",
                    type = KeyType.Website,
                    password = "wzy1048168235."
                ),
                KeyItem(id = 1, name = "TestCard", username = "code", type = KeyType.Card),
                KeyItem(id = 2, name = "AuthTest", type = KeyType.Authorization)
            )
            resentUsedItems.addAll(list)
        }
    }


}