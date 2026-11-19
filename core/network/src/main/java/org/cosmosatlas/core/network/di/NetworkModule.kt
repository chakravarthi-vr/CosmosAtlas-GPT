package org.cosmosatlas.core.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.cosmosatlas.core.network.AstrophysicsRepository
import org.cosmosatlas.core.network.AstrophysicsRepositoryImpl
import org.cosmosatlas.core.network.api.HelioviewerApi
import org.cosmosatlas.core.network.api.JplHorizonsApi
import org.cosmosatlas.core.network.api.NasaApodApi
import org.cosmosatlas.core.network.api.NasaCneosApi
import org.cosmosatlas.core.network.api.NasaExoplanetApi
import org.cosmosatlas.core.network.api.NasaImageLibraryApi
import org.cosmosatlas.core.network.client.RateLimitAndRetryInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @Named("NasaApiKey") apiKey: String
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        val rateLimitInterceptor = RateLimitAndRetryInterceptor(
            apiKeyProvider = { apiKey }
        )

        return OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(rateLimitInterceptor)
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideNasaApodApi(okHttpClient: OkHttpClient, json: Json): NasaApodApi {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl("https://api.nasa.gov/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(NasaApodApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNasaImageLibraryApi(okHttpClient: OkHttpClient, json: Json): NasaImageLibraryApi {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl("https://images-api.nasa.gov/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(NasaImageLibraryApi::class.java)
    }

    @Provides
    @Singleton
    fun provideJplHorizonsApi(okHttpClient: OkHttpClient): JplHorizonsApi {
        return Retrofit.Builder()
            .baseUrl("https://ssd.jpl.nasa.gov/")
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(JplHorizonsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNasaExoplanetApi(okHttpClient: OkHttpClient, json: Json): NasaExoplanetApi {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl("https://exoplanetarchive.ipac.caltech.edu/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(NasaExoplanetApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNasaCneosApi(okHttpClient: OkHttpClient, json: Json): NasaCneosApi {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl("https://ssd-api.jpl.nasa.gov/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(NasaCneosApi::class.java)
    }

    @Provides
    @Singleton
    fun provideHelioviewerApi(okHttpClient: OkHttpClient): HelioviewerApi {
        return Retrofit.Builder()
            .baseUrl("https://api.helioviewer.org/")
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(HelioviewerApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAstrophysicsRepository(impl: AstrophysicsRepositoryImpl): AstrophysicsRepository = impl
}
