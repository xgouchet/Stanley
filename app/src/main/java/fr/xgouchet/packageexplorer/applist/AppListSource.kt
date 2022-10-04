package fr.xgouchet.packageexplorer.applist

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe

/**
 * @author Xavier F. Gouchet
 */
class AppListSource(val context: Context) :
    ObservableOnSubscribe<AppViewModel> {

    @Suppress("DEPRECATION")
    override fun subscribe(emitter: ObservableEmitter<AppViewModel>) {
        val pm = context.packageManager
        val applications = pm.getInstalledApplications(0)
        val packages = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            pm.getInstalledPackages(PackageManager.GET_SIGNATURES or PackageManager.GET_SIGNING_CERTIFICATES)
        } else {
            pm.getInstalledPackages(PackageManager.GET_SIGNATURES )
        }

        applications.forEach {ai ->
            val pi = packages.firstOrNull() {
                it.packageName == ai.packageName
            }
            val app = AppViewModel.fromAppInfo(pm, pi, ai)
            emitter.onNext(app)
        }

        emitter.onComplete()
    }
}
