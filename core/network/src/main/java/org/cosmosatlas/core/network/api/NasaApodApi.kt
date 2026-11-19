package org.cosmosatlas.core.network.api

import org.cosmosatlas.core.network.model.NasaApodFallbackDto
import org.cosmosatlas.core.network.model.WpApodDto
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface NasaApodApi {

    /**
     * Updated NASA Science WordPress JSON endpoint:
     * https://science.nasa.gov/wp-json/wp/v2/apod-basic
     */
    @GET
    suspend fun getScienceApodList(
        @Url url: String = "https://science.nasa.gov/wp-json/wp/v2/apod-basic?per_page=1"
    ): List<WpApodDto>

    /**
     * Fallback standard open NASA APOD endpoint:
     * https://api.nasa.gov/planetary/apod
     */
    @GET("planetary/apod")
    suspend fun getStandardApod(
        @Query("api_key") apiKey: String,
        @Query("date") date: String? = null,
        @Query("thumbs") thumbs: Boolean = true
    ): NasaApodFallbackDto

    /**
     * Retrieve range of APOD entries from fallback
     */
    @GET("planetary/apod")
    suspend fun getStandardApodRange(
        @Query("api_key") apiKey: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): List<NasaApodFallbackDto>
}
