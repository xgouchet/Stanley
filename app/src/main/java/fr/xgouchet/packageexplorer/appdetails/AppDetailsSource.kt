package fr.xgouchet.packageexplorer.appdetails


import android.content.ComponentName
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.FeatureInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.appdetails.AppInfoType.INFO_TYPE_ACTIVITIES
import fr.xgouchet.packageexplorer.appdetails.AppInfoType.INFO_TYPE_CUSTOM_PERMISSIONS
import fr.xgouchet.packageexplorer.appdetails.AppInfoType.INFO_TYPE_FEATURES_REQUIRED
import fr.xgouchet.packageexplorer.appdetails.AppInfoType.INFO_TYPE_GLOBAL
import fr.xgouchet.packageexplorer.appdetails.AppInfoType.INFO_TYPE_PERMISSIONS
import fr.xgouchet.packageexplorer.appdetails.AppInfoType.INFO_TYPE_PROVIDERS
import fr.xgouchet.packageexplorer.appdetails.AppInfoType.INFO_TYPE_RECEIVERS
import fr.xgouchet.packageexplorer.appdetails.AppInfoType.INFO_TYPE_SERVICES
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import mu.KLogging

/**
 * @author Xavier F. Gouchet
 */
class AppDetailsSource(val context: Context,
                       val packageName: String)
    : ObservableOnSubscribe<AppInfoViewModel> {

    companion object : KLogging() {
        val PACKAGE_INFO_FLAGS = PackageManager.GET_ACTIVITIES
                .or(PackageManager.GET_GIDS)
                .or(PackageManager.GET_CONFIGURATIONS)
                .or(PackageManager.GET_INSTRUMENTATION)
                .or(PackageManager.GET_PERMISSIONS)
                .or(PackageManager.GET_PROVIDERS)
                .or(PackageManager.GET_RECEIVERS)
                .or(PackageManager.GET_SERVICES)
//                .or(PackageManager.GET_SIGNATURES)

        val APP_INFO_FLAGS = 0
    }

    override fun subscribe(emitter: ObservableEmitter<AppInfoViewModel>?) {
        if (emitter == null) return

        try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(packageName, PACKAGE_INFO_FLAGS)
            val applicationInfo = packageManager.getApplicationInfo(packageName, APP_INFO_FLAGS)


            extractMainInfo(emitter, packageInfo, applicationInfo)

            extractFeatures(emitter, packageInfo)
            extractCustomPermissions(emitter, packageInfo)
            extractPermissions(emitter, packageInfo)

            extractActivities(emitter, packageInfo, packageManager)
            extractServices(emitter, packageInfo)
            extractReceivers(emitter, packageInfo)
            extractProviders(emitter, packageInfo)

            emitter.onComplete()

        } catch (e: PackageManager.NameNotFoundException) {
            emitter.onError(e)
        }
    }

    private fun extractMainInfo(emitter: ObservableEmitter<AppInfoViewModel>,
                                packageInfo: PackageInfo,
                                applicationInfo: ApplicationInfo) {
        emitter.apply {
            emitter.onNext(AppInfoHeader(INFO_TYPE_GLOBAL, "Global Information", R.drawable.ic_info))

            onNext(AppInfoSimple(INFO_TYPE_GLOBAL, "Version Code : ${packageInfo.versionCode}"))
            onNext(AppInfoSimple(INFO_TYPE_GLOBAL, "Version Name : “${packageInfo.versionName}”"))
            onNext(AppInfoSimple(INFO_TYPE_GLOBAL, "Target SDK : ${applicationInfo.targetSdkVersion}"))

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                var installLocation: String? = null
                when (packageInfo.installLocation) {
                    PackageInfo.INSTALL_LOCATION_AUTO -> installLocation = "Install Location : Auto"
                    PackageInfo.INSTALL_LOCATION_INTERNAL_ONLY -> installLocation = "Install Location : Internal"
                    PackageInfo.INSTALL_LOCATION_PREFER_EXTERNAL -> installLocation = "Install Location : External (if possible)"
                }
                if (installLocation != null) {
                    onNext(AppInfoSimple(INFO_TYPE_GLOBAL, installLocation))
                }
            }
        }
    }

    private fun extractActivities(emitter: ObservableEmitter<AppInfoViewModel>,
                                  packageInfo: PackageInfo,
                                  packageManager: PackageManager) {
        val activities = packageInfo.activities ?: return
        val packageName = packageInfo.packageName

        emitter.apply {
            emitter.onNext(AppInfoHeader(INFO_TYPE_ACTIVITIES, "Activities", R.drawable.ic_activity))

            for (activity in activities) {
                val name = simplifyName(activity.name, packageName)
                val label = activity.loadLabel(packageManager).toString()
                var icon: Drawable? = null
                try {
                    val component = ComponentName(packageName, activity.name)
                    icon = packageManager.getActivityIcon(component)
                } catch (ignore: PackageManager.NameNotFoundException) {
                }

                emitter.onNext(AppInfoWithSubtitleAndIcon(INFO_TYPE_ACTIVITIES, label, name, icon))
            }
        }
    }

    private fun extractServices(emitter: ObservableEmitter<AppInfoViewModel>,
                                packageInfo: PackageInfo) {
        val services = packageInfo.services ?: return
        val packageName = packageInfo.packageName

        emitter.apply {
            emitter.onNext(AppInfoHeader(INFO_TYPE_SERVICES, "Services", R.drawable.ic_services))

            for (service in services) {
                onNext(AppInfoSimple(INFO_TYPE_SERVICES, simplifyName(service.name, packageName)))
            }
        }
    }


    private fun extractProviders(emitter: ObservableEmitter<AppInfoViewModel>,
                                 packageInfo: PackageInfo) {
        val providers = packageInfo.providers ?: return
        val packageName = packageInfo.packageName

        emitter.apply {
            emitter.onNext(AppInfoHeader(INFO_TYPE_PROVIDERS, "Content Providers", R.drawable.ic_provider))

            for (provider in providers) {
                onNext(AppInfoSimple(INFO_TYPE_PROVIDERS, simplifyName(provider.name, packageName)))
            }
        }
    }

    private fun extractReceivers(emitter: ObservableEmitter<AppInfoViewModel>,
                                 packageInfo: PackageInfo) {
        val receivers = packageInfo.receivers ?: return
        val packageName = packageInfo.packageName

        emitter.apply {
            emitter.onNext(AppInfoHeader(INFO_TYPE_RECEIVERS, "Broadcast Receivers", R.drawable.ic_receiver))

            for (receiver in receivers) {
                onNext(AppInfoSimple(INFO_TYPE_RECEIVERS, simplifyName(receiver.name, packageName)))
            }
        }
    }

    private fun extractCustomPermissions(emitter: ObservableEmitter<AppInfoViewModel>,
                                         packageInfo: PackageInfo) {

        val permissions = packageInfo.permissions ?: return

        emitter.apply {
            emitter.onNext(AppInfoHeader(INFO_TYPE_CUSTOM_PERMISSIONS, "Custom Permissions", R.drawable.ic_custom_permission))

            for (cpi in permissions) {
                val info: String
                if (cpi.name.endsWith(".C2D_MESSAGE")) {
                    info = context.getString(R.string.c2d_message_generic)
                } else {
                    info = simplifyName(cpi.name, packageInfo.packageName)
                }
                onNext(AppInfoSimple(INFO_TYPE_CUSTOM_PERMISSIONS, info))
            }
        }
    }

    private fun extractPermissions(emitter: ObservableEmitter<AppInfoViewModel>,
                                   packageInfo: PackageInfo) {
        val permissions = packageInfo.requestedPermissions ?: return

        emitter.apply {
            emitter.onNext(AppInfoHeader(INFO_TYPE_PERMISSIONS, "Requested Permissions", R.drawable.ic_permission))

            for (name in permissions) {
                val stringRes = context.resources.getIdentifier(name, "string", context.packageName)


                val description: String
                val title: String

                if (stringRes == 0) {
                    if (name.endsWith(".permission.C2D_MESSAGE")) {
                        description = context.getString(R.string.c2d_message_generic)
                    } else {
                        Log.e("Apps", "Unable to find description for <\"$name\">")
                        description = "?"
                    }
                } else {
                    description = context.getString(stringRes)
                }

                if (name.endsWith(".permission.C2D_MESSAGE")) {
                    title = ".permission.C2D_MESSAGE"
                } else if (name.startsWith("android.permission.")) {
                    title = name.substring("android.permission.".length)
                } else {
                    title = name
                }

                onNext(AppInfoWithSubtitle(INFO_TYPE_PERMISSIONS, title, description))
            }
        }
    }

    private fun extractFeatures(emitter: ObservableEmitter<AppInfoViewModel>,
                                packageInfo: PackageInfo) {
        val features = packageInfo.reqFeatures ?: return

        emitter.apply {
            emitter.onNext(AppInfoHeader(INFO_TYPE_FEATURES_REQUIRED, "Features required", R.drawable.ic_feature))

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
                    emitter.onNext(AppInfoSimple(INFO_TYPE_FEATURES_REQUIRED, "$info (REQUIRED)"))
                } else {
                    emitter.onNext(AppInfoSimple(INFO_TYPE_FEATURES_REQUIRED, info))
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
}