package org.cosmosatlas.core.model

import kotlinx.serialization.Serializable
import org.cosmosatlas.core.astromath.CelestialBodyType
import org.cosmosatlas.core.astromath.PhysicalTelemetry
import org.cosmosatlas.core.astromath.Vector3D

@Serializable
data class CelestialImage(
    val title: String,
    val description: String,
    val thumbnailUrl: String,
    val fullResUrl: String,
    val mission: String, // e.g. "JWST", "Hubble", "Juno", "Cassini", "New Horizons"
    val dateCreated: String
)

data class EphemerisVector(
    val timestamp: String,
    val positionAU: Vector3D,
    val distanceSunAU: Double,
    val distanceSunKm: Double,
    val velocityKmS: Double,
    val source: String = "JPL Horizons On-Line Ephemeris System"
)

data class CelestialBodyDossier(
    val id: String,
    val name: String,
    val bodyType: CelestialBodyType,
    val telemetry: PhysicalTelemetry,
    val semiMajorAxisAU: Double,
    val eccentricity: Double,
    val inclinationDeg: Double,
    val formation: String,
    val internalStructure: String,
    val magnetosphere: String,
    val explorationHistory: String,
    val galleryImages: List<CelestialImage> = emptyList(),
    val realTimeEphemeris: EphemerisVector? = null
)

enum class CelestialCategory {
    NEBULA,
    GALAXY,
    STAR_CLUSTER,
    BLACK_HOLE,
    SUPERNOVA_REMNANT,
    EXOPLANET
}

data class MultiWavelengthData(
    val opticalUrl: String,
    val infraredUrl: String,
    val xrayUrl: String,
    val opticalDescription: String,
    val infraredDescription: String,
    val xrayDescription: String
)

data class DeepSpaceObject(
    val id: String,
    val name: String,
    val catalogDesignation: String,
    val category: CelestialCategory,
    val rightAscension: String,
    val declination: String,
    val constellation: String,
    val spectralClassification: String,
    val apparentMagnitude: Double,
    val distanceLightYears: Double,
    val redshiftZ: Double?,
    val physicalProcessesDescription: String,
    val imagery: MultiWavelengthData,
    val simbadUrl: String
)

@Serializable
data class ApodRecord(
    val date: String,
    val title: String,
    val explanation: String,
    val url: String,
    val hdUrl: String? = null,
    val mediaType: String = "image",
    val copyright: String? = null,
    val isBookmarked: Boolean = false
)

enum class SolarWavelength(
    val channelName: String,
    val angstroms: Int,
    val primaryIon: String,
    val targetRegion: String,
    val temperatureRange: String,
    val sourceId: Int // Helioviewer source identifier
) {
    AIA_171("AIA 171 Å", 171, "Fe IX", "Quiet Corona & Coronal Loops", "1,000,000 K", 10),
    AIA_193("AIA 193 Å", 193, "Fe XII, XXIV", "Corona & Hot Flare Plasma", "1,250,000 - 20,000,000 K", 11),
    AIA_211("AIA 211 Å", 211, "Fe XIV", "Active Regions & Coronal Loops", "2,000,000 K", 12),
    AIA_304("AIA 304 Å", 304, "He II", "Chromosphere & Transition Region", "50,000 K", 13),
    AIA_131("AIA 131 Å", 131, "Fe VIII, XXI", "Solar Flares & Active Regions", "10,000,000 K", 9),
    HMI_MAGNETOGRAM("HMI Mag", 6173, "Fe I", "Photospheric Magnetic Polarity", "Photosphere", 19)
}

data class SpaceWeatherTelemetry(
    val timestamp: String,
    val solarWindSpeedKmS: Double,
    val solarWindDensityProtonsCm3: Double,
    val interplanetaryMagneticFieldBt: Double,
    val geomagneticKpIndex: Double, // 0.0 to 9.0
    val stormCategory: String, // "G0 Quiet", "G1 Minor", "G2 Moderate", "G3 Strong", "G4 Severe", "G5 Extreme"
    val solarFlareStatus: String, // e.g. "C1.2 Background", "M3.4 Medium Flare", "X2.1 Major Blast"
    val sunspotNumber: Int
)

data class NearEarthObject(
    val id: String,
    val name: String,
    val closeApproachDate: String,
    val missDistanceAu: Double,
    val missDistanceKm: Double,
    val relativeVelocityKmS: Double,
    val estimatedDiameterMetersMin: Double,
    val estimatedDiameterMetersMax: Double,
    val isPotentiallyHazardous: Boolean,
    val orbitClass: String
)
