package org.cosmosatlas.core.network.api

import org.cosmosatlas.core.network.model.CneosCadResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaCneosApi {

    /**
     * NASA JPL SSD Close Approach Data (CAD) API
     * https://ssd-api.jpl.nasa.gov/cad.api
     */
    @GET("cad.api")
    suspend fun getCloseApproaches(
        @Query("dist-max") distMaxAu: String = "0.05",
        @Query("sort") sort: String = "dist",
        @Query("limit") limit: Int = 20
    ): CneosCadResponse
}
