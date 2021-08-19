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

data class PagedResponse(val response: Response,
                         val jsonResponse: JsonObject = klaxonJsonParser.parse(StringReader(response.text())) as JsonObject,
                         val totalResults: Int = jsonResponse.totalRecords(),
                         val startIndex: Int = jsonResponse.safeStartIndex()) {

    val items: List<JsonObject> = if (response.code == HTTP_OK) jsonResponse.array(ChJson.ITEMS)!! else emptyList()
}

private fun JsonObject.safeStartIndex(): Int {
    // This exists because of this: https://forum.aws.chdev.org/t/start-index-field-is-sometimes-returned-as-string-instead-of-int/4259
    val value = get(ChJson.START_INDEX)
    return if (value is Number) {
        value.toInt()
    } else {
        (value as String).toInt()
    }
}

data class SequenceResult(val statusCode: Int,
                          val firstResponse: JsonObject,
                          val sequence: Sequence<JsonObject>)
