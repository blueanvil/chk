# Companies House Kotlin Client
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Build Status](https://travis-ci.com/blueanvil/chk.svg?branch=main)](https://travis-ci.com/blueanvil/chk)
[![Coverage Status](https://coveralls.io/repos/github/blueanvil/chk/badge.svg?branch=main)](https://coveralls.io/github/blueanvil/chk?branch=main)

CHK is a Kotlin library for interacting with the [Companies House REST API](https://developer.company-information.service.gov.uk/overview) 

# Gradle
```
repositories {
    maven { url "https://jitpack.io" }
}

dependencies {
    compile 'com.github.blueanvil:chk:1.0.9'
}
```

# Usage
```kotlin
val client = CompaniesHouseClient(apiKey)
client.searchCompanies("banana")
        .forEach { company ->
            println("${company.name}/${company.companyNumber}")
        }

client.appointments(officerId)
        .forEach { appointment ->
            println(appointment.companyName)
        }
```

# Low-level client
If you'd like to extend the library's functionality or simply require a more basic interaction with the API,
you can use a low-level client based on [OkHttp](https://square.github.io/okhttp/) and [Klaxon](https://github.com/cbeust/klaxon).
```kotlin
val client = CompaniesHouseRestClient(apiKey)

val request = client.request("/company/02685120")
        .get()
println(request.code)

client.allResults("/search/companies?q=powders")
        .forEach { json ->
            println(json.string("company_number"))
        }
```

# License
The code is licensed under [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0).
