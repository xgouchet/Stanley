package fr.xgouchet.packageexplorer.details.apk

import android.app.Activity
import android.net.Uri
import fr.xgouchet.packageexplorer.details.AppInfoType
import fr.xgouchet.packageexplorer.details.AppInfoViewModel
import fr.xgouchet.packageexplorer.details.AppInfoWithSubtitle
import fr.xgouchet.packageexplorer.details.BaseDetailsPresenter
import fr.xgouchet.packageexplorer.details.DetailsSource
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * @author Xavier F. Gouchet
 */
class ApkDetailsPresenter(activity: Activity,
                          val uri: Uri)
    : BaseDetailsPresenter(null, activity.applicationContext) {

    override fun getDetails(): Observable<AppInfoViewModel> {
        return Single.create(CopyApkSource(context, uri))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
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

}