package org.cosmosatlas.core.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.cosmosatlas.core.database.CosmosDatabase
import org.cosmosatlas.core.database.dao.ApodDao
import org.cosmosatlas.core.database.dao.DeepSpaceObjectDao
import org.cosmosatlas.core.database.dao.NearEarthObjectDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideCosmosDatabase(
        @ApplicationContext context: Context
    ): CosmosDatabase {
        return Room.databaseBuilder(
            context,
            CosmosDatabase::class.java,
            "cosmos_atlas.db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideApodDao(db: CosmosDatabase): ApodDao = db.apodDao()

    @Provides
    fun provideDeepSpaceObjectDao(db: CosmosDatabase): DeepSpaceObjectDao = db.deepSpaceObjectDao()

    @Provides
    fun provideNearEarthObjectDao(db: CosmosDatabase): NearEarthObjectDao = db.nearEarthObjectDao()
}
