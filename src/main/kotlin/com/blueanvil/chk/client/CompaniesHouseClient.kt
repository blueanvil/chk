package com.blueanvil.chk.client

import com.blueanvil.chk.lowlevel.ChJson
import com.blueanvil.chk.lowlevel.CompaniesHouseRestClient
import com.blueanvil.chk.utf8UrlEncode
import io.github.bucket4j.BlockingBucket

/**
 * @author Cosmin Marginean
 */
class CompaniesHouseClient(private val apiKey: String,
                           private val bucket: BlockingBucket = CompaniesHouseRestClient.defaultBucket()) {

    val restClient = CompaniesHouseRestClient(apiKey, bucket)

    fun searchCompanies(name: String): Sequence<CompanyInfo> {
        return restClient.allResults("/search/companies?q=${name.utf8UrlEncode()}")
                .map { json ->
                    CompanyInfo(
                            name = json.string(ChJson.TITLE)!!,
                            companyNumber = json.string(ChJson.COMPANY_NUMBER)?.padStart(8, '0'),
                            officerData = json.officerData(),
                            companyStatus = json.string(ChJson.COMPANY_STATUS)
                    )
                }
    }
}
