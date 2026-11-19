package org.cosmosatlas.core.network

import kotlinx.coroutines.flow.Flow
import org.cosmosatlas.core.model.ApodRecord
import org.cosmosatlas.core.model.CelestialBodyDossier
import org.cosmosatlas.core.model.CelestialCategory
import org.cosmosatlas.core.model.DeepSpaceObject
import org.cosmosatlas.core.model.EphemerisVector
import org.cosmosatlas.core.model.NearEarthObject
import org.cosmosatlas.core.model.SolarWavelength
import org.cosmosatlas.core.model.SpaceWeatherTelemetry

interface AstrophysicsRepository {

    fun getApodToday(forceRefresh: Boolean = false): Flow<ApodRecord>

    suspend fun toggleApodBookmark(date: String, bookmarked: Boolean)

    fun getBookmarkedApods(): Flow<List<ApodRecord>>

    fun getSolarSystemBodyDossier(bodyId: String): Flow<CelestialBodyDossier>

    suspend fun fetchJplHorizonsVector(bodyId: String, dateIso: String): EphemerisVector?

    fun getDeepSpaceCatalog(category: CelestialCategory? = null): Flow<List<DeepSpaceObject>>

    fun getSpaceWeatherTelemetry(): Flow<SpaceWeatherTelemetry>

    fun getHelioviewerImageUrl(wavelength: SolarWavelength, dateIso: String): String

    fun getNearEarthObjects(): Flow<List<NearEarthObject>>
}
