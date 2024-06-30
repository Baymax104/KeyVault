package top.baymaxam.keyvault.repo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import top.baymaxam.keyvault.model.entity.AuthPassEntity

/**
 * 授权实体DAO
 * @author John
 * @since 30 6月 2024
 */
@Dao
interface AuthDao {

    @Insert
    suspend fun insert(entity: AuthPassEntity): Long

    @Query("select * from t_auth")
    suspend fun queryAll(): MutableList<AuthPassEntity>
}