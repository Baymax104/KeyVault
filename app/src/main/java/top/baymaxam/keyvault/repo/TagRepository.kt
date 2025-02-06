package top.baymaxam.keyvault.repo

import top.baymaxam.keyvault.model.entity.KeyTagRelation
import top.baymaxam.keyvault.model.entity.TagEntity

/**
 * TagRepository
 * @author John
 * @since 29 1月 2025
 */
class TagRepository(
    dao: TagDao,
    private val keyTagDao: KeyTagDao,
) : TagDao by dao {

    suspend fun updateTagsByKeyId(
        keyId: String,
        inserted: List<TagEntity>,
        removed: List<TagEntity>
    ) {
        transaction {
            val insertedRelations = inserted.map { KeyTagRelation(keyId, it.id) }
            val removedRelations = removed.map { KeyTagRelation(keyId, it.id) }
            keyTagDao.insert(insertedRelations)
            keyTagDao.delete(removedRelations)
        }
    }
}