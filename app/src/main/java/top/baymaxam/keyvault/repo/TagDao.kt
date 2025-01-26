package top.baymaxam.keyvault.repo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import top.baymaxam.keyvault.model.entity.TagEntity

/**
 * TagDao
 * @author John
 * @since 27 1月 2025
 */
@Dao
interface TagDao {

    @Insert
    suspend fun insert(entity: TagEntity)

    @Delete
    suspend fun delete(entity: TagEntity)

    @Delete
    suspend fun delete(entities: List<TagEntity>)

    @Query("select * from t_tag")
    fun queryAll(): Flow<List<TagEntity>>

    @Query("select count(*) from t_tag")
    fun queryCount(): Flow<Int>

    @Query("select count(*) from t_tag where name = :name")
    suspend fun queryCountByName(name: String): Int

}