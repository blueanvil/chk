package com.blueanvil.chk

import com.beust.klaxon.JsonObject
import com.blueanvil.chk.lowlevel.REGEX_OFFICER_LINK

/**
 * @author Cosmin Marginean
 */
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

fun JsonObject.name() = ChJson.nameFields.map { string(it) }.first { it != null }!!
fun JsonObject.officerRegNo() = obj(ChJson.IDENTIFICATION)?.string(ChJson.REG_NO)
