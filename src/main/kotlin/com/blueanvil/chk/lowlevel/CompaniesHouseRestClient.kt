package com.blueanvil.chk.lowlevel

import com.beust.klaxon.JsonObject
import io.github.bucket4j.Bandwidth
import io.github.bucket4j.BlockingBucket
import io.github.bucket4j.Bucket4j
import java.time.Duration

/**
 * @author Cosmin Marginean
 */
class CompaniesHouseRestClient(private val apiKey: String,
                               private val bucket: BlockingBucket = defaultBucket()) {

    fun request(resource: String): ApiRequest {
        return ApiRequest(apiKey, bucket, resource)
    }

    fun allResults(resource: String, itemsPerPage: Int = RESULTS_PER_PAGE): Sequence<JsonObject> {
        return allResults { startIndex ->
            val appendChar = if (resource.contains("?")) "&" else "?"
            val response = request(resource + "${appendChar}items_per_page=${itemsPerPage}&start_index=${startIndex}").get()
            PagedResponse(response)
        }
    }

    private fun allResults(fetchPage: (Int) -> PagedResponse): Sequence<JsonObject> {
        val firstResponse = fetchPage(0)
        if (firstResponse.totalResults == 0) {
            return emptySequence()
        }

        var page = firstResponse.items
        var currentIndex = 0
        return generateSequence {
            if (currentIndex == firstResponse.totalResults) {
                null
            } else {
                val currentElement = page[currentIndex % ChJson.RESULTS_PER_PAGE]
                currentIndex++
                if (currentIndex < firstResponse.totalResults && currentIndex % ChJson.RESULTS_PER_PAGE == 0) {
                    page = fetchPage(currentIndex).items
                }

                currentElement
            }
        }
    }

    companion object {
        const val RESULTS_PER_PAGE = 50

        private fun defaultBucket(): BlockingBucket {
            val limit = Bandwidth.simple(240, Duration.ofMinutes(2))
            return Bucket4j.builder().addLimit(limit).build().asScheduler()
        }
    }
}
