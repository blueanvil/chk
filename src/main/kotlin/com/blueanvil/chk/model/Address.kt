package com.blueanvil.chk.model

import com.beust.klaxon.JsonObject
import com.blueanvil.chk.ChJson
import com.blueanvil.chk.tidySpaces

data class Address(val premises: String? = null,
                   val addressLine1: String? = null,
                   val addressLine2: String? = null,
                   val locality: String? = null,
                   val postCode: String? = null,
                   val country: String? = null) {

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

    companion object {
        fun fromRecord(record: JsonObject): Address? {
            val key = ChJson.addressFields.first { record.containsKey(it) }
            val fullAddress = record.obj(key)
            if (fullAddress != null) {
                return Address(premises = fullAddress.string("premises"),
                        addressLine1 = fullAddress.string("address_line_1"),
                        addressLine2 = fullAddress.string("address_line_2"),
                        locality = fullAddress.string("locality"),
                        postCode = fullAddress.string("postal_code"),
                        country = fullAddress.string("country"))
            }

            return null
        }
    }
}
