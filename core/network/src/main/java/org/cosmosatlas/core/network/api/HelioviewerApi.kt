package org.cosmosatlas.core.network.api

import retrofit2.http.GET
import retrofit2.http.Query

interface HelioviewerApi {

    /**
     * Helioviewer REST API:
     * https://api.helioviewer.org/v2/getClosestImage/
     * Returns JSON with image URL or metadata
     */
    @GET("v2/getClosestImage/")
    suspend fun getClosestImage(
        @Query("date") date: String,          // ISO-8601 e.g. "2026-11-19T10:00:00.000Z"
        @Query("sourceId") sourceId: Int       // e.g. 10 (AIA 171), 11 (AIA 193), 13 (AIA 304)
    ): String

    /**
     * Quick URL builder helper for direct Helioviewer SDO snapshot
     */
    companion object {
        fun buildDirectScreenshotUrl(
            dateIso: String,
            sourceId: Int,
            imageScale: Double = 2.42
        ): String {
            return "https://api.helioviewer.org/v2/takeScreenshot/?date=$dateIso&imageScale=$imageScale&layers=[$sourceId,1,100]&events=&eventLabels=false"
        }
    }
}
