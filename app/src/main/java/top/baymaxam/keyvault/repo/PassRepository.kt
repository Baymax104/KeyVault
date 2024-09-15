package top.baymaxam.keyvault.repo

import top.baymaxam.keyvault.model.domain.KeyItem
import top.baymaxam.keyvault.model.domain.KeyType
import top.baymaxam.keyvault.model.domain.asAuthEntity
import top.baymaxam.keyvault.model.domain.asPasswordEntity
import top.baymaxam.keyvault.model.entity.asItem

/**
 * 密码条目仓库
 * @author John
 * @since 02 7月 2024
 */
class PassRepository {

    private val passDao = LocalDatabase.Instance.passDao()
    private val authDao = LocalDatabase.Instance.authDao()

    private val itemCache = mutableMapOf<String, KeyItem>()

    suspend fun getAllItems(): List<KeyItem> {
        if (itemCache.isEmpty()) {
            val passItems = passDao.queryAll().map { it.asItem() }
            val passAssociation = passItems.associateBy { it.id }
            val authItems = authDao.queryAll().map { e -> e.asItem { passAssociation[it] } }
            itemCache.putAll((passItems + authItems).associateBy { it.id })
        }
        return itemCache.values.toList()
    }

    suspend fun addItem(item: KeyItem) {
        if (item.type != KeyType.Authorization) {
            passDao.insert(item.asPasswordEntity())
        } else {
            authDao.insert(item.asAuthEntity())
        }
        itemCache[item.id] = item
    }

    suspend fun removeItem(item: KeyItem) {
        if (item.type != KeyType.Authorization) {
            passDao.delete(item.asPasswordEntity())
        } else {
            authDao.delete(item.asAuthEntity())
        }
        itemCache.remove(item.id)
    }
}