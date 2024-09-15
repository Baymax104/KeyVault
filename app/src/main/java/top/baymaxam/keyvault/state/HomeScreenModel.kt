package top.baymaxam.keyvault.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.KeyType
import top.baymaxam.keyvault.repo.PassRepository

/**
 * 主页ViewModel
 * @author John
 * @since 12 9月 2024
 */
@Stable
class HomeScreenModel(private val repo: PassRepository) : ScreenModel {

    val resentUsedItems = mutableStateListOf<KeyItem>()

    init {
        screenModelScope.launch {
            val list = mutableStateListOf(
                KeyItem(
                    name = "测试",
                    username = "wzy1048168235@bjut.edu.cn",
                    type = KeyType.Website,
                    password = "wzy1048168235."
                ),
                KeyItem(name = "TestCard", username = "code", type = KeyType.Card),
                KeyItem(name = "AuthTest", type = KeyType.Authorization)
            )
            resentUsedItems.addAll(list)
        }
    }


}