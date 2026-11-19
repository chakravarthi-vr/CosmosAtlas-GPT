package org.cosmosatlas.core.network.api

import retrofit2.http.GET
import retrofit2.http.Query

interface JplHorizonsApi {

    /**
     * JPL Horizons On-Line Solar System Data Service
     * https://ssd.jpl.nasa.gov/api/horizons.api
     */
    @GET("api/horizons.api")
    suspend fun queryHorizonsVectors(
        @Query("format") format: String = "text",
        @Query("COMMAND") command: String,          // e.g. "399" (Earth), "499" (Mars), "-98" (Voyager 1)
        @Query("EPHEM_TYPE") ephemType: String = "VECTORS",
        @Query("CENTER") center: String = "@sun",     // Sun-centered (heliocentric)
        @Query("START_TIME") startTime: String,      // e.g. "2026-11-19"
        @Query("STOP_TIME") stopTime: String,        // e.g. "2026-11-20"
        @Query("STEP_SIZE") stepSize: String = "1d",
        @Query("VEC_TABLE") vecTable: String = "3",  // State vectors (position + velocity)
        @Query("REF_SYSTEM") refSystem: String = "ICRF",
        @Query("OUT_UNITS") outUnits: String = "AU-D" // AU and AU/day
    ): String
}
