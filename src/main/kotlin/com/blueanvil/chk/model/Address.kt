package com.blueanvil.chk.model

import com.beust.klaxon.JsonObject
import com.blueanvil.chk.ChJson
import com.blueanvil.chk.tidySpaces

data class Address(val poBox: String? = null,
                   val premises: String? = null,
                   val careOf: String? = null,
                   val addressLine1: String? = null,
                   val addressLine2: String? = null,
                   val locality: String? = null,
                   val postCode: String? = null,
                   val region: String? = null,
                   val country: String? = null) {

    var snippet: String
        get() {
            var addressBase = "$poBox, $premises, $addressLine1, $addressLine2, $locality, $postCode, $region, $country"
            if (careOf != null) {
                addressBase += " (care of $careOf)"
            }
            return addressBase
                    .replace("null,", "")
                    .replace("null", "")
                    .trim()
                    .replace(",$".toRegex(), "")
                    .replace("^,".toRegex(), "")
                    .tidySpaces()
        }
        set(v) {}

    companion object {
        fun fromRecord(record: JsonObject): Address? {
            val key = ChJson.addressFields.first { record.containsKey(it) }
            val fullAddress = record.obj(key)
            if (fullAddress != null) {
                return Address(
                        poBox = fullAddress.string("po_box"),
                        premises = fullAddress.string("premises"),
                        addressLine1 = fullAddress.string("address_line_1"),
                        addressLine2 = fullAddress.string("address_line_2"),
                        locality = fullAddress.string("locality"),
                        postCode = fullAddress.string("postal_code"),
                        region = fullAddress.string("region"),
                        country = fullAddress.string("country"),
                        careOf = fullAddress.string("care_of"))
            }

            return null
        }
    }
}
