package fr.xgouchet.packageexplorer.core.utils

import javax.security.cert.X509Certificate

fun X509Certificate.humanReadableName(): String {
    val certificateName = subjectDN.name
    val properties = certificateName.split(Regex("(?<!\\\\),"))

    var organization: String? = null
    var name: String? = null
    for (property in properties) {
        val tokens = property.split("=")
        val key = tokens[0]
        val value = tokens[1].replace("\\,", ",")

        if (key == "CN") {
            name = value
        } else if (key == "O") {
            organization = value
        } else if (key == "OU" && organization.isNullOrBlank()) {
            organization = value
        }
    }

    return if (name != null) {
        if (organization != null) {
            "$name ($organization) ✓"
        } else {
            "$name ✓"
        }
    } else {
        if (organization != null) {
            "($organization) ✓"
        } else {
            "Unknown ✗"
        }
    }
}