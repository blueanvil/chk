package com.blueanvil.chk

/**
 * @author Cosmin Marginean
 */
object ChJson {

    const val TOTAL_RESULTS = "total_results"
    const val START_INDEX = "start_index"
    const val ITEMS = "items"
    const val COMPANY = "company"
    const val COMPANY_NUMBER = "company_number"
    const val COMPANY_STATUS = "company_status"
    const val TITLE = "title"
    const val NAME = "name"
    const val COMPANY_NAME = "company_name"
    const val DATE_OF_CREATION = "date_of_creation"
    const val DATE_OF_CESSATION = "date_of_cessation"
    const val CORPORATE = "corporate"

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
}
