package fr.xgouchet.packageexplorer.details.app

import android.app.Activity
import android.content.Intent
import fr.xgouchet.packageexplorer.core.utils.applicationInfoIntent
import fr.xgouchet.packageexplorer.core.utils.applicationPlayStoreIntent
import fr.xgouchet.packageexplorer.core.utils.uninstallPackageIntent
import fr.xgouchet.packageexplorer.details.AppInfoViewModel
import fr.xgouchet.packageexplorer.details.BaseDetailsPresenter
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers


/**
 * @author Xavier F. Gouchet
 */
class AppDetailsPresenter(activity: Activity,
                          val packageName: String,
                          val isSystemApp: Boolean)
    : BaseDetailsPresenter(null, activity.applicationContext) {

    override fun getDetails(): Observable<AppInfoViewModel> {
        return Observable.create(AppDetailsSource(context, packageName))
                .subscribeOn(Schedulers.io())
    }

    fun openAppInfo() {
        context.startActivity(applicationInfoIntent(packageName).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })
    }

    fun openPlayStore() {
        context.startActivity(applicationPlayStoreIntent(packageName).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })
    }


    fun openUninstaller() {
        context.startActivity(uninstallPackageIntent(packageName).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })
    }
}