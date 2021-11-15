package com.blueanvil.chk.model

import com.beust.klaxon.JsonObject
import com.blueanvil.chk.*

/**
 * @author Cosmin Marginean
 */
data class CompanyInfo(val name: String,
                       val companyNumber: String,
                       val companyStatus: String?,
                       val officerId: String?,
                       val numberOfAppointments: Int,
                       val address: Address?,
                       val dateOfCreation: ChkPartialDate?,
                       val dateOfCessation: ChkPartialDate?) {

    constructor(record: JsonObject) : this(
            name = record.name(),
            companyNumber = record.string(ChJson.COMPANY_NUMBER)?.fixCompanyNumber() ?: ChkConst.COMPANY_NUMBER_UNKNOWN,
            companyStatus = record.string(ChJson.COMPANY_STATUS),
            officerId = record.officerId(),
            numberOfAppointments = record.int(ChJson.APPT_COUNT) ?: 0,
            address = Address.fromRecord(record),
            dateOfCreation = ChkPartialDate.fromField(record[ChJson.DATE_OF_CREATION]),
            dateOfCessation = ChkPartialDate.fromField(record[ChJson.DATE_OF_CESSATION])
    )
}

