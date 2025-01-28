package top.baymaxam.keyvault.repo

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * BaseDao
 * @author John
 * @since 28 1月 2025
 */
interface BaseDao<E> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: E)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entities: List<E>)

    @Update
    suspend fun update(entity: E)

    @Delete
    suspend fun delete(entity: E)

    @Delete
    suspend fun delete(entities: List<E>)
}