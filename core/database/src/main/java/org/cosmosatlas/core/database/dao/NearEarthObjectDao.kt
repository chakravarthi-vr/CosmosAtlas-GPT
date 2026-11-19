package org.cosmosatlas.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.cosmosatlas.core.database.entity.NearEarthObjectEntity

@Dao
interface NearEarthObjectDao {

    @Query("SELECT * FROM near_earth_objects ORDER BY missDistanceAu ASC")
    fun getAll(): Flow<List<NearEarthObjectEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(objects: List<NearEarthObjectEntity>)

    @Query("DELETE FROM near_earth_objects")
    suspend fun clearAll()
}
