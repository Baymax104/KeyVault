package top.baymaxam.keyvault.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.Tag
import top.baymaxam.keyvault.repo.KeyRepository

/**
 * TagItemListViewModel
 * @author John
 * @since 31 1月 2025
 */
class TagItemListViewModel(
    private val keyRepository: KeyRepository,
    val tag: Tag
) : ViewModel() {

    val items = mutableStateListOf<SelectedState<KeyItem>>()

    var isInitialized by mutableStateOf(false)

}