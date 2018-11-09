package fr.xgouchet.packageexplorer.details.app

import android.app.Activity
import android.content.Intent
import fr.xgouchet.packageexplorer.core.utils.*
import fr.xgouchet.packageexplorer.details.BaseDetailsPresenter
import fr.xgouchet.packageexplorer.details.adapter.AppInfoViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.util.zip.ZipException
import javax.xml.parsers.ParserConfigurationException
import javax.xml.transform.TransformerException


/**
 * @author Xavier F. Gouchet
 */
class AppDetailsPresenter(activity: Activity,
                          val packageName: String,
                          val isSystemApp: Boolean)
    : BaseDetailsPresenter<AppDetailsFragment>(null, activity.applicationContext) {

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

        when {
            resolvedInfos.isEmpty() -> return
            resolvedInfos.size == 1 -> {
                val intent = getResolvedIntent(resolvedInfos[0])
                context.startActivity(intent)
            }
            else -> displayer?.promptActivity(resolvedInfos)
        }
    }

    fun exportManifest() {
        try {
            val packageInfo = context.packageManager.getPackageInfo(packageName, 0)
            exportDisposable = exportManifestFromPackage(packageInfo, context)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        displayer?.onManifestExported(it)
                    }
        } catch (e: ZipException) {
            displayer?.setError(e)
        } catch (e: IOException) {
            displayer?.setError(e)
        } catch (e: TransformerException) {
            displayer?.setError(e)
        } catch (e: ParserConfigurationException) {
            displayer?.setError(e)
        }

    }
}