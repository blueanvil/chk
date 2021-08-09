package com.blueanvil.chk.lowlevel

import com.beust.klaxon.JsonObject
import com.blueanvil.chk.*
import io.github.bucket4j.BlockingBucket
import okhttp3.Response
import java.io.StringReader

/**
 * @author Cosmin Marginean
 */
data class ApiRequest(val apiKey: String,
                      val bucket: BlockingBucket,
                      val resource: String) {

    private val headers = mapOf("Authorization" to "$apiKey")

    fun get(): Response {
        bucket.consume(1)
        val url = "$REST_ENDPOINT${resource.removePrefix("/")}"
        return httpClient().get(url, headers)
    }

    companion object {
        private const val REST_ENDPOINT = "https://api.company-information.service.gov.uk/"
    }
}

class PagedResponse(response: Response,
                    val jsonResponse: JsonObject = klaxonJsonParser.parse(StringReader(response.text())) as JsonObject,
                    val totalResults: Int = jsonResponse.int(ChJson.TOTAL_RESULTS)!!,
                    val startIndex: Int = jsonResponse.int(ChJson.START_INDEX)!!) {

    val items: List<JsonObject> = jsonResponse.array(ChJson.ITEMS)!!
}
