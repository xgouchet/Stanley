package fr.xgouchet.packageexplorer.core.details

import android.content.ComponentName
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.FeatureInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.details.AppInfoHeader
import fr.xgouchet.packageexplorer.details.AppInfoSimple
import fr.xgouchet.packageexplorer.details.AppInfoType
import fr.xgouchet.packageexplorer.details.AppInfoViewModel
import fr.xgouchet.packageexplorer.details.AppInfoWithIcon
import fr.xgouchet.packageexplorer.details.AppInfoWithSubtitle
import fr.xgouchet.packageexplorer.details.AppInfoWithSubtitleAndIcon
import io.reactivex.ObservableEmitter
import javax.security.cert.CertificateException
import javax.security.cert.X509Certificate

open class DetailsSource(val context: Context) {

    companion object {
        const val C2D_MESSAGE = ".permission.C2D_MESSAGE"
    }


    protected fun extractMainInfo(emitter: ObservableEmitter<AppInfoViewModel>,
                                  packageInfo: PackageInfo,
                                  applicationInfo: ApplicationInfo?) {
        emitter.apply {
            onNext(AppInfoWithSubtitle(AppInfoType.INFO_TYPE_METADATA, "PackageName", packageInfo.packageName))

            onNext(AppInfoHeader(AppInfoType.INFO_TYPE_GLOBAL, "Global Information", R.drawable.ic_category_global_info))

            onNext(AppInfoSimple(AppInfoType.INFO_TYPE_GLOBAL, "Version Code : ${packageInfo.versionCode}"))
            onNext(AppInfoSimple(AppInfoType.INFO_TYPE_GLOBAL, "Version Name : “${packageInfo.versionName}”"))

            if (applicationInfo != null) {
                onNext(AppInfoSimple(AppInfoType.INFO_TYPE_GLOBAL, "Target SDK : ${applicationInfo.targetSdkVersion}"))
                if ((applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0) {
                    onNext(AppInfoWithIcon(AppInfoType.INFO_TYPE_GLOBAL, "System app", null, R.drawable.ic_system_app))
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
        }
    }


    protected fun extractActivities(emitter: ObservableEmitter<AppInfoViewModel>,
                                    packageInfo: PackageInfo,
                                    packageManager: PackageManager) {
        val activities = packageInfo.activities ?: return
        val packageName = packageInfo.packageName

        emitter.apply {
            onNext(AppInfoHeader(AppInfoType.INFO_TYPE_ACTIVITIES, "Activities", R.drawable.ic_category_activity))

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
            }
        }
    }

    protected fun extractServices(emitter: ObservableEmitter<AppInfoViewModel>,
                                  packageInfo: PackageInfo) {
        val services = packageInfo.services ?: return
        val packageName = packageInfo.packageName

        emitter.apply {
            onNext(AppInfoHeader(AppInfoType.INFO_TYPE_SERVICES, "Services", R.drawable.ic_category_services))

            for (service in services) {
                val simplifiedName = simplifyName(service.name, packageName)
                onNext(AppInfoSimple(AppInfoType.INFO_TYPE_SERVICES, simplifiedName, service.name))
            }
        }
    }


    protected fun extractProviders(emitter: ObservableEmitter<AppInfoViewModel>,
                                   packageInfo: PackageInfo) {
        val providers = packageInfo.providers ?: return
        val packageName = packageInfo.packageName

        emitter.apply {
            onNext(AppInfoHeader(AppInfoType.INFO_TYPE_PROVIDERS, "Content Providers", R.drawable.ic_category_provider))

            for (provider in providers) {
                val simplifiedName = simplifyName(provider.name, packageName)
                onNext(AppInfoSimple(AppInfoType.INFO_TYPE_PROVIDERS, simplifiedName, provider.name))
            }
        }
    }

    protected fun extractReceivers(emitter: ObservableEmitter<AppInfoViewModel>,
                                   packageInfo: PackageInfo) {
        val receivers = packageInfo.receivers ?: return
        val packageName = packageInfo.packageName

        emitter.apply {
            onNext(AppInfoHeader(AppInfoType.INFO_TYPE_RECEIVERS, "Broadcast Receivers", R.drawable.ic_category_receiver))

            for (receiver in receivers) {
                val simplifiedName = simplifyName(receiver.name, packageName)
                onNext(AppInfoSimple(AppInfoType.INFO_TYPE_RECEIVERS, simplifiedName, receiver.name))
            }
        }
    }

    protected fun extractCustomPermissions(emitter: ObservableEmitter<AppInfoViewModel>,
                                           packageInfo: PackageInfo) {

        val permissions = packageInfo.permissions ?: return

        emitter.apply {
            onNext(AppInfoHeader(AppInfoType.INFO_TYPE_CUSTOM_PERMISSIONS, "Custom Permissions", R.drawable.ic_category_custom_permission))

            for (cpi in permissions) {
                val description: String
                val simplified = simplifyName(cpi.name, packageInfo.packageName)

                if (simplified == C2D_MESSAGE) {
                    description = context.getString(R.string.permission_c2d_message_generic)
                } else {
                    description = context.getString(R.string.permission_unknown)
                }
                onNext(AppInfoWithSubtitle(AppInfoType.INFO_TYPE_CUSTOM_PERMISSIONS, simplified, description, cpi.name))
            }
        }
    }

    protected fun extractPermissions(emitter: ObservableEmitter<AppInfoViewModel>,
                                     packageInfo: PackageInfo) {
        val permissions = packageInfo.requestedPermissions ?: return

        emitter.apply {
            onNext(AppInfoHeader(AppInfoType.INFO_TYPE_PERMISSIONS, "Requested Permissions", R.drawable.ic_category_permission))

            for (name in permissions) {
                val stringRes = context.resources.getIdentifier(name, "string", context.packageName)

                val description: String
                val title: String
                val simplified = simplifyName(name, packageInfo.packageName)

                if (stringRes == 0) {
                    if (simplified == C2D_MESSAGE) {
                        description = context.getString(R.string.permission_c2d_message_generic)
                    } else {
                        Log.e("Apps", "Unable to find description for <\"$name\">")
                        description = context.getString(R.string.permission_unknown)
                    }
                } else {
                    description = context.getString(stringRes)
                }

                if (name.startsWith("android.permission.")) {
                    title = name.substring("android.permission.".length)
                } else {
                    title = simplified
                }

                onNext(AppInfoWithSubtitle(AppInfoType.INFO_TYPE_PERMISSIONS, title, description, name))
            }
        }
    }

    protected fun extractFeatures(emitter: ObservableEmitter<AppInfoViewModel>,
                                  packageInfo: PackageInfo) {
        val features = packageInfo.reqFeatures ?: return

        emitter.apply {
            onNext(AppInfoHeader(AppInfoType.INFO_TYPE_FEATURES_REQUIRED, "Features required", R.drawable.ic_category_feature))

            for (feature in features) {
                val info: String
                if (feature.name == null) {
                    val maj = feature.reqGlEsVersion shr 16
                    val min = feature.reqGlEsVersion and 0xFFFF
                    info = "OpenGL ES v$maj.$min"
                } else {
                    val stringRes = context.resources.getIdentifier(feature.name, "string", context.packageName)
                    if (stringRes == 0) {
                        Log.e("Apps", "Unable to find description for <\"" + feature.name + "\">")
                        info = feature.name
                    } else {
                        info = context.getString(stringRes)
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

    protected fun extractSignatures(emitter: ObservableEmitter<AppInfoViewModel>,
                                    packageInfo: PackageInfo) {
        val signatures: Array<Signature> = packageInfo.signatures ?: return
        if (signatures.isEmpty()) return


        emitter.apply {
            onNext(AppInfoHeader(AppInfoType.INFO_TYPE_SIGNATURE, "Signing Certificate", R.drawable.ic_category_signature))

            for (signature in signatures) {
                try {
                    val cert = X509Certificate.getInstance(signature.toByteArray())
                    val name = cert.subjectDN.name
                    val humanName = extractHumanName(name)
                    onNext(AppInfoWithSubtitle(AppInfoType.INFO_TYPE_SIGNATURE, humanName, name, name))
                } catch (e: CertificateException) {
                    onNext(AppInfoWithSubtitle(AppInfoType.INFO_TYPE_SIGNATURE, "(Unreadable signature)", signature.toCharsString(), null))
                }
            }
        }


    }

    protected fun extractHumanName(name: String): String {
        val properties = name.split(",")

        var organization: String? = null
        for (property in properties) {
            val tokens = property.split("=")
            val key = tokens[0]
            val value = tokens[1]

            if (key == "CN") {
                return value
            } else if (key == "O") {
                organization = value
            } else if (key == "OU" && organization.isNullOrBlank()) {
                organization = value
            }
        }

        return organization ?: "Unknown"
    }

    protected fun simplifyName(name: String?, packageName: String?): String {
        if (name == null) return "?"
        if (packageName == null) return name

        if (name.startsWith(packageName)) {
            return name.substring(packageName.length)
        }

        return name
    }
}