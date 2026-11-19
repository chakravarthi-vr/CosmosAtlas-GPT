package org.cosmosatlas.core.network.client

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class RateLimitAndRetryInterceptor(
    private val apiKeyProvider: () -> String,
    private val maxRetries: Int = 3
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        // Inject NASA API Key if querying api.nasa.gov and key isn't already present
        if (request.url.host.contains("api.nasa.gov") && request.url.queryParameter("api_key") == null) {
            val key = apiKeyProvider()
            val newUrl = request.url.newBuilder()
                .addQueryParameter("api_key", key)
                .build()
            request = request.newBuilder().url(newUrl).build()
        }

        var response: Response? = null
        var attempts = 0
        var delayMs = 1000L

        while (attempts < maxRetries) {
            try {
                response?.close()
                response = chain.proceed(request)

                // If Rate Limit hit (HTTP 429 Too Many Requests)
                if (response.code == 429) {
                    attempts++
                    if (attempts < maxRetries) {
                        try {
                            Thread.sleep(delayMs)
                        } catch (e: InterruptedException) {
                            Thread.currentThread().interrupt()
                            throw IOException("Retry interrupted", e)
                        }
                        delayMs *= 2
                        continue
                    }
                }
                return response
            } catch (e: IOException) {
                attempts++
                if (attempts >= maxRetries) throw e
                try {
                    Thread.sleep(delayMs)
                } catch (ie: InterruptedException) {
                    Thread.currentThread().interrupt()
                    throw IOException("Retry interrupted", ie)
                }
                delayMs *= 2
            }
        }

        return response ?: throw IOException("Failed to execute request after $maxRetries attempts")
    }
}
