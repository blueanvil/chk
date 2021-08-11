package com.blueanvil.chk

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory

/**
 * @author Cosmin Marginean
 */
object CompaniesHouseEnums {

    val enumerationNames = listOf(
            "constants.yml",
            "disqualified_officer_descriptions.yml",
            "errors.yml",
            "exemption_descriptions.yml",
            "filing_descriptions.yml",
            "filing_history_descriptions.yml",
            "filing_history_exceptions.yml",
            "missing_image_delivery.yaml",
            "mortgage_descriptions.yml",
            "orders_descriptions.yaml",
            "payments.yaml",
            "psc_descriptions.yml",
            "search_descriptions_raw.yaml")

    private val mapper: ObjectMapper = ObjectMapper(YAMLFactory())
    private val httpClient = httpClient()
    private val enumerations: Map<String, ChEnumeration>

    init {
        enumerations = enumerationNames
                .map { name ->
                    name to loadEnumeration(name)
                }
                .toMap()
    }

    fun getEnumeration(name: String): ChEnumeration {
        return enumerations[name] ?: throw IllegalArgumentException("Could not find Companies House enumeration '$name'")
    }

    private fun loadEnumeration(name: String): ChEnumeration {
        val path = "https://raw.githubusercontent.com/companieshouse/api-enumerations/master/$name"
        val text = httpClient.get(path)
                .checkOk()
                .text()
        val map = mapper.readValue(text, Map::class.java) as Map<String, Any>
        return ChEnumeration(name = name,
                sections = map.map { entry ->
                    entry.key to ChEnumSection(name = entry.key, mappings = entry.value as Map<String, String>)
                }.toMap())
    }
}

data class ChEnumeration(val name: String,
                         val sections: Map<String, ChEnumSection>)

data class ChEnumSection(val name: String,
                         val mappings: Map<String, String>)
