package fr.xgouchet.packageexplorer.applist

import android.content.Context
import android.content.pm.ApplicationInfo
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe

/**
 * @author Xavier F. Gouchet
 */
class AppListSource(val context: Context)
    : ObservableOnSubscribe<AppViewModel> {

    override fun subscribe(emitter: ObservableEmitter<AppViewModel>) {
        val pm = context.packageManager
        val packages = pm.getInstalledPackages(0)

        var ai: ApplicationInfo
        for (pi in packages) {
            ai = pi.applicationInfo
            if (ai == null) continue
            val app = AppViewModel.fromAppInfo(pm, pi, ai)

            emitter.onNext(app)
        }

        emitter.onComplete()
    }
}