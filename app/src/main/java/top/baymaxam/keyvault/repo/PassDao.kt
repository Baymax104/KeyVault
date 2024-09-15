package top.baymaxam.keyvault.repo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import top.baymaxam.keyvault.model.entity.PasswordEntity

/**
 * 密码条目DAO
 * @author John
 * @since 04 7月 2024
 */
@Dao
interface PassDao {

    @Insert
    suspend fun insert(entity: PasswordEntity)

    @Query("select * from t_password")
    suspend fun queryAll(): List<PasswordEntity>

    @Query("select * from t_password where id=:id")
    suspend fun query(id: Long): PasswordEntity

    @Query("select * from t_password where id in (:ids)")
    suspend fun query(ids: List<Long>): List<PasswordEntity>
}