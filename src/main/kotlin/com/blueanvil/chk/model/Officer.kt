package com.blueanvil.chk.model

import com.beust.klaxon.JsonObject
import com.blueanvil.chk.ChJson
import com.blueanvil.chk.fixCompanyNumber
import com.blueanvil.chk.officerId
import com.blueanvil.chk.officerRegNo

data class Officer(val name: String,
                   val officerId: String?,
                   val role: String,
                   val dateOfBirth: PartialDate?,
                   val address: Address = Address(),
                   val appointed: PartialDate?,
                   val occupation: String?,
                   val nationality: String?,
                   val countryOfResidence: String?,
                   val resignedOn: PartialDate?,

                   val corporate: Boolean,
                   val companyNumber: String?,
                   val companyName: String?,
                   val companyStatus: String?,
                   val officerCompanyNumber: String?) {

    constructor(json: JsonObject, officerId: String? = null) : this(
            name = json.string(ChJson.NAME)!!,
            officerId = json.officerId() ?: officerId,
            role = json.string(ChJson.OFFICER_ROLE)!!,
            dateOfBirth = PartialDate.fromField(json[ChJson.DATE_OF_BIRTH]),
            address = Address.fromRecord(json)!!,

            appointed = if (json.string(ChJson.APPT_ON) != null) {
                PartialDate.fromField(json.string(ChJson.APPT_ON))
            } else {
                PartialDate.fromField(json.string(ChJson.APPT_BEFORE))
            },

            occupation = json.string(ChJson.OCCUPATION),
            nationality = json.string(ChJson.NATIONALITY),
            countryOfResidence = json.string(ChJson.COUNTRY_OF_RESIDENCE),
            resignedOn = PartialDate.fromField(json.string(ChJson.RESIGNED_ON)),


            corporate = json.string(ChJson.OFFICER_ROLE)!!.contains(ChJson.CORPORATE),

            companyNumber = json.obj(ChJson.APPT_TO)?.string(ChJson.COMPANY_NUMBER).fixCompanyNumber(),
            companyName = json.obj(ChJson.APPT_TO)?.string(ChJson.COMPANY_NAME),
            companyStatus = json.obj(ChJson.APPT_TO)?.string(ChJson.COMPANY_STATUS),
            officerCompanyNumber = json.officerRegNo()
    )
}

data class Appointments(val name: String?,
                        val dateOfBirth: PartialDate?,
                        val appointments: Sequence<Officer>)