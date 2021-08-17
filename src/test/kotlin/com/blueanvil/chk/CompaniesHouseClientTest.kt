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
        assertTrue(client.searchDisqualifiedOfficers("smith").count() > 70)
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
        assertTrue(client.appointments("tZDPlH3KiSJmoRHNwFx0OKsLF64").appointments.count() >= 90)
    }

    @Test
    fun filingHistory() {
        assertTrue(client.filingHistory("03020647").count() > 7)
    }

    fun usageExample() {
        val officerId = ""

        val client = CompaniesHouseClient(apiKey)
        client.searchCompanies("banana")
                .forEach { company ->
                    println("${company.name}/${company.companyNumber}")
                }

        client.appointments(officerId)
                .appointments
                .forEach { appointment ->
                    println(appointment.companyName)
                }
    }
}
