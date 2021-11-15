package com.blueanvil.chk

import com.beust.klaxon.JsonObject
import com.blueanvil.chk.lowlevel.CompaniesHouseRestClient
import com.blueanvil.chk.model.*
import io.github.bucket4j.BlockingBucket

/**
 * @author Cosmin Marginean
 */
class CompaniesHouseClient(apiKey: String,
                           bucket: BlockingBucket = CompaniesHouseRestClient.defaultBucket()) {

    val restClient = CompaniesHouseRestClient(apiKey, bucket)

    fun searchCompanies(name: String): Sequence<CompanyInfo> {
        return restClient.allResults("/search/companies?q=${name.utf8UrlEncode()}")
                .sequence
                .map { CompanyInfo(it) }
    }

    fun companyProfile(companyNumber: String): JsonObject {
        return restClient.request("company/$companyNumber")
                .get()
                .checkOk()
                .json()
    }

    fun searchOfficers(name: String): Sequence<OfficerInfo> {
        return restClient.allResults("/search/officers?q=${name.utf8UrlEncode()}")
                .sequence
                .map { OfficerInfo(it) }
    }

    fun searchDisqualifiedOfficers(name: String): Sequence<DisqualifiedOfficerInfo> {
        return restClient.allResults("/search/disqualified-officers?q=${name.utf8UrlEncode()}")
                .sequence
                .map { DisqualifiedOfficerInfo(it) }
    }

    fun officers(companyNumber: String): Sequence<Officer> {
        return restClient.allResults("/company/$companyNumber/officers")
                .sequence
                .map { Officer(it) }
    }

    fun filingHistory(companyNumber: String): List<JsonObject> {
        return restClient.allResults("/company/$companyNumber/filing-history")
                .sequence
                .toList()
    }

    fun appointments(officerId: String): Appointments {
        val response = restClient.allResults("/officers/$officerId/appointments")
        return Appointments(name = response.firstResponse?.string(ChJson.NAME),
                dateOfBirth = ChkPartialDate.fromField(response.firstResponse?.get(ChJson.DATE_OF_BIRTH)),
                appointments = response.sequence.map { Officer(it, officerId) })
    }
}
