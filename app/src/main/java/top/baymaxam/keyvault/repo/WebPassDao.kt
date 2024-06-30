package top.baymaxam.keyvault.repo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import top.baymaxam.keyvault.model.entity.WebPassEntity

/**
 * 网站密码DAO
 * @author John
 * @since 30 6月 2024
 */
@Dao
interface WebPassDao {

    @Insert
    suspend fun insertOne(entity: WebPassEntity): Long

    @Query("select * from t_web where id = :id")
    suspend fun queryOne(id: Long): WebPassEntity

    @Query("select * from t_web")
    suspend fun queryAll(): MutableList<WebPassEntity>
}