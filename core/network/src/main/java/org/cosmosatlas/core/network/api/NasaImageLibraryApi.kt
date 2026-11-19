package org.cosmosatlas.core.network.api

import org.cosmosatlas.core.network.model.NasaImageSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaImageLibraryApi {

    /**
     * Search high-resolution imagery from JWST, Hubble, Juno, Cassini, etc.
     * https://images-api.nasa.gov/search
     */
    @GET("search")
    suspend fun searchImagery(
        @Query("q") query: String,
        @Query("media_type") mediaType: String = "image",
        @Query("page_size") pageSize: Int = 20,
        @Query("center") center: String? = null
    ): NasaImageSearchResponse
}
