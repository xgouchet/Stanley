package fr.xgouchet.packageexplorer.details.apk


import android.content.Context
import android.content.pm.PackageManager
import fr.xgouchet.packageexplorer.details.DetailsSource
import fr.xgouchet.packageexplorer.details.adapter.AppInfoViewModel
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import java.io.File


/**
 * @author Xavier F. Gouchet
 */
class ApkDetailsSource(context: Context,
                       val path: String)
    : DetailsSource(context),
        ObservableOnSubscribe<AppInfoViewModel> {

    companion object {
        const val PACKAGE_INFO_FLAGS = PackageManager.GET_ACTIVITIES
                .or(PackageManager.GET_GIDS)
                .or(PackageManager.GET_CONFIGURATIONS)
                .or(PackageManager.GET_INSTRUMENTATION)
                .or(PackageManager.GET_PERMISSIONS)
                .or(PackageManager.GET_PROVIDERS)
                .or(PackageManager.GET_RECEIVERS)
                .or(PackageManager.GET_SERVICES)
                .or(PackageManager.GET_SIGNATURES)
        const val PACKAGE_INFO_FLAGS_NO_SIGN = PackageManager.GET_ACTIVITIES
                .or(PackageManager.GET_GIDS)
                .or(PackageManager.GET_CONFIGURATIONS)
                .or(PackageManager.GET_INSTRUMENTATION)
                .or(PackageManager.GET_PERMISSIONS)
                .or(PackageManager.GET_PROVIDERS)
                .or(PackageManager.GET_RECEIVERS)
                .or(PackageManager.GET_SERVICES)
    }

    override fun subscribe(emitter: ObservableEmitter<AppInfoViewModel>) {

        try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageArchiveInfo(path, PACKAGE_INFO_FLAGS)
                    ?: packageManager.getPackageArchiveInfo(path, PACKAGE_INFO_FLAGS_NO_SIGN)

            extractMainInfo(emitter, packageInfo, null, File(path))

            extractSignatures(emitter, packageInfo)

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

}
