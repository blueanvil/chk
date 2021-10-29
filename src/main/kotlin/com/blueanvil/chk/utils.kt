package com.blueanvil.chk

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import okhttp3.Headers.Companion.toHeaders
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.slf4j.LoggerFactory
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * @author Cosmin Marginean
 */
private val httpLog = LoggerFactory.getLogger(OkHttpClient::class.java)
internal val klaxonJsonParser = Parser.default()
internal val DATE_FMT_DASH_YMD = DateTimeFormatter.ofPattern("yyyy-MM-dd")

internal val HTTP_OK = 200
internal val HTTP_NOT_FOUND = 404

internal fun String.tidySpaces() = replace("\\s+".toRegex(), " ").trim()
internal fun String.utf8UrlEncode() = URLEncoder.encode(this, StandardCharsets.UTF_8.name())

internal fun String?.toLocalDate(): LocalDate? {
    if (this == null) {
        return null
    }
    return LocalDate.from(DATE_FMT_DASH_YMD.parse(this))
}

internal fun httpClient() = OkHttpClient().newBuilder()
        .connectTimeout(Duration.of(120, ChronoUnit.SECONDS))
        .readTimeout(Duration.of(120, ChronoUnit.SECONDS))
        .build()

internal fun OkHttpClient.get(url: String, headers: Map<String, String> = emptyMap()): Response {
    val request = Request.Builder()
            .url(url)
            .get()
            .headers(headers.toHeaders())
            .build()
    return newCall(request).execute()
}

fun Response.text(): String {
    val string = this.body!!.string()
    this.body!!.close()
    return string
}

fun Response.json(): JsonObject {
    return klaxonJsonParser.parse(StringBuilder(text())) as JsonObject
}

fun Response.checkOk(): Response {
    return checkStatus(HTTP_OK)
}

fun Response.checkStatus(vararg successCodes: Int): Response {
    if (successCodes.isNotEmpty() && !successCodes.contains(code)) {
        val message = "Error on request ${request.url}. Status code is $code and response body is ${text()}"
        httpLog.error(message)
        throw RuntimeException(message)
    }
    return this
}

//Because CH sometimes returns '2538098' and sometimes '02538098' for a company
fun String?.fixCompanyNumber(): String? {
    return this?.padStart(8, '0')
}
