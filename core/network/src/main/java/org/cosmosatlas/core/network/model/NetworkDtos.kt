package org.cosmosatlas.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO for NASA Science WordPress APOD endpoint:
 * https://science.nasa.gov/wp-json/wp/v2/apod-basic
 */
@Serializable
data class WpApodDto(
    val id: Long? = null,
    val date: String? = null,
    val link: String? = null,
    val title: WpRendered? = null,
    val content: WpRendered? = null,
    @SerialName("featured_media_url") val featuredMediaUrl: String? = null,
    @SerialName("apod_date") val apodDate: String? = null,
    @SerialName("apod_explanation") val apodExplanation: String? = null,
    @SerialName("apod_media_type") val apodMediaType: String? = null,
    @SerialName("apod_hd_url") val apodHdUrl: String? = null,
    @SerialName("apod_copyright") val apodCopyright: String? = null
)

@Serializable
data class WpRendered(
    val rendered: String? = null
)

/**
 * Standard NASA Open API APOD fallback DTO:
 * https://api.nasa.gov/planetary/apod
 */
@Serializable
data class NasaApodFallbackDto(
    val date: String? = null,
    val title: String? = null,
    val explanation: String? = null,
    val url: String? = null,
    @SerialName("hdurl") val hdUrl: String? = null,
    @SerialName("media_type") val mediaType: String? = "image",
    val copyright: String? = null
)

/**
 * NASA Image and Video Library DTOs:
 * https://images-api.nasa.gov/search
 */
@Serializable
data class NasaImageSearchResponse(
    val collection: NasaImageCollection? = null
)

@Serializable
data class NasaImageCollection(
    val items: List<NasaImageItem>? = null
)

@Serializable
data class NasaImageItem(
    val href: String? = null,
    val data: List<NasaImageData>? = null,
    val links: List<NasaImageLink>? = null
)

@Serializable
data class NasaImageData(
    val title: String? = null,
    val description: String? = null,
    @SerialName("nasa_id") val nasaId: String? = null,
    @SerialName("date_created") val dateCreated: String? = null,
    val center: String? = null,
    val keywords: List<String>? = null,
    @SerialName("secondary_creator") val secondaryCreator: String? = null
)

@Serializable
data class NasaImageLink(
    val href: String? = null,
    val rel: String? = null,
    val render: String? = null
)

/**
 * NASA SSD / CNEOS Close Approach DTO:
 * https://ssd-api.jpl.nasa.gov/cad.api
 */
@Serializable
data class CneosCadResponse(
    val signature: CneosSignature? = null,
    val count: String? = null,
    val fields: List<String>? = null,
    val data: List<List<String>>? = null
)

@Serializable
data class CneosSignature(
    val source: String? = null,
    val version: String? = null
)

/**
 * NASA Exoplanet Archive TAP Query Row
 */
@Serializable
data class ExoplanetTapRow(
    @SerialName("pl_name") val plName: String? = null,
    @SerialName("hostname") val hostname: String? = null,
    @SerialName("discoverymethod") val discoveryMethod: String? = null,
    @SerialName("disc_year") val discYear: Int? = null,
    @SerialName("pl_orbper") val plOrbPer: Double? = null,
    @SerialName("pl_rade") val plRadE: Double? = null,
    @SerialName("pl_bmasse") val plMassE: Double? = null,
    @SerialName("pl_eqt") val plEqT: Double? = null,
    @SerialName("st_spectype") val stSpecType: String? = null,
    @SerialName("st_teff") val stTeff: Double? = null,
    @SerialName("st_rad") val stRad: Double? = null,
    @SerialName("st_mass") val stMass: Double? = null,
    @SerialName("sy_dist") val syDistPc: Double? = null
)
