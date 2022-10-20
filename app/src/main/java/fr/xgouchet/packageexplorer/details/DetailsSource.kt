package fr.xgouchet.packageexplorer.details

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.FeatureInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.graphics.drawable.Drawable
import android.os.Build
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.core.utils.AndroidManifest
import fr.xgouchet.packageexplorer.core.utils.ManifestType
import fr.xgouchet.packageexplorer.core.utils.filterByName
import fr.xgouchet.packageexplorer.core.utils.formatItem
import fr.xgouchet.packageexplorer.core.utils.getItemsFromChildByType
import fr.xgouchet.packageexplorer.core.utils.humanReadableName
import fr.xgouchet.packageexplorer.details.adapter.AppInfoBullet
import fr.xgouchet.packageexplorer.details.adapter.AppInfoHeader
import fr.xgouchet.packageexplorer.details.adapter.AppInfoSimple
import fr.xgouchet.packageexplorer.details.adapter.AppInfoSubHeader
import fr.xgouchet.packageexplorer.details.adapter.AppInfoType
import fr.xgouchet.packageexplorer.details.adapter.AppInfoViewModel
import fr.xgouchet.packageexplorer.details.adapter.AppInfoWithIcon
import fr.xgouchet.packageexplorer.details.adapter.AppInfoWithSubtitle
import fr.xgouchet.packageexplorer.details.adapter.AppInfoWithSubtitleAndAction
import fr.xgouchet.packageexplorer.details.adapter.AppInfoWithSubtitleAndIcon
import io.reactivex.rxjava3.core.ObservableEmitter
import java.io.File
import java.security.MessageDigest
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import javax.security.cert.CertificateException
import javax.security.cert.X509Certificate
import timber.log.Timber

@Suppress("TooManyFunctions", "MagicNumber")
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
            onNext(
                AppInfoWithSubtitle(
                    AppInfoType.INFO_TYPE_METADATA,
                    PACKAGE_NAME,
                    packageInfo.packageName
                )
            )

            onNext(
                AppInfoHeader(
                    AppInfoType.INFO_TYPE_GLOBAL,
                    context.getString(R.string.header_type_global),
                    R.drawable.ic_category_global_info
                )
            )

            extractPackageAndVersion(packageInfo)

            if (applicationInfo != null) {
                extractAppInfo(applicationInfo)
            }

            extractInstallInfo(packageInfo)

            if (apkFile != null) {
                extractApkInfo(apkFile)
            }
        }
    }

    private fun ObservableEmitter<AppInfoViewModel>.extractAppInfo(
        applicationInfo: ApplicationInfo
    ) {
        onNext(
            AppInfoSimple(
                AppInfoType.INFO_TYPE_GLOBAL,
                "Target SDK : ${applicationInfo.targetSdkVersion}"
            )
        )

        onNext(
            AppInfoSimple(
                AppInfoType.INFO_TYPE_GLOBAL,
                "Min SDK : ${applicationInfo.minSdkVersion}"
            )
        )

        if ((applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0) {
            onNext(
                AppInfoWithIcon(
                    AppInfoType.INFO_TYPE_GLOBAL,
                    "System app",
                    null,
                    R.drawable.ic_flag_system_app
                )
            )
        }
        if ((applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
            onNext(
                AppInfoWithIcon(
                    AppInfoType.INFO_TYPE_GLOBAL,
                    "Debug version",
                    null,
                    R.drawable.ic_flag_debuggable
                )
            )
        }
        if ((applicationInfo.flags and ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
            onNext(
                AppInfoWithIcon(
                    AppInfoType.INFO_TYPE_GLOBAL,
                    "Installed on external storage",
                    null,
                    R.drawable.ic_flag_external_location
                )
            )
        }
        if ((applicationInfo.flags and ApplicationInfo.FLAG_LARGE_HEAP) != 0) {
            onNext(
                AppInfoWithIcon(
                    AppInfoType.INFO_TYPE_GLOBAL,
                    "Requires large heap",
                    null,
                    R.drawable.ic_flag_large_heap
                )
            )
        }
    }

    private fun ObservableEmitter<AppInfoViewModel>.extractInstallInfo(
        packageInfo: PackageInfo
    ) {
        var installLocation: String? = null
        when (packageInfo.installLocation) {
            PackageInfo.INSTALL_LOCATION_AUTO -> installLocation = "Install Location : Auto"
            PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY -> installLocation =
                "Install Location : Internal"
            PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL -> installLocation =
                "Install Location : External (if possible)"
        }
        if (installLocation != null) {
            onNext(
                AppInfoWithIcon(
                    AppInfoType.INFO_TYPE_GLOBAL,
                    installLocation,
                    null,
                    R.drawable.ic_flag_storage
                )
            )
        }
    }

    private fun ObservableEmitter<AppInfoViewModel>.extractApkInfo(
        apkFile: File
    ) {
        val sizeStr = humanReadableByteCount(apkFile.length())
        onNext(
            AppInfoWithIcon(
                AppInfoType.INFO_TYPE_GLOBAL,
                "Local APK size: $sizeStr",
                null,
                R.drawable.ic_apk_size
            )
        )

        val nativeLibraries = nativeLibraries(apkFile)
        if (nativeLibraries.isNotEmpty()) {
            onNext(
                AppInfoHeader(
                    AppInfoType.INFO_TYPE_NATIVE,
                    context.getString(R.string.header_type_native_libs),
                    R.drawable.ic_category_native_libs
                )
            )
            val nativeLibGroups = nativeLibraries.groupBy { it.group }
            nativeLibGroups.forEach { (group, list) ->
                onNext(
                    AppInfoSubHeader(
                        AppInfoType.INFO_TYPE_NATIVE,
                        context.getString(group)
                    )
                )
                list.forEach { lib ->
                    onNext(AppInfoSimple(AppInfoType.INFO_TYPE_NATIVE, lib.description()))
                }
            }
        }
    }

    private fun ObservableEmitter<AppInfoViewModel>.extractPackageAndVersion(
        packageInfo: PackageInfo
    ) {
        val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            @Suppress("DEPRECATION")
            packageInfo.versionCode.toLong()
        }
        onNext(
            AppInfoSimple(
                AppInfoType.INFO_TYPE_GLOBAL,
                "Version Code : $versionCode"
            )
        )
        onNext(
            AppInfoSimple(
                AppInfoType.INFO_TYPE_GLOBAL,
                "Version Name : “${packageInfo.versionName}”"
            )
        )
    }

    protected fun extractActivities(
        emitter: ObservableEmitter<AppInfoViewModel>,
        packageInfo: PackageInfo,
        packageManager: PackageManager
    ) {
        val activities = packageInfo.activities ?: return
        val packageName = packageInfo.packageName

        emitter.apply {
            onNext(
                AppInfoHeader(
                    AppInfoType.INFO_TYPE_ACTIVITIES,
                    context.getString(R.string.header_type_activities),
                    R.drawable.ic_category_activity
                )
            )

            for (activity in activities) {
                val name = simplifyName(activity.name, packageName)
                val label: String = activity.loadLabel(packageManager).toString()
                var icon: Drawable? = null
                try {
                    val component = ComponentName(packageName, activity.name)
                    icon = packageManager.getActivityIcon(component)
                } catch (ignore: PackageManager.NameNotFoundException) {
                }

                onNext(
                    AppInfoWithSubtitleAndIcon(
                        AppInfoType.INFO_TYPE_ACTIVITIES,
                        label,
                        name,
                        activity.name,
                        icon
                    )
                )

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
            onNext(
                AppInfoHeader(
                    AppInfoType.INFO_TYPE_SERVICES,
                    context.getString(R.string.header_type_services),
                    R.drawable.ic_category_services
                )
            )

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
            onNext(
                AppInfoHeader(
                    AppInfoType.INFO_TYPE_PROVIDERS,
                    context.getString(R.string.header_type_providers),
                    R.drawable.ic_category_provider
                )
            )

            for (provider in providers) {
                val simplifiedName = simplifyName(provider.name, packageName)
                onNext(
                    AppInfoSimple(
                        AppInfoType.INFO_TYPE_PROVIDERS,
                        simplifiedName,
                        provider.name
                    )
                )
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
            onNext(
                AppInfoHeader(
                    AppInfoType.INFO_TYPE_RECEIVERS,
                    context.getString(R.string.header_type_receivers),
                    R.drawable.ic_category_receiver
                )
            )

            for (receiver in receivers) {
                val simplifiedName = simplifyName(receiver.name, packageName)
                onNext(
                    AppInfoSimple(
                        AppInfoType.INFO_TYPE_RECEIVERS,
                        simplifiedName,
                        receiver.name
                    )
                )

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
            onNext(
                AppInfoHeader(
                    AppInfoType.INFO_TYPE_CUSTOM_PERMISSIONS,
                    context.getString(R.string.header_type_custom_permissions),
                    R.drawable.ic_category_custom_permission
                )
            )

            for (cpi in permissions) {
                val description: String
                val simplified = simplifyName(cpi.name, packageInfo.packageName)

                description = if (simplified == C2D_MESSAGE) {
                    context.getString(R.string.permission_c2d_message_generic)
                } else {
                    context.getString(R.string.permission_custom)
                }
                onNext(
                    AppInfoWithSubtitle(
                        AppInfoType.INFO_TYPE_CUSTOM_PERMISSIONS,
                        simplified,
                        description,
                        cpi.name
                    )
                )
            }
        }
    }

    protected fun extractPermissions(
        emitter: ObservableEmitter<AppInfoViewModel>,
        packageInfo: PackageInfo
    ) {
        val permissions = packageInfo.requestedPermissions?.sorted() ?: return
        val customPermissions =
            packageInfo.permissions?.toList()?.mapNotNull { it.name } ?: emptyList<String?>()

        emitter.apply {
            onNext(
                AppInfoHeader(
                    AppInfoType.INFO_TYPE_PERMISSIONS,
                    context.getString(R.string.header_type_uses_permissions),
                    R.drawable.ic_category_permission
                )
            )

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
                        Timber.w("Unknown permission <\"$name\">")
                        context.getString(R.string.permission_unknown)
                    }
                }

                title = if (name.startsWith("android.permission.")) {
                    name.substring("android.permission.".length)
                } else {
                    simplified
                }

                onNext(
                    AppInfoWithSubtitle(
                        AppInfoType.INFO_TYPE_PERMISSIONS,
                        title,
                        description,
                        name
                    )
                )
            }
        }
    }

    protected fun extractFeatures(
        emitter: ObservableEmitter<AppInfoViewModel>,
        packageInfo: PackageInfo
    ) {
        val features = packageInfo.reqFeatures ?: return

        emitter.apply {
            onNext(
                AppInfoHeader(
                    AppInfoType.INFO_TYPE_FEATURES_REQUIRED,
                    context.getString(R.string.header_type_features),
                    R.drawable.ic_category_feature
                )
            )

            for (feature in features) {
                val info = getFeatureName(feature)

                if (feature.flags == FeatureInfo.FLAG_REQUIRED) {
                    onNext(
                        AppInfoSimple(
                            AppInfoType.INFO_TYPE_FEATURES_REQUIRED,
                            "$info (REQUIRED)"
                        )
                    )
                } else {
                    onNext(AppInfoSimple(AppInfoType.INFO_TYPE_FEATURES_REQUIRED, info))
                }
            }
        }
    }

    @SuppressLint("DiscouragedApi")
    private fun getFeatureName(feature: FeatureInfo): String {
        return if (feature.name == null) {
            val maj = feature.reqGlEsVersion shr 16
            val min = feature.reqGlEsVersion and 0xFFFF
            "OpenGL ES v$maj.$min"
        } else {
            val stringRes = context.resources.getIdentifier(
                feature.name.lowercase(),
                "string",
                context.packageName
            )
            if (stringRes == 0) {
                Timber.w("Unable to find description for <\"${feature.name}\">")
                feature.name
            } else {
                context.getString(stringRes)
            }
        }
    }

    protected fun extractSignatures(
        emitter: ObservableEmitter<AppInfoViewModel>,
        packageInfo: PackageInfo
    ) {

        val signatures: MutableSet<Signature> = signatures(packageInfo)
        if (signatures.isEmpty()) return

        emitter.apply {
            onNext(
                AppInfoHeader(
                    AppInfoType.INFO_TYPE_SIGNATURE,
                    context.getString(R.string.header_type_signature),
                    R.drawable.ic_category_signature
                )
            )

            for (signature in signatures) {
                try {
                    val cert = X509Certificate.getInstance(signature.toByteArray())
                    val name = cert.subjectDN.name
                    val sha1 = MessageDigest.getInstance("SHA-1").digest(cert.encoded).toHexString()
                    val sha256 =
                        MessageDigest.getInstance("SHA-256").digest(cert.encoded).toHexString()
                    val humanName = cert.humanReadableName()
                    onNext(
                        AppInfoWithSubtitleAndAction(
                            AppInfoType.INFO_TYPE_SIGNATURE,
                            humanName,
                            name,
                            name,
                            context.getString(R.string.action_more),
                            cert
                        )
                    )
                    onNext(
                        AppInfoWithSubtitle(
                            AppInfoType.INFO_TYPE_SIGNATURE,
                            "SHA-1",
                            sha1,
                            sha1
                        )
                    )
                    onNext(
                        AppInfoWithSubtitle(
                            AppInfoType.INFO_TYPE_SIGNATURE,
                            "SHA-256",
                            sha256,
                            sha256
                        )
                    )
                } catch (e: CertificateException) {
                    Timber.w("Can't read package signature", e)
                    onNext(
                        AppInfoWithSubtitle(
                            AppInfoType.INFO_TYPE_SIGNATURE,
                            "(Unreadable signature)",
                            signature.toCharsString(),
                            null
                        )
                    )
                }
            }
        }
    }

    private fun signatures(packageInfo: PackageInfo): MutableSet<Signature> {
        val signatures: MutableSet<Signature> = mutableSetOf()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val signingInfo = packageInfo.signingInfo
            if (signingInfo != null) {
                signingInfo.signingCertificateHistory?.let { signatures.addAll(it) }
                signingInfo.apkContentsSigners?.let { signatures.addAll(it) }
            }
        }
        @Suppress("DEPRECATION")
        packageInfo.signatures?.let { signatures.addAll(it) }
        return signatures
    }

    private fun extractIntentFilters(
        observableEmitter: ObservableEmitter<AppInfoViewModel>,
        infoType: Int,
        name: String
    ) {
        androidManifestXml?.let { manifest ->
            val parent = manifest.filterByName(name)
            val intentFilters = parent?.getItemsFromChildByType(ManifestType.INTENT_FILTER)

            intentFilters?.formatItem()?.forEach { intentFilter ->
                observableEmitter.onNext(AppInfoSubHeader(infoType, ManifestType.INTENT_FILTER))
                intentFilter.forEach { (tagName, item) ->
                    val content =
                        tagName + "\n" + item.entries.joinToString("\n") { "  ${it.key} = ${it.value}" }
                    val raw =
                        "<$tagName " + item.entries.joinToString(" ") { "${it.key}=\"${it.value}\"/>" }
                    observableEmitter.onNext(AppInfoBullet(infoType, content, raw))
                }
            }
        }
    }

    private fun simplifyName(name: String?, packageName: String?): String {
        return if (name == null) {
            "?"
        } else if (packageName == null) {
            name
        } else if (name.startsWith(packageName)) {
            name.substring(packageName.length)
        } else {
            name
        }
    }

    private fun humanReadableByteCount(bytes: Long): String {
        val unit = 1024
        if (bytes < unit) return "$bytes B"
        val exp = (Math.log(bytes.toDouble()) / Math.log(unit.toDouble())).toInt()
        val pre = "KMGTPE"[exp - 1]
        return String.format(
            Locale.US,
            "%.2f %sB",
            bytes / Math.pow(unit.toDouble(), exp.toDouble()),
            pre
        )
    }

    private fun nativeLibraries(apk: File): Collection<NativeLibrary> {
        val nativeLibs = mutableMapOf<String, NativeLibrary>()
        val zis = ZipInputStream(apk.inputStream())
        var zipEntry: ZipEntry? = zis.nextEntry
        while (zipEntry != null) {
            val path = zipEntry.name
            val segments = path.split(File.separatorChar)
            if (segments.size == 3 && segments[0] == "lib") {
                val fileName = segments[2]
                val arch = segments[1]

                val nativeLibrary = nativeLibs[fileName]
                if (nativeLibrary != null) {
                    nativeLibrary.withArch(arch)
                } else {
                    nativeLibs[fileName] = NativeLibrary.from(fileName, arch)
                }
            }
            zipEntry = zis.nextEntry
        }
        zis.closeEntry()
        zis.close()

        nativeLibs.values.filter { it.group == R.string.native_lib_group_unknown }
            .forEach {
                Timber.w("Unknown native library ${it.description()}")
            }

        return nativeLibs.values.sortedBy {
            if (it.group == R.string.native_lib_group_unknown) {
                Int.MAX_VALUE
            } else {
                it.group
            }
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
