package com.blueanvil.chk.lowlevel

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import java.io.StringReader

/**
 * @author Cosmin Marginean
 */
object ChJson {

    private val jsonParser = Parser.default()

    const val RESULTS_PER_PAGE = 50

    const val TOTAL_RESULTS = "total_results"
    const val START_INDEX = "start_index"
    const val ITEMS = "items"
    const val COMPANY = "company"
    const val COMPANY_NUMBER = "company_number"
    const val COMPANY_STATUS = "company_status"
    const val TITLE = "title"
    const val NAME = "name"
    const val COMPANY_NAME = "company_name"

    val nameFields = listOf(NAME, TITLE, COMPANY_NAME)

    const val ADDRESS = "address"
    const val ADDRESS_SNIPPET = "address_snippet"
    const val APPT_COUNT = "appointment_count"
    const val APPOINTMENTS = "appointments"
    const val APPT_TO = "appointed_to"
    const val APPT_ON = "appointed_on"
    const val APPT_BEFORE = "appointed_before"
    const val OCCUPATION = "occupation"
    const val NATIONALITY = "nationality"
    const val RESIGNED_ON = "resigned_on"

    const val DOB = "date_of_birth"
    const val MONTH = "month"
    const val YEAR = "year"

    const val OFFICER_ROLE = "officer_role"

    const val LINKS = "links"
    const val SELF = "self"
    const val OFFICER = "officer"
    const val IDENTIFICATION = "identification"
    const val REG_NO = "registration_number"

    private val REGEX_OFFICER_LINK = "/officers/(.*)/appointments".toRegex()
    private val REGEX_COMPANY = "/company/(.*)".toRegex()

    fun name(jsonRecord: JsonObject): String {
        return nameFields.map { jsonRecord.string(it) }.first { it != null }!!
    }

    fun officerRegNo(apptRecord: JsonObject): String? {
        return apptRecord.obj(IDENTIFICATION)?.string(REG_NO)
    }

    fun officerId(searchResult: JsonObject): String? {
        val selfLink = searchResult.obj(LINKS)?.string(SELF)
        val officerAppts = searchResult.obj(LINKS)?.obj(OFFICER)?.string(APPOINTMENTS)

        val finalLink = selfLink ?: officerAppts
        if (finalLink != null && finalLink.matches(REGEX_OFFICER_LINK)) {
            return finalLink.replace(REGEX_OFFICER_LINK, "$1")
        }

        return null
    }
}
