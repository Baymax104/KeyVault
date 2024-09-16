package top.baymaxam.keyvault.state

import androidx.compose.runtime.Stable
import cafe.adriel.voyager.core.model.ScreenModel
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.repo.PassRepository

/**
 * 主页ViewModel
 * @author John
 * @since 12 9月 2024
 */
@Stable
class HomeScreenModel(private val repo: PassRepository) : ScreenModel {

    suspend fun getResentItems(): List<KeyItem> {
        return repo.getAllItems()
            .filter { it.lastUsedDate.time != 0L }
            .take(10)
            .sortedByDescending { it.lastUsedDate.time }
    }

    suspend fun getItemCount(): Int = repo.getAllItems().size


}