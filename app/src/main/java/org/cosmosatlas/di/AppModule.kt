package org.cosmosatlas.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.cosmosatlas.BuildConfig
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @Named("NasaApiKey")
    fun provideNasaApiKey(): String {
        return BuildConfig.NASA_API_KEY
    }
}
