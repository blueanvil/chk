package com.blueanvil.chk.model

import com.beust.klaxon.JsonObject
import com.blueanvil.chk.ChJson
import com.blueanvil.chk.disqualifiedOfficerId
import com.blueanvil.chk.name
import com.blueanvil.chk.officerId

data class OfficerInfo(val name: String,
                       val officerId: String,
                       val numberOfAppointments: Int,
                       val dateOfBirth: PartialDate?) {

    constructor(record: JsonObject) : this(
            name = record.name(),
            officerId = record.officerId()!!,
            numberOfAppointments = record.int(ChJson.APPT_COUNT) ?: 0,
            dateOfBirth = PartialDate.fromField(record[ChJson.DATE_OF_BIRTH]))

}

data class DisqualifiedOfficerInfo(val name: String,
                                   val officerId: String,
                                   val dateOfBirth: PartialDate?) {

    constructor(record: JsonObject) : this(
            name = record.name(),
            officerId = record.disqualifiedOfficerId()!!,
            dateOfBirth = PartialDate.fromField(record[ChJson.DATE_OF_BIRTH]))

}
