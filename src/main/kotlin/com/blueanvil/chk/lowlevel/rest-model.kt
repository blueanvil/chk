package com.blueanvil.chk.lowlevel

import com.beust.klaxon.JsonObject
import com.blueanvil.chk.*
import com.blueanvil.chk.get
import com.blueanvil.chk.httpClient
import com.blueanvil.chk.klaxonJsonParser
import io.github.bucket4j.BlockingBucket
import okhttp3.Response
import org.slf4j.LoggerFactory
import java.io.StringReader

/**
 * @author Cosmin Marginean
 */
internal val REGEX_OFFICER_LINK = "/officers/(.*)/appointments".toRegex()

data class ApiRequest(val apiKey: String,
                      val bucket: BlockingBucket,
                      val resource: String) {

    private val headers = mapOf("Authorization" to "$apiKey")

    fun get(vararg successCodes: Int): Response {
        bucket.consume(1)
        val url = "$REST_ENDPOINT${resource.removePrefix("/")}"
        val response = httpClient().get(url, headers)
        if (successCodes.isNotEmpty() && !successCodes.contains(response.code)) {
            val message = "Error on Companies House API request $url. Status code is ${response.code} and response body is ${response.text()}"
            log.error(message)
            throw RuntimeException(message)
        }
        return response
    }

    companion object {
        private const val REST_ENDPOINT = "https://api.company-information.service.gov.uk/"
        private val log = LoggerFactory.getLogger(ApiRequest::class.java)
    }
}

class PagedResponse(response: Response,
                    val jsonResponse: JsonObject = klaxonJsonParser.parse(StringReader(response.text())) as JsonObject,
                    val totalResults: Int = jsonResponse.int(ChJson.TOTAL_RESULTS)!!,
                    val startIndex: Int = jsonResponse.int(ChJson.START_INDEX)!!) {

    val items: List<JsonObject> = jsonResponse.array(ChJson.ITEMS)!!
}
