package org.cosmosatlas.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import org.cosmosatlas.core.database.dao.ApodDao
import org.cosmosatlas.core.database.dao.DeepSpaceObjectDao
import org.cosmosatlas.core.database.dao.NearEarthObjectDao
import org.cosmosatlas.core.database.entity.ApodEntity
import org.cosmosatlas.core.database.entity.DeepSpaceObjectEntity
import org.cosmosatlas.core.database.entity.NearEarthObjectEntity

@Database(
    entities = [
        ApodEntity::class,
        DeepSpaceObjectEntity::class,
        NearEarthObjectEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CosmosDatabase : RoomDatabase() {
    abstract fun apodDao(): ApodDao
    abstract fun deepSpaceObjectDao(): DeepSpaceObjectDao
    abstract fun nearEarthObjectDao(): NearEarthObjectDao
}
