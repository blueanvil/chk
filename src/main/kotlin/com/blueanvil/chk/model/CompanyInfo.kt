package com.blueanvil.chk.model

import com.beust.klaxon.JsonObject
import com.blueanvil.chk.ChJson
import com.blueanvil.chk.fixCompanyNumber
import com.blueanvil.chk.name
import com.blueanvil.chk.officerId

/**
 * @author Cosmin Marginean
 */
data class CompanyInfo(val name: String,
                       val companyNumber: String?,
                       val companyStatus: String?,
                       val officerId: String?,
                       val numberOfAppointments: Int,
                       val address: Address?) {

    constructor(record: JsonObject) : this(
            name = record.name(),
            companyNumber = record.string(ChJson.COMPANY_NUMBER).fixCompanyNumber(),
            companyStatus = record.string(ChJson.COMPANY_STATUS),
            officerId = record.officerId(),
            numberOfAppointments = record.int(ChJson.APPT_COUNT) ?: 0,
            address = Address.fromRecord(record))
}

