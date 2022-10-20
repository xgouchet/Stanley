package fr.xgouchet.packageexplorer.parser

data class Identifier(
    val localName: String,
    val namespaceUri: String?,
    val namespacePrefix: String?,
    val qualifiedName: String
) {
    override fun toString(): String {
        return if (namespaceUri == null) {
            "Identifier(“$localName”)"
        } else {
            "Identifier(“${qualifiedName}”, $namespaceUri)"
        }
    }
}
