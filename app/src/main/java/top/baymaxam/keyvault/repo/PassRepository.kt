package top.baymaxam.keyvault.repo

import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.entity.asItem

/**
 * 密码条目仓库
 * @author John
 * @since 02 7月 2024
 */
class PassRepository {

    private val passDao = LocalDatabase.Instance.passDao()

    private val authDao = LocalDatabase.Instance.authDao()

    suspend fun getAllItems(): MutableList<KeyItem> {
        val passItems = passDao.queryAll().map { it.asItem() }
        val passAssociation = passItems.associateBy { it.id }
        val authItems = authDao.queryAll().map { it.asItem(passAssociation::getValue) }
        return (passItems + authItems).sortedByDescending { it.lastUsedDate.time }.toMutableList()
    }
}