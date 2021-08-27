package fr.xgouchet.packageexplorer.details.app

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import fr.xgouchet.packageexplorer.core.utils.applicationInfoIntent
import fr.xgouchet.packageexplorer.core.utils.applicationPlayStoreIntent
import fr.xgouchet.packageexplorer.core.utils.exportManifestFromPackage
import fr.xgouchet.packageexplorer.core.utils.getMainActivities
import fr.xgouchet.packageexplorer.core.utils.getResolvedIntent
import fr.xgouchet.packageexplorer.core.utils.uninstallPackageIntent
import fr.xgouchet.packageexplorer.details.BaseDetailsPresenter
import fr.xgouchet.packageexplorer.details.adapter.AppInfoViewModel
import fr.xgouchet.packageexplorer.ui.mvp.Navigator
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.security.cert.X509Certificate

/**
 * @author Xavier F. Gouchet
 */
class AppDetailsPresenter(
    activity: Activity,
    certficateNavigator: Navigator<X509Certificate>,
    val packageName: String,
    val isSystemApp: Boolean
) :
    BaseDetailsPresenter<AppDetailsFragment>(null, certficateNavigator, activity.applicationContext) {

    private var exportDisposable: Disposable? = null

    override fun onDisplayerDetached() {
        super.onDisplayerDetached()
        exportDisposable?.dispose()
        exportDisposable = null
    }

    override fun getDetails(): Observable<AppInfoViewModel> {
        return Observable.create(AppDetailsSource(context, packageName))
                .subscribeOn(Schedulers.io())
    }

    fun openAppInfo() {
        val intent = applicationInfoIntent(packageName)
                .apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
        context.startActivity(intent)
    }

    fun openPlayStore() {
        val intent = applicationPlayStoreIntent(packageName)
                .apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
        context.startActivity(intent)
    }

    fun openUninstaller() {
        val intent = uninstallPackageIntent(packageName)
                .apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
        context.startActivity(intent)
    }

    fun openApplication() {
        val resolvedInfos = getMainActivities(context, packageName)

        if (resolvedInfos.isEmpty()) {
            return
        } else if (resolvedInfos.size == 1) {
            val intent = getResolvedIntent(resolvedInfos[0])
            context.startActivity(intent)
        } else {
            displayer?.promptActivity(resolvedInfos)
        }
    }

    fun exportManifest() {
        val packageInfo = try {
            context.packageManager.getPackageInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            displayer?.setError(e)
            return
        }
        exportDisposable = exportManifestFromPackage(packageInfo, context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    displayer?.onManifestExported(it)
                }, {
                    displayer?.setError(it)
                })
    }
}
