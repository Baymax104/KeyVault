package top.baymaxam.keyvault.repo

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.withTransaction
import com.blankj.utilcode.util.Utils
import top.baymaxam.keyvault.model.entity.KeyEntity
import top.baymaxam.keyvault.model.entity.TagEntity

/**
 * Room数据库
 * @author John
 * @since 30 6月 2024
 */
@Database(
    entities = [KeyEntity::class, TagEntity::class],
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

    abstract fun keyDao(): KeyDao

    abstract fun tagDao(): TagDao
}

suspend fun <R> transaction(block: suspend () -> R) = LocalDatabase.Instance.withTransaction(block)