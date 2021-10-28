package com.blueanvil.chk.lowlevel

import com.blueanvil.chk.model.Address
import org.testng.Assert.assertEquals
import org.testng.annotations.Test

/**
 * @author Cosmin Marginean
 */
class AddressTest {

    @Test
    fun snippet() {
        assertEquals(Address("1234", null, null, "20 Amandine Road", null, "London", "PBT567", null, "England").snippet, "1234, 20 Amandine Road, London, PBT567, England")
        assertEquals(Address(null, "Flat 2", "Gigi", "20 Amandine Road", null, "London", "PBT567", "Essex", "England").snippet, "Flat 2, 20 Amandine Road, London, PBT567, Essex, England (care of Gigi)")
    }
}
