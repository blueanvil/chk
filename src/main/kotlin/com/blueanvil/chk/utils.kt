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
import java.time.temporal.ChronoUnit

/**
 * @author Cosmin Marginean
 */
internal val klaxonJsonParser = Parser.default()

internal fun String.tidySpaces() = replace("\\s+".toRegex(), " ").trim()
internal fun String.utf8UrlEncode() = URLEncoder.encode(this, StandardCharsets.UTF_8.name())

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

