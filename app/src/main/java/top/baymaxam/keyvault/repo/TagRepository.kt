package top.baymaxam.keyvault.repo

import top.baymaxam.keyvault.model.entity.TagEntity

/**
 * TagRepository
 * @author John
 * @since 29 1月 2025
 */
class TagRepository(
    dao: TagDao,
    private val keyTagDao: KeyTagDao
) : TagDao by dao {

    suspend fun deleteWithKeys(tagEntities: List<TagEntity>) {
        transaction {
            tagEntities.forEach {
                delete(it)
                keyTagDao.deleteByTagId(it.id)
            }
        }
    }
}