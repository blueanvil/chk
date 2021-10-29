package com.blueanvil.chk

import com.beust.klaxon.JsonObject

/**
 * @author Cosmin Marginean
 */
internal val REGEX_OFFICER_LINK = "/officers/(.*)/appointments".toRegex()
internal val REGEX_DISQUALIFIED_OFFICER_LINK = "/disqualified-officers/natural/(.*)".toRegex()
internal const val FILING_FILE_URL_BASE = "https://find-and-update.company-information.service.gov.uk"
private val filingDescEnum =
        CompaniesHouseEnums.getEnumeration("filing_history_descriptions.yml").sections["description"]!!

fun JsonObject.officerId(): String? {
    val selfLink = obj(ChJson.LINKS)?.string(ChJson.SELF)
    if (selfLink != null && selfLink.matches(REGEX_OFFICER_LINK)) {
        return selfLink.replace(REGEX_OFFICER_LINK, "$1")
    }

    val officerAppts = obj(ChJson.LINKS)?.obj(ChJson.OFFICER)?.string(ChJson.APPOINTMENTS)
    if (officerAppts != null && officerAppts.matches(REGEX_OFFICER_LINK)) {
        return officerAppts.replace(REGEX_OFFICER_LINK, "$1")
    }

    return null
}

fun JsonObject.disqualifiedOfficerId(): String? {
    val selfLink = obj(ChJson.LINKS)?.string(ChJson.SELF)
    if (selfLink != null && selfLink.matches(REGEX_DISQUALIFIED_OFFICER_LINK)) {
        return selfLink.replace(REGEX_DISQUALIFIED_OFFICER_LINK, "$1")
    }

    return null
}

fun JsonObject.totalRecords(): Int {
    val key = ChJson.totalFields.firstOrNull() { containsKey(it) }
    return if (key != null) int(key)!! else 0
}

fun JsonObject.filingHistoryDescription(): String {
    val descriptionKey = string("description")!!
    if (!filingDescEnum.mappings.containsKey(descriptionKey)) {
        return obj("description_values")?.string("description") ?: descriptionKey
    }
    val descriptionFmt = filingDescEnum.mappings[descriptionKey]!!
    var description = descriptionFmt.replace("*", "")
    if (containsKey("description_values")) {
        val descValues = obj("description_values")!!
        descValues.keys
                .filter { descValues[it] is String }
                .forEach { description = description.replace("{${it}}", descValues.string(it)!!) }
    }
    return description
}

fun JsonObject.filingHistoryUrl(): String? {
    val relativeUrl = obj("links")?.string("self") ?: return null
    return "$FILING_FILE_URL_BASE$relativeUrl/document?format=pdf&download=0"
}

fun JsonObject.name() = ChJson.nameFields.map { string(it) }.first { it != null }!!
fun JsonObject.officerRegNo() = obj(ChJson.IDENTIFICATION)?.string(ChJson.REG_NO)
