package fr.xgouchet.packageexplorer.details.app

import android.app.Activity
import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import fr.xgouchet.packageexplorer.core.utils.applicationInfoIntent
import fr.xgouchet.packageexplorer.core.utils.applicationPlayStoreIntent
import fr.xgouchet.packageexplorer.core.utils.getMainActivities
import fr.xgouchet.packageexplorer.core.utils.getResolvedIntent
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
    : BaseDetailsPresenter<AppDetailsFragment>(null, activity.applicationContext) {

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
        // context.startActivity(applicationLaunchIntent(packageName).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })
        val resolvedInfos = getMainActivities(context, packageName)

        if (resolvedInfos.isEmpty()) {
            return
        } else if (resolvedInfos.size == 1) {
            val intent = getResolvedIntent(resolvedInfos[0])
            context.startActivity(intent)
        } else {
            displayer?.promptActivity(resolvedInfos)
//            // Prompt for one activity
//            val fm = getSupportFragmentManager()
//            val resolveDialog = ResolveInfoDialog()
//            val args = Bundle()
//            args.putParcelableArray(Constants.EXTRA_RESOLVE_INFO,
//                    mActivities.toArray(arrayOfNulls<ResolveInfo>(count)))
//            resolveDialog.setArguments(args)
//            resolveDialog.show(fm, "resolveDialog")
        }
    }
}