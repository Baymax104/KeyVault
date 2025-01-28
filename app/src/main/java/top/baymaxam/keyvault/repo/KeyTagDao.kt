package top.baymaxam.keyvault.repo

import androidx.room.Dao
import androidx.room.Query
import top.baymaxam.keyvault.model.entity.KeyTagRelation

/**
 * KeyTagDao
 * @author John
 * @since 28 1月 2025
 */
@Dao
interface KeyTagDao : BaseDao<KeyTagRelation> {

    @Query("delete from t_key_tag where keyId = :keyId")
    suspend fun deleteByKeyId(keyId: String)

    @Query("delete from t_key_tag where tagId = :tagId")
    suspend fun deleteByTagId(tagId: String)
}
