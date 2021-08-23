package com.blueanvil.chk.lowlevel

import com.beust.klaxon.JsonObject
import com.blueanvil.chk.*
import io.github.bucket4j.BlockingBucket
import okhttp3.Response
import org.slf4j.LoggerFactory
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

data class PagedResponse(val response: Response) {

    val jsonResponse = jsonResponse()
    val totalResults: Int = jsonResponse?.totalRecords() ?: 0
    val startIndex: Int = jsonResponse?.safeStartIndex() ?: 0

    val items: List<JsonObject>
        get() {
            return if (response.code != HTTP_OK || jsonResponse == null || !jsonResponse.containsKey(ChJson.ITEMS))
                emptyList()
            else
                jsonResponse?.array(ChJson.ITEMS) ?: emptyList()
        }

    private fun jsonResponse(): JsonObject? {
        if (response.code != HTTP_OK) {
            val text = response.text()
            log.error("Error on request ${response.request.url}. Status code is $response.code and response body is $text")
            return null
        }

        return klaxonJsonParser.parse(StringReader(response.text())) as JsonObject
    }

    companion object {
        private val log = LoggerFactory.getLogger(PagedResponse::class.java)
    }
}

private fun JsonObject.safeStartIndex(): Int {
    // This exists because of this: https://forum.aws.chdev.org/t/start-index-field-is-sometimes-returned-as-string-instead-of-int/4259
    val value = get(ChJson.START_INDEX)
    return if (value is Number) {
        value.toInt()
    } else {
        (value as String?)?.toInt() ?: 0
    }
}

data class SequenceResult(val statusCode: Int,
                          val firstResponse: JsonObject,
                          val sequence: Sequence<JsonObject>)
