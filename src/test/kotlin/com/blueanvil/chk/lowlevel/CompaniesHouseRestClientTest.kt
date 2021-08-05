package com.blueanvil.chk.lowlevel

import com.blueanvil.chk.text
import org.testng.Assert.assertEquals
import org.testng.Assert.assertTrue
import org.testng.annotations.Test
import java.io.File

/**
 * @author Cosmin Marginean
 */
class CompaniesHouseRestClientTest {

    private val apiKey = File("./companies-house-test-api-key.txt").readText().trim()
    private val client = CompaniesHouseRestClient(apiKey)

    @Test
    fun basicRestFunctions() {
        assertTrue(client.request("/company/02685120").get().text().contains("""persons_with_significant_control_statements":"/company/02685120/persons-with-significant-control-statements""""))
        assertTrue(client.allResults("/search/companies?q=powders").count() > 60)
        assertTrue(client.allResults("/officers/tZDPlH3KiSJmoRHNwFx0OKsLF64/appointments").count() >= 90)
        assertEquals(client.request("/company/1234").get().code, 404)
    }
}
