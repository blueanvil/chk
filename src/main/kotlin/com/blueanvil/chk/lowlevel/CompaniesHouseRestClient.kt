package com.blueanvil.chk.lowlevel

import com.beust.klaxon.JsonObject
import com.blueanvil.chk.checkOk
import io.github.bucket4j.Bandwidth
import io.github.bucket4j.BlockingBucket
import io.github.bucket4j.Bucket4j
import java.time.Duration
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

/**
 * @author Cosmin Marginean
 */
class CompaniesHouseRestClient(private val apiKey: String,
                               private val bucket: BlockingBucket = defaultBucket()) {

    fun request(resource: String): ApiRequest {
        return ApiRequest(apiKey, bucket, resource)
    }

    fun allResults(pagedResource: String): SequenceResult {
        return allResults { startIndex ->
            pagedGet(pagedResource, startIndex)
        }
    }

    fun allResults(pagedResource: String,
                   threadPool: ExecutorService,
                   handleItems: (List<JsonObject>) -> Unit) {
        allResultsParallel(threadPool, handleItems) { startIndex ->
            pagedGet(pagedResource, startIndex)
        }
    }

    fun allResults(pagedResource: String,
                   threads: Int,
                   handleItems: (List<JsonObject>) -> Unit) {
        val threadPool = Executors.newFixedThreadPool(threads)
        allResults(pagedResource, threadPool, handleItems)
        threadPool.shutdown()
        threadPool.awaitTermination(1, TimeUnit.MINUTES)
    }

    private fun pagedGet(pagedResource: String, startIndex: Int): PagedResponse {
        val appendChar = if (pagedResource.contains("?")) "&" else "?"
        val resource = pagedResource + "${appendChar}items_per_page=${RESULTS_PER_PAGE}&start_index=${startIndex}"
        val response = request(resource)
                .get()
                .checkOk()
        return PagedResponse(response)
    }

    private fun allResults(fetchPage: (Int) -> PagedResponse): SequenceResult {
        val firstResponse = fetchPage(0)
        val totalResults = firstResponse.totalResults.coerceAtMost(MAX_RESULTS)
        if (totalResults == 0) {
            return SequenceResult(firstResponse.jsonResponse, emptySequence())
        }

        var page = firstResponse.items
        var currentIndex = 0
        val sequence = generateSequence {
            if (currentIndex == totalResults) {
                null
            } else {
                val currentElement = page[currentIndex % RESULTS_PER_PAGE]
                currentIndex++
                if (currentIndex < totalResults && currentIndex % RESULTS_PER_PAGE == 0) {
                    page = fetchPage(currentIndex).items
                }

                currentElement
            }
        }
        return SequenceResult(firstResponse.jsonResponse, sequence)
    }

    private fun allResultsParallel(threadPool: ExecutorService,
                                   handleItems: (List<JsonObject>) -> Unit,
                                   fetchPage: (Int) -> PagedResponse) {

        val futures = mutableListOf<Future<*>>()
        val firstResponse = fetchPage(0)
        if (firstResponse.totalResults > 0) {
            threadPool.submit { handleItems(firstResponse.items) }
            val pageCount = firstResponse.totalResults.coerceAtMost(MAX_RESULTS) / RESULTS_PER_PAGE + 1
            (1 until pageCount).map { pageIndex ->
                val future = threadPool.submit {
                    val items = fetchPage(pageIndex * RESULTS_PER_PAGE).items
                    handleItems(items)
                }
                futures.add(future)
            }
        }
        futures.forEach { it.get() }
    }

    companion object {
        const val RESULTS_PER_PAGE = 50
        const val MAX_RESULTS = 1000

        internal fun defaultBucket(): BlockingBucket {
            // See https://developer.company-information.service.gov.uk/developer-guidelines
            val limit = Bandwidth.simple(590, Duration.ofMinutes(5))
            return Bucket4j.builder().addLimit(limit).build().asScheduler()
        }
    }
}
