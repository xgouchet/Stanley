package fr.xgouchet.gradle.plugin

data class OSSDependency(
    val name: String,
    val identifier: String,
    val license: String,
    val sourceUrl: String?
) {

    val licenseKey = SHORT_LICENSE_NAME[license]

    init {
        if (licenseKey == null) {
            System.err.println("Unknwon license: $license for $name [$identifier]")
        }
    }

    fun toJson(): String {
        val sourceField = if (sourceUrl != null) {
            "\"sourceUrl\":\"${sourceUrl}\""
        } else {
            "\"sourceUrl\":null"
        }
        return "{" +
            "\"name\":\"$name\", " +
            "\"identifier\":\"$identifier\", " +
            "\"license\":\"$license\"," +
            "\"licenseKey\":\"$licenseKey\"," +
            sourceField +
            "}"
    }

    companion object {
        private val SHORT_LICENSE_NAME = mapOf(
            "The Apache Software License, Version 2.0" to "Apache-2.0",
            "The Apache License, Version 2.0" to "Apache-2.0",
            "MIT License" to "MIT",
            "CC0" to "CC0 1.0"
        )
    }
}
