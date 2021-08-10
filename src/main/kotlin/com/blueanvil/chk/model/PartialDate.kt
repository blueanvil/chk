package com.blueanvil.chk.model

import com.beust.klaxon.JsonObject
import com.blueanvil.chk.ChJson
import com.blueanvil.chk.DATE_FMT_DASH_YMD
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoField
import java.time.temporal.TemporalAccessor

data class PartialDate(val dayOfMonth: Int?,
                       val month: Int,
                       val year: Int) {

    constructor(temporalAccessor: TemporalAccessor) : this(
            if (temporalAccessor.isSupported(ChronoField.DAY_OF_MONTH)) temporalAccessor.getLong(ChronoField.DAY_OF_MONTH).toInt() else null,
            temporalAccessor.getLong(ChronoField.MONTH_OF_YEAR).toInt(),
            temporalAccessor.getLong(ChronoField.YEAR).toInt())

    companion object {
        fun fromField(value: Any?): PartialDate? {
            if (value == null) {
                return null
            }

            return if (value is String) {
                try {
                    PartialDate(DATE_FMT_DASH_YMD.parse(value.replace("T.*".toRegex(), "")))
                } catch (e: DateTimeParseException) {
                    // Because the field value is sometimes "Unknown" or some other crap
                    return null
                }
            } else {
                val date = value as JsonObject
                PartialDate(null, date.int(ChJson.MONTH)!!, date.int(ChJson.YEAR)!!)
            }
        }
    }
}
