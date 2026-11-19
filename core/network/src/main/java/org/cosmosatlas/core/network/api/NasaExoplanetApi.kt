package org.cosmosatlas.core.network.api

import org.cosmosatlas.core.network.model.ExoplanetTapRow
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaExoplanetApi {

    /**
     * NASA Exoplanet Archive Table Access Protocol (TAP) API
     * https://exoplanetarchive.ipac.caltech.edu/TAP/sync
     */
    @GET("TAP/sync")
    suspend fun queryConfirmedExoplanets(
        @Query("query") query: String = "select pl_name,hostname,discoverymethod,disc_year,pl_orbper,pl_rade,pl_bmasse,pl_eqt,st_spectype,st_teff,st_rad,st_mass,sy_dist from ps where default_flag=1 order by disc_year desc",
        @Query("format") format: String = "json"
    ): List<ExoplanetTapRow>
}
