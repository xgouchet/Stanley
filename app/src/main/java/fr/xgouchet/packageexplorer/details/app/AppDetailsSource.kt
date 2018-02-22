package fr.xgouchet.packageexplorer.details.app


import android.content.Context
import android.content.pm.PackageManager
import fr.xgouchet.packageexplorer.details.adapter.AppInfoViewModel
import fr.xgouchet.packageexplorer.details.DetailsSource
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe


/**
 * @author Xavier F. Gouchet
 */
class AppDetailsSource(context: Context,
                       val packageName: String)
    : DetailsSource(context),
        ObservableOnSubscribe<AppInfoViewModel> {

    companion object {
        val PACKAGE_INFO_FLAGS = PackageManager.GET_ACTIVITIES
                .or(PackageManager.GET_GIDS)
                .or(PackageManager.GET_CONFIGURATIONS)
                .or(PackageManager.GET_INSTRUMENTATION)
                .or(PackageManager.GET_PERMISSIONS)
                .or(PackageManager.GET_PROVIDERS)
                .or(PackageManager.GET_RECEIVERS)
                .or(PackageManager.GET_SERVICES)
                .or(PackageManager.GET_SIGNATURES)

        const val APP_INFO_FLAGS = PackageManager.GET_META_DATA
    }

    override fun subscribe(emitter: ObservableEmitter<AppInfoViewModel>) {

        try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(packageName, PACKAGE_INFO_FLAGS)
            val applicationInfo = packageManager.getApplicationInfo(packageName, APP_INFO_FLAGS)

            extractMainInfo(emitter, packageInfo, applicationInfo)

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