package org.cosmosatlas.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.cosmosatlas.core.database.entity.DeepSpaceObjectEntity

@Dao
interface DeepSpaceObjectDao {

    @Query("SELECT * FROM deep_space_objects ORDER BY name ASC")
    fun getAll(): Flow<List<DeepSpaceObjectEntity>>

    @Query("SELECT * FROM deep_space_objects WHERE category = :category ORDER BY name ASC")
    fun getByCategory(category: String): Flow<List<DeepSpaceObjectEntity>>

    @Query("SELECT * FROM deep_space_objects WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): DeepSpaceObjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(objects: List<DeepSpaceObjectEntity>)

    @Query("SELECT COUNT(*) FROM deep_space_objects")
    suspend fun getCount(): Int
}
