package fr.xgouchet.packageexplorer.oss

import com.google.gson.annotations.SerializedName

data class OSSDependency(
    @SerializedName("name") val name: String,
    @SerializedName("identifier") val identifier: String,
    @SerializedName("license") val license: String,
    @SerializedName("licenseKey") val licenseKey: String,
    @SerializedName("sourceUrl") val sourceUrl: String?
)
