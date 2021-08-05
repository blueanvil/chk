package com.blueanvil.chk.lowlevel

import org.testng.Assert.assertTrue
import org.testng.annotations.Test

/**
 * @author Cosmin Marginean
 */
class CompaniesHouseRestClientTest {

    private val apiKey = System.getProperty("chkTestKey")

    @Test
    fun searchSequence() {
        assertTrue(CompaniesHouseRestClient(apiKey).allResults("/search/companies?q=powders").count() > 60)
    }
}
