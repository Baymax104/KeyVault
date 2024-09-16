package top.baymaxam.keyvault.state

import androidx.compose.runtime.Stable
import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.entity.asItem
import top.baymaxam.keyvault.repo.KeyDao

/**
 * 主页ViewModel
 * @author John
 * @since 12 9月 2024
 */
@Stable
class HomeScreenModel(private val dao: KeyDao) : ScreenModel {

    fun getResentItems(): Flow<List<KeyItem>> {
        return dao.queryOrderedByLastUsedTime().transform { l -> l.map { it.asItem() } }
    }

    fun getItemCount(): Flow<Int> = dao.queryItemCounts()
}