package top.baymaxam.keyvault.repo

import top.baymaxam.keyvault.model.entity.KeyEntity
import top.baymaxam.keyvault.model.entity.KeyTagRelation
import top.baymaxam.keyvault.model.entity.TagEntity

/**
 * KeyRepository
 * @author John
 * @since 29 1月 2025
 */
class KeyRepository(
    dao: KeyDao,
    private val keyTagDao: KeyTagDao
) : KeyDao by dao {

    suspend fun insertWithTags(keyEntity: KeyEntity, tagEntities: List<TagEntity>) {
        transaction {
            insert(keyEntity)
            val keyTagRelations = tagEntities.map { KeyTagRelation(keyEntity.id, it.id) }
            keyTagDao.insert(keyTagRelations)
        }
    }

}