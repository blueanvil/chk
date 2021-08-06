package com.blueanvil.chk.lowlevel

import com.blueanvil.chk.text
import com.hazelcast.map.IMap
import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket4j
import io.github.bucket4j.grid.GridBucketState
import io.github.bucket4j.grid.RecoveryStrategy
import io.github.bucket4j.grid.hazelcast.Hazelcast
import org.testng.Assert.assertEquals
import org.testng.Assert.assertTrue
import org.testng.annotations.Test
import java.time.Duration
import java.util.concurrent.atomic.AtomicLong


/**
 * @author Cosmin Marginean
 */
class CompaniesHouseRestClientTest {

    private val apiKey = System.getProperty("chTestApiKey")
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
        val pagedResource = "/company/02685120/officers"

        val count = client.allResults(pagedResource).count()
        println("Found $count officers with regular fetch")

        val parallelCount = AtomicLong(0)
        client.allResults(pagedResource, 4) { appts ->
            parallelCount.addAndGet(appts.size.toLong())
        }
        println("Found ${parallelCount.get()} officers with parallel fetch")

        assertEquals(count.toLong(), parallelCount.get())
    }

    @Test
    fun clusteredBucket() {
        val map: IMap<String, GridBucketState> = com.hazelcast.core.Hazelcast.newHazelcastInstance().getMap("bucketmap")
        val bandwidth = Bandwidth.simple(590, Duration.ofMinutes(5))
        val bucket = Bucket4j.extension(Hazelcast::class.java)
                .builder()
                .addLimit(bandwidth)
                .build(map, "key", RecoveryStrategy.RECONSTRUCT)
        val clusterClient = CompaniesHouseRestClient(apiKey, bucket.asScheduler())


        val pagedResource = "/company/02685120/officers"
        val parallelCount = AtomicLong(0)
        clusterClient.allResults(pagedResource, 4) { appts ->
            parallelCount.addAndGet(appts.size.toLong())
        }
        assertTrue(parallelCount.get() > 100)
    }
}
