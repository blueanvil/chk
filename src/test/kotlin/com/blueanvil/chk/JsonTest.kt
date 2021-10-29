package com.blueanvil.chk

import com.beust.klaxon.JsonObject
import org.testng.Assert.assertEquals
import org.testng.annotations.Test

/**
 * @author Cosmin Marginean
 */
class JsonTest {

    @Test
    fun filingHistoryDescription() {
        assertEquals((klaxonJsonParser.parse(StringBuilder("""
            {
            "category": "capital",
            "date": "2021-07-05",
            "description": "legacy",
            "description_values": {
                "description": "Statement by Directors"
            },
            "links": {
                "self": "/company/05667032/filing-history/MzMwNjY3MjczOWFkaXF6a2N4",
                "document_metadata": "https://frontend-doc-api.company-information.service.gov.uk/document/07YwOtGABIu_D78rXNhFYnIsVNDMw2c6GsXy3Stz7bU"
            },
            "paper_filed": true,
            "type": "SH20",
            "pages": 1,
            "barcode": "RA78G5HC",
            "transaction_id": "MzMwNjY3MjczOWFkaXF6a2N4"
        }
        """)) as JsonObject).filingHistoryDescription(), "Statement by Directors")


        assertEquals((klaxonJsonParser.parse(StringBuilder("""
        {
            "action_date": "2021-07-05",
            "category": "capital",
            "date": "2021-07-05",
            "description": "capital-statement-capital-company-with-date-currency-figure",
            "description_values": {
                "capital": [
                    {
                        "figure": "1",
                        "currency": "GBP"
                    }
                ],
                "date": "2021-07-05"
            },
            "links": {
                "self": "/company/05667032/filing-history/MzMwNjY3MjYwNGFkaXF6a2N4",
                "document_metadata": "https://frontend-doc-api.company-information.service.gov.uk/document/H_5AWhRNdbVMRxKaqA4PQbHHVrHGqMeZzy1TyFSVCiE"
            },
            "paper_filed": true,
            "type": "SH19",
            "pages": 3,
            "barcode": "AA79SJ4Y",
            "transaction_id": "MzMwNjY3MjYwNGFkaXF6a2N4"
        }
                """)) as JsonObject).filingHistoryDescription(), "Statement of capital on 2021-07-05")
    }

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