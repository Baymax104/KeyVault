package top.baymaxam.keyvault.repo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import top.baymaxam.keyvault.model.entity.CardPassEntity

/**
 * 卡片密码DAO
 * @author John
 * @since 30 6月 2024
 */
@Dao
interface CardPassDao {

    @Insert
    suspend fun insertOne(entity: CardPassEntity): Long

    @Query("select * from t_card")
    suspend fun queryAll(): MutableList<CardPassEntity>
}