package org.cosmosatlas.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.cosmosatlas.core.model.NearEarthObject

@Entity(tableName = "near_earth_objects")
data class NearEarthObjectEntity(
    @PrimaryKey val id: String,
    val name: String,
    val closeApproachDate: String,
    val missDistanceAu: Double,
    val missDistanceKm: Double,
    val relativeVelocityKmS: Double,
    val estimatedDiameterMetersMin: Double,
    val estimatedDiameterMetersMax: Double,
    val isPotentiallyHazardous: Boolean,
    val orbitClass: String
) {
    fun toDomain(): NearEarthObject = NearEarthObject(
        id = id,
        name = name,
        closeApproachDate = closeApproachDate,
        missDistanceAu = missDistanceAu,
        missDistanceKm = missDistanceKm,
        relativeVelocityKmS = relativeVelocityKmS,
        estimatedDiameterMetersMin = estimatedDiameterMetersMin,
        estimatedDiameterMetersMax = estimatedDiameterMetersMax,
        isPotentiallyHazardous = isPotentiallyHazardous,
        orbitClass = orbitClass
    )

    companion object {
        fun fromDomain(neo: NearEarthObject): NearEarthObjectEntity = NearEarthObjectEntity(
            id = neo.id,
            name = neo.name,
            closeApproachDate = neo.closeApproachDate,
            missDistanceAu = neo.missDistanceAu,
            missDistanceKm = neo.missDistanceKm,
            relativeVelocityKmS = neo.relativeVelocityKmS,
            estimatedDiameterMetersMin = neo.estimatedDiameterMetersMin,
            estimatedDiameterMetersMax = neo.estimatedDiameterMetersMax,
            isPotentiallyHazardous = neo.isPotentiallyHazardous,
            orbitClass = neo.orbitClass
        )
    }
}
