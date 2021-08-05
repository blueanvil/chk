package com.blueanvil.chk.lowlevel

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
        assertTrue(client.allResults("/search/companies?q=powders").count() > 60)
        assertTrue(client.allResults("/officers/tZDPlH3KiSJmoRHNwFx0OKsLF64/appointments").count() >= 90)
    }
}
