package com.blueanvil.chk.client

import com.beust.klaxon.JsonObject
import com.blueanvil.chk.lowlevel.ChJson
import com.blueanvil.chk.lowlevel.OfficerData
import com.blueanvil.chk.tidySpaces

/**
 * @author Cosmin Marginean
 */
data class Address(var premises: String? = null,
                   var addressLine1: String? = null,
                   var addressLine2: String? = null,
                   var locality: String? = null,
                   var postCode: String? = null,
                   var country: String? = null) {

    var snippet: String
        get() {
            return (("$premises $addressLine1").trim() + ", " +
                    addressLine2 + ", " +
                    locality + ", " +
                    postCode + ", " +
                    country)
                    .replace("null,", "")
                    .replace("null", "")
                    .trim()
                    .replace(",$".toRegex(), "")
                    .replace("^,".toRegex(), "")
                    .tidySpaces()
        }
        set(v) {}
}

fun address(jsonRecord: JsonObject): Address {
    val address = Address()
//        val snippet = jsonRecord.string(ADDRESS_SNIPPET)
//        if (snippet != null) {
//            address.snippet = snippet
//        }

    val fullAddress = jsonRecord.obj(ChJson.ADDRESS)
    if (fullAddress != null) {
        address.premises = fullAddress.string("premises")
        address.addressLine1 = fullAddress.string("address_line_1")
        address.addressLine2 = fullAddress.string("address_line_2")
        address.locality = fullAddress.string("locality")
        address.postCode = fullAddress.string("postal_code")
        address.country = fullAddress.string("country")
    }

    return address
}

fun officerData(searchResult: JsonObject): OfficerData? {
    val officerId = ChJson.officerId(searchResult)
    if (officerId != null) {
        return OfficerData(officerId, searchResult.int(ChJson.APPT_COUNT) ?: 0)
    }
    return null
}

