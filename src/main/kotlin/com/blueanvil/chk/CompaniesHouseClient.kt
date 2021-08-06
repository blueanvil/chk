package com.blueanvil.chk

import com.beust.klaxon.JsonObject
import com.blueanvil.chk.lowlevel.CompaniesHouseRestClient
import com.blueanvil.chk.model.CompanyInfo
import com.blueanvil.chk.model.Officer
import com.blueanvil.chk.model.OfficerInfo
import io.github.bucket4j.BlockingBucket

/**
 * @author Cosmin Marginean
 */
class CompaniesHouseClient(apiKey: String,
                           bucket: BlockingBucket = CompaniesHouseRestClient.defaultBucket()) {

    private val restClient = CompaniesHouseRestClient(apiKey, bucket)

    fun searchCompanies(name: String): Sequence<CompanyInfo> {
        return restClient.allResults("/search/companies?q=${name.utf8UrlEncode()}")
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
                .map { OfficerInfo(it) }
    }

    fun officers(companyNumber: String): Sequence<Officer> {
        return restClient.allResults("/company/$companyNumber/officers")
                .map { Officer(it) }
    }

    fun appointments(officerId: String): Sequence<Officer> {
        return restClient.allResults("/officers/$officerId/appointments")
                .map { Officer(it) }
    }
}
