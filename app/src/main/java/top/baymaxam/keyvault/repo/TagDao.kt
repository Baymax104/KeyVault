package top.baymaxam.keyvault.repo

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import top.baymaxam.keyvault.model.entity.TagEntity

/**
 * TagDao
 * @author John
 * @since 27 1月 2025
 */
@Dao
interface TagDao : BaseDao<TagEntity> {

    @Query("select * from t_tag")
    fun queryAll(): Flow<List<TagEntity>>

    @Query("select count(*) from t_tag")
    fun queryCount(): Flow<Int>

    @Query("select count(*) from t_tag where name = :name")
    suspend fun queryCountByName(name: String): Int

    @Query(
        """
            select * from t_tag
            join t_key_tag on t_key_tag.tagId = t_tag.id
            where t_key_tag.keyId = :keyId
        """
    )
    fun queryByKeyId(keyId: String): Flow<List<TagEntity>>

}