package com.blueanvil.chk

import org.testng.Assert.assertTrue
import org.testng.annotations.Test

/**
 * @author Cosmin Marginean
 */
class CompaniesHouseClientTest {

    private val apiKey = System.getenv("chTestApiKey")
    private val client = CompaniesHouseClient(apiKey)

    @Test
    fun search() {
        assertTrue(client.searchCompanies("panama").count() > 130)
        assertTrue(client.searchOfficers("marginean").count() > 70)
    }

    @Test
    fun companyProfile() {
        println(client.companyProfile("11697283").toJsonString(true))
    }

    @Test
    fun companyOfficers() {
        assertTrue(client.officers("02391726").count() >= 3)
    }

    @Test
    fun officerAppointments() {
        assertTrue(client.appointments("tZDPlH3KiSJmoRHNwFx0OKsLF64").count() >= 90)
    }
}
