package top.baymaxam.keyvault.repo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import top.baymaxam.keyvault.model.domain.KeyType
import top.baymaxam.keyvault.model.entity.KeyEntity

/**
 * 密码条目DAO
 * @author John
 * @since 04 7月 2024
 */
@Dao
interface KeyDao {

    @Insert
    suspend fun insert(entity: KeyEntity)

    @Delete
    suspend fun delete(entity: KeyEntity)

    @Delete
    suspend fun delete(entities: List<KeyEntity>)

    @Query("select * from t_key")
    fun queryAll(): Flow<List<KeyEntity>>

    @Query("select * from t_key where lastUsedDate != 0 order by lastUsedDate DESC limit 10")
    fun queryOrderedByLastUsedTime(): Flow<List<KeyEntity>>

    @Query("select count(*) from t_key")
    fun queryItemCounts(): Flow<Int>

    @Query("select * from t_key where type != 'Authorization'")
    fun queryNonAuthItem(): Flow<List<KeyEntity>>

    @Query("select * from t_key where id = :id")
    suspend fun queryById(id: Long): KeyEntity

    @Query("select * from t_key where type = :type")
    fun queryByType(type: KeyType): Flow<List<KeyEntity>>
}