package com.blueanvil.chk

import com.beust.klaxon.JsonObject
import org.testng.Assert.assertEquals
import org.testng.annotations.Test
import java.lang.StringBuilder

/**
 * @author Cosmin Marginean
 */
class JsonTest {

    @Test
    fun filingHistoryUrl() {
        val json = klaxonJsonParser.parse(StringBuilder("""
        {
            "action_date": "2021-05-06",
            "category": "confirmation-statement",
            "date": "2021-05-19",
            "description": "confirmation-statement-with-no-updates",
            "description_values": {
                "made_up_date": "2021-05-06"
            },
            "links": {
                "self": "/company/05957738/filing-history/MzMwMTUzMTMzNmFkaXF6a2N4",
                "document_metadata": "https://frontend-doc-api.company-information.service.gov.uk/document/b6ZSjAK7iuWVvqQp1mzYZZJwvM-hxT3NRB9kXQO4iDk"
            },
            "type": "CS01",
            "pages": 3,
            "barcode": "XA4TOG4Y",
            "transaction_id": "MzMwMTUzMTMzNmFkaXF6a2N4"
        }
        """)) as JsonObject
        assertEquals(json.filingHistoryUrl(), "https://find-and-update.company-information.service.gov.uk/company/05957738/filing-history/MzMwMTUzMTMzNmFkaXF6a2N4/document?format=pdf&download=0")
    }
}