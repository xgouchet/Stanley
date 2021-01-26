package fr.xgouchet.packageexplorer.oss

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.details.adapter.AppInfoHeader
import fr.xgouchet.packageexplorer.details.adapter.AppInfoType
import fr.xgouchet.packageexplorer.details.adapter.AppInfoViewModel
import fr.xgouchet.packageexplorer.details.adapter.AppInfoWithSubtitle
import fr.xgouchet.packageexplorer.details.adapter.AppInfoWithSubtitleAndAction
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import java.io.IOException

class OSSDependenciesSource(val context: Context) :
    ObservableOnSubscribe<AppInfoViewModel> {

    override fun subscribe(emitter: ObservableEmitter<AppInfoViewModel>) {

        try {
            val stream = context.assets.open("oss_licenses.json")
            val gson: Gson = GsonBuilder().create()

            val data = gson.fromJson(
                stream.reader(Charsets.UTF_8),
                Array<OSSDependency>::class.java
            ).sortedBy { it.identifier }

            val androidX = data.filter { it.identifier.startsWith("androidx") }
            val kotlin = data.filter { it.identifier.startsWith("org.jetbrains.kotlin") }
            val misc = data.filter {
                (!it.identifier.startsWith("org.jetbrains.kotlin")) &&
                    (!it.identifier.startsWith("androidx"))
            }

            if (androidX.isNotEmpty()) {
                emitter.onNext(
                    AppInfoHeader(
                        AppInfoType.INFO_TYPE_ANDROID,
                        "AndroidX",
                        R.drawable.ic_oss_android_logo
                    )
                )
                androidX.forEach {
                    emitter.onNext(
                        convertDependency(
                            it,
                            AppInfoType.INFO_TYPE_ANDROID
                        )
                    )
                }
            }

            if (kotlin.isNotEmpty()) {
                emitter.onNext(
                    AppInfoHeader(
                        AppInfoType.INFO_TYPE_KOTLIN,
                        "Kotlin",
                        R.drawable.ic_oss_kotlin_logo
                    )
                )
                kotlin.forEach {
                    emitter.onNext(
                        convertDependency(
                            it,
                            AppInfoType.INFO_TYPE_KOTLIN
                        )
                    )
                }
            }

            if (misc.isNotEmpty()) {
                emitter.onNext(
                    AppInfoHeader(
                        AppInfoType.INFO_TYPE_MISC,
                        "Misc",
                        R.drawable.ic_oss_package_logo
                    )
                )
                misc.forEach { emitter.onNext(convertDependency(it, AppInfoType.INFO_TYPE_MISC)) }
            }

            emitter.onComplete()
        } catch (e: IOException) {
        }
    }

    private fun convertDependency(ossDependency: OSSDependency, type: Int): AppInfoViewModel {
        val title = "${ossDependency.name} — ${ossDependency.licenseKey}"
        val subtitle = "${ossDependency.identifier}\n${ossDependency.license}"
        val raw = ossDependency.identifier
        val actionData = ossDependency.sourceUrl
        return if (actionData.isNullOrBlank()) {
            AppInfoWithSubtitle(type, title, subtitle, raw)
        } else {
            AppInfoWithSubtitleAndAction(type, title, subtitle, raw, "Source", actionData)
        }
    }
}
