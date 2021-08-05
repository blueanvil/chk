package com.blueanvil.chk.lowlevel

import com.blueanvil.chk.text
import org.testng.Assert.assertEquals
import org.testng.Assert.assertTrue
import org.testng.annotations.Test
import java.io.File
import java.util.concurrent.atomic.AtomicLong

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

    @Test
    fun parallelism() {
        val companyNumber = "02685120"
        val count = client.allResults("/company/$companyNumber/officers").count()
        println("Found $count officers with regular fetch")
        val parallelCount = AtomicLong(0)
        client.allResults("/company/$companyNumber/officers", 4) { appts ->
            appts.forEach { parallelCount.incrementAndGet() }
        }
        println("Found ${parallelCount.get()} officers with parallel fetch")
        assertEquals(count.toLong(), parallelCount.get())
    }
}
