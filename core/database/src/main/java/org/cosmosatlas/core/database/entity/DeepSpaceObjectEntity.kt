package org.cosmosatlas.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.cosmosatlas.core.model.CelestialCategory
import org.cosmosatlas.core.model.DeepSpaceObject
import org.cosmosatlas.core.model.MultiWavelengthData

@Entity(tableName = "deep_space_objects")
data class DeepSpaceObjectEntity(
    @PrimaryKey val id: String,
    val name: String,
    val catalogDesignation: String,
    val category: String,
    val rightAscension: String,
    val declination: String,
    val constellation: String,
    val spectralClassification: String,
    val apparentMagnitude: Double,
    val distanceLightYears: Double,
    val redshiftZ: Double?,
    val physicalProcessesDescription: String,
    val opticalUrl: String,
    val infraredUrl: String,
    val xrayUrl: String,
    val opticalDescription: String,
    val infraredDescription: String,
    val xrayDescription: String,
    val simbadUrl: String
) {
    fun toDomain(): DeepSpaceObject = DeepSpaceObject(
        id = id,
        name = name,
        catalogDesignation = catalogDesignation,
        category = CelestialCategory.valueOf(category),
        rightAscension = rightAscension,
        declination = declination,
        constellation = constellation,
        spectralClassification = spectralClassification,
        apparentMagnitude = apparentMagnitude,
        distanceLightYears = distanceLightYears,
        redshiftZ = redshiftZ,
        physicalProcessesDescription = physicalProcessesDescription,
        imagery = MultiWavelengthData(
            opticalUrl = opticalUrl,
            infraredUrl = infraredUrl,
            xrayUrl = xrayUrl,
            opticalDescription = opticalDescription,
            infraredDescription = infraredDescription,
            xrayDescription = xrayDescription
        ),
        simbadUrl = simbadUrl
    )

    companion object {
        fun fromDomain(obj: DeepSpaceObject): DeepSpaceObjectEntity = DeepSpaceObjectEntity(
            id = obj.id,
            name = obj.name,
            catalogDesignation = obj.catalogDesignation,
            category = obj.category.name,
            rightAscension = obj.rightAscension,
            declination = obj.declination,
            constellation = obj.constellation,
            spectralClassification = obj.spectralClassification,
            apparentMagnitude = obj.apparentMagnitude,
            distanceLightYears = obj.distanceLightYears,
            redshiftZ = obj.redshiftZ,
            physicalProcessesDescription = obj.physicalProcessesDescription,
            opticalUrl = obj.imagery.opticalUrl,
            infraredUrl = obj.imagery.infraredUrl,
            xrayUrl = obj.imagery.xrayUrl,
            opticalDescription = obj.imagery.opticalDescription,
            infraredDescription = obj.imagery.infraredDescription,
            xrayDescription = obj.imagery.xrayDescription,
            simbadUrl = obj.simbadUrl
        )
    }
}
