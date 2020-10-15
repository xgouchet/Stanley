package fr.xgouchet.packageexplorer.details

import android.content.ComponentName
import android.content.Context
import android.content.pm.*
import android.graphics.drawable.Drawable
import android.os.Build
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.core.utils.*
import fr.xgouchet.packageexplorer.details.adapter.*
import io.reactivex.ObservableEmitter
import timber.log.Timber
import java.io.File
import java.security.MessageDigest
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import javax.security.cert.CertificateException
import javax.security.cert.X509Certificate

open class DetailsSource(val context: Context) {

    companion object {
        const val C2D_MESSAGE = ".permission.C2D_MESSAGE"

        const val PACKAGE_NAME = "PackageName"

        private val HEX_CHARS = "0123456789ABCDEF".toCharArray()
    }

    protected var androidManifestXml: AndroidManifest? = null

    protected fun extractMainInfo(
            emitter: ObservableEmitter<AppInfoViewModel>,
            packageInfo: PackageInfo,
            applicationInfo: ApplicationInfo?,
            apkFile: File?
    ) {
        emitter.apply {
            onNext(AppInfoWithSubtitle(AppInfoType.INFO_TYPE_METADATA, PACKAGE_NAME, packageInfo.packageName))

            onNext(AppInfoHeader(AppInfoType.INFO_TYPE_GLOBAL, context.getString(R.string.header_type_global), R.drawable.ic_category_global_info))

            onNext(AppInfoSimple(AppInfoType.INFO_TYPE_GLOBAL, "Version Code : ${packageInfo.versionCode}"))
            onNext(AppInfoSimple(AppInfoType.INFO_TYPE_GLOBAL, "Version Name : “${packageInfo.versionName}”"))

            if (applicationInfo != null) {
                onNext(AppInfoSimple(AppInfoType.INFO_TYPE_GLOBAL, "Target SDK : ${applicationInfo.targetSdkVersion}"))

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    onNext(AppInfoSimple(AppInfoType.INFO_TYPE_GLOBAL, "Min SDK : ${applicationInfo.minSdkVersion}"))
                }

                if ((applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0) {
                    onNext(AppInfoWithIcon(AppInfoType.INFO_TYPE_GLOBAL, "System app", null, R.drawable.ic_flag_system_app))
                }
                if ((applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
                    onNext(AppInfoWithIcon(AppInfoType.INFO_TYPE_GLOBAL, "Debug version", null, R.drawable.ic_flag_debuggable))
                }
                if ((applicationInfo.flags and ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
                    onNext(AppInfoWithIcon(AppInfoType.INFO_TYPE_GLOBAL, "Installed on external storage", null, R.drawable.ic_flag_external_location))
                }
                if ((applicationInfo.flags and ApplicationInfo.FLAG_LARGE_HEAP) != 0) {
                    onNext(AppInfoWithIcon(AppInfoType.INFO_TYPE_GLOBAL, "Requires large heap", null, R.drawable.ic_flag_large_heap))
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                var installLocation: String? = null
                when (packageInfo.installLocation) {
                    PackageInfo.INSTALL_LOCATION_AUTO -> installLocation = "Install Location : Auto"
                    PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY -> installLocation = "Install Location : Internal"
                    PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL -> installLocation = "Install Location : External (if possible)"
                }
                if (installLocation != null) {
                    onNext(AppInfoWithIcon(AppInfoType.INFO_TYPE_GLOBAL, installLocation, null, R.drawable.ic_flag_storage))
                }
            }

            if (apkFile != null) {
                val sizeStr = humanReadableByteCount(apkFile.length())
                onNext(AppInfoWithIcon(AppInfoType.INFO_TYPE_GLOBAL, "Local APK size: $sizeStr", null, R.drawable.ic_apk_size))

                val nativeLibs = nativeLibraries(apkFile)
                if (nativeLibs.isNotEmpty()) {
                    onNext(AppInfoHeader(AppInfoType.INFO_TYPE_NATIVE, context.getString(R.string.header_type_native_libs), R.drawable.ic_category_native_libs))
                    nativeLibs.forEach {
                        onNext(AppInfoSimple(AppInfoType.INFO_TYPE_NATIVE, it, it))
                    }
                }
            }
        }
    }

    protected fun extractActivities(
            emitter: ObservableEmitter<AppInfoViewModel>,
            packageInfo: PackageInfo,
            packageManager: PackageManager
    ) {
        val activities = packageInfo.activities ?: return
        val packageName = packageInfo.packageName

        emitter.apply {
            onNext(AppInfoHeader(AppInfoType.INFO_TYPE_ACTIVITIES, context.getString(R.string.header_type_activities), R.drawable.ic_category_activity))

            for (activity in activities) {
                val name = simplifyName(activity.name, packageName)
                val label: String = activity.loadLabel(packageManager).toString()
                var icon: Drawable? = null
                try {
                    val component = ComponentName(packageName, activity.name)
                    icon = packageManager.getActivityIcon(component)
                } catch (ignore: PackageManager.NameNotFoundException) {
                }

                onNext(AppInfoWithSubtitleAndIcon(AppInfoType.INFO_TYPE_ACTIVITIES, label, name, activity.name, icon))

                extractIntentFilters(this, AppInfoType.INFO_TYPE_ACTIVITIES, activity.name)

            }
        }
    }

    protected fun extractServices(
            emitter: ObservableEmitter<AppInfoViewModel>,
            packageInfo: PackageInfo
    ) {
        val services = packageInfo.services ?: return
        val packageName = packageInfo.packageName

        emitter.apply {
            onNext(AppInfoHeader(AppInfoType.INFO_TYPE_SERVICES, context.getString(R.string.header_type_services), R.drawable.ic_category_services))

            for (service in services) {
                val simplifiedName = simplifyName(service.name, packageName)
                onNext(AppInfoSimple(AppInfoType.INFO_TYPE_SERVICES, simplifiedName, service.name))

                extractIntentFilters(this, AppInfoType.INFO_TYPE_SERVICES, service.name)

            }
        }
    }

    protected fun extractProviders(
            emitter: ObservableEmitter<AppInfoViewModel>,
            packageInfo: PackageInfo
    ) {
        val providers = packageInfo.providers ?: return
        val packageName = packageInfo.packageName

        emitter.apply {
            onNext(AppInfoHeader(AppInfoType.INFO_TYPE_PROVIDERS, context.getString(R.string.header_type_providers), R.drawable.ic_category_provider))

            for (provider in providers) {
                val simplifiedName = simplifyName(provider.name, packageName)
                onNext(AppInfoSimple(AppInfoType.INFO_TYPE_PROVIDERS, simplifiedName, provider.name))
            }
        }
    }

    protected fun extractReceivers(
            emitter: ObservableEmitter<AppInfoViewModel>,
            packageInfo: PackageInfo
    ) {
        val receivers = packageInfo.receivers ?: return
        val packageName = packageInfo.packageName

        emitter.apply {
            onNext(AppInfoHeader(AppInfoType.INFO_TYPE_RECEIVERS, context.getString(R.string.header_type_receivers), R.drawable.ic_category_receiver))

            for (receiver in receivers) {
                val simplifiedName = simplifyName(receiver.name, packageName)
                onNext(AppInfoSimple(AppInfoType.INFO_TYPE_RECEIVERS, simplifiedName, receiver.name))

                extractIntentFilters(this, AppInfoType.INFO_TYPE_RECEIVERS, receiver.name)
            }
        }
    }

    protected fun extractCustomPermissions(
            emitter: ObservableEmitter<AppInfoViewModel>,
            packageInfo: PackageInfo
    ) {

        val permissions = packageInfo.permissions ?: return

        emitter.apply {
            onNext(AppInfoHeader(AppInfoType.INFO_TYPE_CUSTOM_PERMISSIONS, context.getString(R.string.header_type_custom_permissions), R.drawable.ic_category_custom_permission))

            for (cpi in permissions) {
                val description: String
                val simplified = simplifyName(cpi.name, packageInfo.packageName)

                description = if (simplified == C2D_MESSAGE) {
                    context.getString(R.string.permission_c2d_message_generic)
                } else {
                    context.getString(R.string.permission_custom)
                }
                onNext(AppInfoWithSubtitle(AppInfoType.INFO_TYPE_CUSTOM_PERMISSIONS, simplified, description, cpi.name))
            }
        }
    }

    protected fun extractPermissions(
            emitter: ObservableEmitter<AppInfoViewModel>,
            packageInfo: PackageInfo
    ) {
        val permissions = packageInfo.requestedPermissions ?: return
        val customPermissions = packageInfo.permissions?.toList()?.map { it.name } ?: emptyList()

        emitter.apply {
            onNext(AppInfoHeader(AppInfoType.INFO_TYPE_PERMISSIONS, context.getString(R.string.header_type_uses_permissions), R.drawable.ic_category_permission))

            for (name in permissions) {
                val stringRes = context.resources.getIdentifier(name, "string", context.packageName)

                val description: String
                val title: String
                val simplified = simplifyName(name, packageInfo.packageName)

                description = when {
                    stringRes != 0 -> context.getString(stringRes)
                    simplified == C2D_MESSAGE -> context.getString(R.string.permission_c2d_message_generic)
                    name in customPermissions -> context.getString(R.string.permission_custom)
                    else -> {
                        Timber.e("Unable to find description for permission <\"$name\">")
                        context.getString(R.string.permission_unknown)
                    }
                }

                title = if (name.startsWith("android.permission.")) {
                    name.substring("android.permission.".length)
                } else {
                    simplified
                }

                onNext(AppInfoWithSubtitle(AppInfoType.INFO_TYPE_PERMISSIONS, title, description, name))
            }
        }
    }

    protected fun extractFeatures(
            emitter: ObservableEmitter<AppInfoViewModel>,
            packageInfo: PackageInfo
    ) {
        val features = packageInfo.reqFeatures ?: return

        emitter.apply {
            onNext(AppInfoHeader(AppInfoType.INFO_TYPE_FEATURES_REQUIRED, context.getString(R.string.header_type_features), R.drawable.ic_category_feature))

            for (feature in features) {
                val info = if (feature.name == null) {
                    val maj = feature.reqGlEsVersion shr 16
                    val min = feature.reqGlEsVersion and 0xFFFF
                    "OpenGL ES v$maj.$min"
                } else {
                    val stringRes = context.resources.getIdentifier(feature.name, "string", context.packageName)
                    if (stringRes == 0) {
                        Timber.e("Unable to find description for <\"${feature.name}\">")
                        feature.name
                    } else {
                        context.getString(stringRes)
                    }
                }

                if (feature.flags == FeatureInfo.FLAG_REQUIRED) {
                    onNext(AppInfoSimple(AppInfoType.INFO_TYPE_FEATURES_REQUIRED, "$info (REQUIRED)"))
                } else {
                    onNext(AppInfoSimple(AppInfoType.INFO_TYPE_FEATURES_REQUIRED, info))
                }
            }
        }
    }

    protected fun extractSignatures(
            emitter: ObservableEmitter<AppInfoViewModel>,
            packageInfo: PackageInfo
    ) {
        val signatures: Array<Signature> = packageInfo.signatures ?: return
        if (signatures.isEmpty()) return

        emitter.apply {
            onNext(AppInfoHeader(AppInfoType.INFO_TYPE_SIGNATURE, context.getString(R.string.header_type_signature), R.drawable.ic_category_signature))

            for (signature in signatures) {
                try {
                    val cert = X509Certificate.getInstance(signature.toByteArray())
                    val name = cert.subjectDN.name
                    val sha1 = MessageDigest.getInstance("SHA-1").digest(cert.encoded).toHexString()
                    val sha256 = MessageDigest.getInstance("SHA-256").digest(cert.encoded).toHexString()
                    val humanName = cert.humanReadableName()
                    onNext(AppInfoWithSubtitleAndAction(AppInfoType.INFO_TYPE_SIGNATURE, humanName, name, name, context.getString(R.string.action_more), cert))
                    onNext(AppInfoWithSubtitle(AppInfoType.INFO_TYPE_SIGNATURE, "SHA-1", sha1, sha1))
                    onNext(AppInfoWithSubtitle(AppInfoType.INFO_TYPE_SIGNATURE, "SHA-256", sha256, sha256))
                } catch (e: CertificateException) {
                    onNext(AppInfoWithSubtitle(AppInfoType.INFO_TYPE_SIGNATURE, "(Unreadable signature)", signature.toCharsString(), null))
                }
            }
        }
    }

    private fun extractIntentFilters(observableEmitter: ObservableEmitter<AppInfoViewModel>, infoType: Int, name: String) {
        androidManifestXml?.let { manifest ->
            val parent = manifest.filterByName(name)
            val intentFilters= parent.getItemsFromChildByType(ManifestType.INTENT_FILTER)

            intentFilters.formatItem().forEach { intentFilter ->
                observableEmitter.onNext(AppInfoSubHeader(infoType, ManifestType.INTENT_FILTER))
                intentFilter.forEach { (tagName, item) ->
                    observableEmitter.onNext(AppInfoSubHeader(infoType, tagName))
                    item.forEach {
                        observableEmitter.onNext(AppInfoBullet(infoType, it.key, it.value, it.value))
                    }
                }
            }
        }
    }

    private fun simplifyName(name: String?, packageName: String?): String {
        if (name == null) return "?"
        if (packageName == null) return name

        if (name.startsWith(packageName)) {
            return name.substring(packageName.length)
        }

        return name
    }

    private fun humanReadableByteCount(bytes: Long): String {
        val unit = 1024
        if (bytes < unit) return bytes.toString() + " B"
        val exp = (Math.log(bytes.toDouble()) / Math.log(unit.toDouble())).toInt()
        val pre = "KMGTPE"[exp - 1]
        return String.format("%.2f %sB", bytes / Math.pow(unit.toDouble(), exp.toDouble()), pre)
    }

    @Suppress("UsePropertyAccessSyntax")
    private fun nativeLibraries(apk: File): List<String> {
        val nativeLibs = mutableListOf<Pair<String, String>>()
        val zis = ZipInputStream(apk.inputStream())
        var zipEntry: ZipEntry? = zis.getNextEntry()
        while (zipEntry != null) {
            val path = zipEntry.name
            val segments = path.split(File.separatorChar)
            if (segments.size == 3 && segments[0] == "lib") {
                nativeLibs.add(segments[2] to segments[1])
            }
            zipEntry = zis.getNextEntry()
        }
        zis.closeEntry()
        zis.close()
        return nativeLibs.groupBy { it.first }
                .entries.map {
                    "${it.key} (${it.value.joinToString(", ") { it.second }})"
                }
    }

    private fun ByteArray.toHexString(): String {
        return joinToString(":") {
            val octet = it.toInt()
            val firstIndex = (octet and 0xF0).ushr(4)
            val secondIndex = octet and 0x0F
            "${HEX_CHARS[firstIndex]}${HEX_CHARS[secondIndex]}"
        }
    }

}



