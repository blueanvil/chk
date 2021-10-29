package com.blueanvil.chk.model

import com.beust.klaxon.JsonObject
import com.blueanvil.chk.ChJson
import com.blueanvil.chk.DATE_FMT_DASH_YMD
import java.io.Serializable
import java.text.DecimalFormat
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoField
import java.time.temporal.TemporalAccessor
import java.time.temporal.TemporalField

/**
 * A partial date is a date that has at least a year, but it might not have month or dayOfMonth.
 * @author Cosmin Marginean
 */
data class PartialDate(val dayOfMonth: Int?,
                       val month: Int?,
                       val year: Int) : TemporalAccessor, Serializable, Comparable<PartialDate> {

    constructor(temporalAccessor: TemporalAccessor) : this(
            if (temporalAccessor.isSupported(ChronoField.DAY_OF_MONTH)) temporalAccessor.getLong(ChronoField.DAY_OF_MONTH).toInt() else null,
            temporalAccessor.getLong(ChronoField.MONTH_OF_YEAR).toInt(),
            temporalAccessor.getLong(ChronoField.YEAR_OF_ERA).toInt())

    private val numberFmt = DecimalFormat("00")
    val full: Boolean = dayOfMonth != null && month != null

    init {
        if (dayOfMonth != null && month == null) {
            throw IllegalStateException("A partial date can't have dayOfMonth and not have month ($dayOfMonth/$month/$year")
        }
    }

    override fun isSupported(field: TemporalField): Boolean {
        if (field == ChronoField.DAY_OF_MONTH) {
            return dayOfMonth != null
        }
        if (field == ChronoField.MONTH_OF_YEAR) {
            return month != null
        }
        return field == ChronoField.YEAR_OF_ERA
    }

    override fun getLong(field: TemporalField): Long {
        if (!isSupported(field)) {
            throw IllegalArgumentException("Field not supported $field")
        }
        if (field == ChronoField.DAY_OF_MONTH) {
            return dayOfMonth!!.toLong()
        }
        if (field == ChronoField.MONTH_OF_YEAR) {
            return month!!.toLong()
        }
        return if (field == ChronoField.YEAR_OF_ERA) {
            year.toLong()
        } else 0
    }

    override fun compareTo(d: PartialDate): Int {
        // Check year first, we might not even need to bother with the rest
        if (year < d.year) {
            return -1
        } else if (year > d.year) {
            return 1
        }

        // Year is the same so let's compare months
        val m1 = month ?: 0
        val m2 = d.month ?: 0
        if (m1 != m2) {
            return m1.compareTo(m2)
        }

        // If we got here then months are either a) identical or b) both null
        // If the month is null then the other one is null as well (we cannot have day and not have month)
        if (month == null) {
            return 0
        }

        val d1 = dayOfMonth ?: 0
        val d2 = d.dayOfMonth ?: 0
        return d1.compareTo(d2)
    }

    override fun toString(): String {
        return (if (dayOfMonth != null) "${numberFmt.format(dayOfMonth)}/" else "") +
                (if (month != null) "${numberFmt.format(month)}/" else "") +
                year
    }

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
