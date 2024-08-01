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
    suspend fun insertOne(entity: PasswordEntity): Long

    @Query("select * from t_password")
    suspend fun queryAll(): MutableList<PasswordEntity>
}