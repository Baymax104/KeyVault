package top.baymaxam.keyvault.repo

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.blankj.utilcode.util.Utils
import top.baymaxam.keyvault.model.entity.AuthPassEntity
import top.baymaxam.keyvault.model.entity.CardPassEntity
import top.baymaxam.keyvault.model.entity.WebPassEntity

/**
 * Room数据库
 * @author John
 * @since 30 6月 2024
 */
@Database(
    entities = [
        WebPassEntity::class,
        CardPassEntity::class,
        AuthPassEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class LocalDatabase : RoomDatabase() {

    companion object {
        val Instance = Room.databaseBuilder(
            context = Utils.getApp(),
            klass = LocalDatabase::class.java,
            name = "key_vault"
        ).build()
    }

    abstract fun webPassDao(): WebPassDao

    abstract fun cardPassDao(): CardPassDao

    abstract fun authDao(): AuthDao

}