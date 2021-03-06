package com.blueanvil.chk.model

import com.beust.klaxon.JsonObject
import com.blueanvil.chk.*
import java.time.LocalDate

data class Officer(val name: String,
                   val officerId: String?,
                   val role: String,
                   val dateOfBirth: ChkPartialDate?,
                   val address: Address = Address(),
                   val appointedOn: LocalDate?,
                   val resignedOn: LocalDate?,
                   val occupation: String?,
                   val nationality: String?,
                   val countryOfResidence: String?,

                   val corporate: Boolean,
                   val companyNumber: String?,
                   val companyName: String?,
                   val companyStatus: String?,
                   val officerCompanyNumber: String?) {

    constructor(json: JsonObject, officerId: String? = null) : this(
            name = json.string(ChJson.NAME)!!,
            officerId = json.officerId() ?: officerId,
            role = json.string(ChJson.OFFICER_ROLE)!!,
            dateOfBirth = ChkPartialDate.fromField(json[ChJson.DATE_OF_BIRTH]),
            address = Address.fromRecord(json)!!,

            appointedOn = if (json.string(ChJson.APPT_ON) != null) {
                json.string(ChJson.APPT_ON)!!.toLocalDate()
            } else {
                json.string(ChJson.APPT_BEFORE).toLocalDate()
            },

            occupation = json.string(ChJson.OCCUPATION),
            nationality = json.string(ChJson.NATIONALITY),
            countryOfResidence = json.string(ChJson.COUNTRY_OF_RESIDENCE),
            resignedOn = json.string(ChJson.RESIGNED_ON).toLocalDate(),


            corporate = json.string(ChJson.OFFICER_ROLE)!!.contains(ChJson.CORPORATE),

            companyNumber = json.obj(ChJson.APPT_TO)?.string(ChJson.COMPANY_NUMBER).fixCompanyNumber(),
            companyName = json.obj(ChJson.APPT_TO)?.string(ChJson.COMPANY_NAME),
            companyStatus = json.obj(ChJson.APPT_TO)?.string(ChJson.COMPANY_STATUS),
            officerCompanyNumber = json.officerRegNo()
    )

    val resigned = resignedOn != null
    val roleLabel = officerRoles.mappings[role]
    val cleanName = if (corporate) {
        name
    } else {
        val comma = name.indexOf(",")
        if (comma > 0) {
            name.substring(comma + 1).trim() + " " + name.substring(0, comma).trim().lowercase().capitalize()
        } else {
            name
        }
    }

    companion object {
        private val officerRoles = CompaniesHouseEnums.getEnumeration("constants.yml").sections["officer_role"]!!
    }
}

data class Appointments(val name: String?,
                        val dateOfBirth: ChkPartialDate?,
                        val appointments: Sequence<Officer>)
