package fr.xgouchet.packageexplorer.applist

import android.content.Context
import android.content.pm.PackageManager
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe

/**
 * @author Xavier F. Gouchet
 */
class AppListSource(val context: Context) :
    ObservableOnSubscribe<AppViewModel> {

    override fun subscribe(emitter: ObservableEmitter<AppViewModel>) {
        val pm = context.packageManager
        val applications = pm.getInstalledApplications(0)
        val packages = pm.getInstalledPackages(PackageManager.GET_SIGNATURES or PackageManager.GET_SIGNING_CERTIFICATES)

//        var ai: ApplicationInfo
//        for (pi in packages) {
//            ai = pi.applicationInfo
//            if (ai == null) continue
//            val app = AppViewModel.fromAppInfo(pm, pi, ai)
//
//            emitter.onNext(app)
//        }

        applications.forEach {
            val app = AppViewModel.fromAppInfo(pm, null, it)
            emitter.onNext(app)
        }

        emitter.onComplete()
    }
}
