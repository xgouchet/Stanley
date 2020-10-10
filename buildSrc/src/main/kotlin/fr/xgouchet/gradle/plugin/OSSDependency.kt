package fr.xgouchet.gradle.plugin

data class OSSDependency(
    val name: String,
    val identifier: String,
    val license: String,
    val sourceUrl: String?
) {
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
                sourceField +
                "}"
    }
}
