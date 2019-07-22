package fr.xgouchet.packageexplorer.details.apk

import android.Manifest
import android.app.Activity
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import fr.xgouchet.packageexplorer.core.utils.exportManifestFromApk
import fr.xgouchet.packageexplorer.core.utils.isFile
import fr.xgouchet.packageexplorer.details.BaseDetailsPresenter
import fr.xgouchet.packageexplorer.details.DetailsSource
import fr.xgouchet.packageexplorer.details.adapter.AppInfoType
import fr.xgouchet.packageexplorer.details.adapter.AppInfoViewModel
import fr.xgouchet.packageexplorer.details.adapter.AppInfoWithSubtitle
import fr.xgouchet.packageexplorer.ui.mvp.Navigator
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import javax.security.cert.X509Certificate


/**
 * @author Xavier F. Gouchet
 */
class ApkDetailsPresenter(activity: Activity,
                          certficateNavigator: Navigator<X509Certificate>,
                          private val uri: Uri)
    : BaseDetailsPresenter<ApkDetailsFragment>(null, certficateNavigator, activity.applicationContext) {

    private var localFilePath: String = ""
    private var exportDisposable: Disposable? = null

    override fun onDisplayerDetached() {
        super.onDisplayerDetached()
        exportDisposable?.dispose()
        exportDisposable = null
    }

    override fun load(force: Boolean) {
        if (uri.isFile()) {
            if (!hasPermission()) {
                requestPermission()
                return
            }
        }

        super.load(force)
    }


    override fun getDetails(): Observable<AppInfoViewModel> {
        return Single.create(CopyApkSource(context, uri))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .doOnSuccess { localFilePath = it }
                .flatMapObservable { path -> Observable.create(ApkDetailsSource(context, path)) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    if (it.mask == AppInfoType.INFO_TYPE_METADATA) {
                        if (it is AppInfoWithSubtitle) {
                            if (it.title == DetailsSource.PACKAGE_NAME) (displayer as ApkDetailsFragment).setPackageName(it.subtitle)
                        }
                    }
                }
    }

    private fun hasPermission(): Boolean {
        val permissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
        return permissionStatus == PermissionChecker.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        displayer?.requestStoragePermission()
    }

    fun onPermissionGranted() {
        load(true)
    }


    fun exportManifest() {
        exportDisposable = exportManifestFromApk(File(localFilePath), context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    displayer?.onManifestExported(it)
                }, {
                    displayer?.setError(it)
                })
    }
}
