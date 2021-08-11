package com.blueanvil.chk

import org.testng.Assert.assertEquals
import org.testng.Assert.assertTrue
import org.testng.annotations.Test

/**
 * @author Cosmin Marginean
 */
class CompaniesHouseEnumsTest {

    @Test
    fun readEnum() {
        val enum = CompaniesHouseEnums.getEnumeration("disqualified_officer_descriptions.yml")!!
        assertEquals(enum.name, "disqualified_officer_descriptions.yml")
        assertEquals(enum.sections["description_identifier"]!!.name, "description_identifier")
        assertTrue(enum.sections["description_identifier"]!!.mappings.size > 20)
        assertEquals(enum.sections["description_identifier"]!!.mappings["conviction-of-indictable-offence"], "Disqualification on conviction of indictable offence")
    }

    @Test(expectedExceptions = [IllegalArgumentException::class])
    fun notFound() {
        CompaniesHouseEnums.getEnumeration("the pattern is the pattern")
    }
}
