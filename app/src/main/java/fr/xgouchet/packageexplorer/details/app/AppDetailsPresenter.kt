package fr.xgouchet.packageexplorer.details.app

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.core.mvp.BaseListPresenter
import fr.xgouchet.packageexplorer.core.mvp.ListDisplayer
import fr.xgouchet.packageexplorer.core.utils.ContextHolder
import fr.xgouchet.packageexplorer.core.utils.applicationInfoIntent
import fr.xgouchet.packageexplorer.core.utils.applicationPlayStoreIntent
import fr.xgouchet.packageexplorer.core.utils.uninstallPackageIntent
import fr.xgouchet.packageexplorer.details.AppInfoHeader
import fr.xgouchet.packageexplorer.details.AppInfoSelectable
import fr.xgouchet.packageexplorer.details.AppInfoViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject


/**
 * @author Xavier F. Gouchet
 */
class AppDetailsPresenter(listDisplayer: ListDisplayer<AppInfoViewModel>?,
                          activity: Activity,
                          val packageName: String,
                          val isSystemApp: Boolean)
    : BaseListPresenter<AppInfoViewModel>(null, activity),
        ContextHolder {

    override val context: Context = activity.applicationContext

    private var memoizedAppInfoList: List<AppInfoViewModel>? = null
    private var currentMask: Int = 0

    private var dataSubject: BehaviorSubject<List<AppInfoViewModel>> = BehaviorSubject.create()
    private var collapseMaskSubject: BehaviorSubject<Int> = BehaviorSubject.createDefault(currentMask)

    init {
        displayer = listDisplayer

        val filteredList = Observable.combineLatest(
                dataSubject,
                collapseMaskSubject,
                BiFunction<List<AppInfoViewModel>, Int, List<AppInfoViewModel>> { list, filter ->
                    return@BiFunction list
                            .filter { (it is AppInfoHeader) || (it.mask and filter) == it.mask }
                            .map {
                                if (it is AppInfoHeader) {
                                    return@map it.copy(expandedIcon = if ((it.mask and filter) == it.mask) R.drawable.ic_expand_less else R.drawable.ic_expand_more)
                                } else {
                                    return@map it
                                }
                            }
                })

        filteredList
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { list -> onItemsLoaded(list) }
    }

    override fun load(force: Boolean) {
        displayer?.let {

            if (!force) {
                it.setLoading(false)
                return@let
            }

            it.setLoading(true)
            disposable?.dispose()

            val list = memoizedAppInfoList
            if (list != null) {
                dataSubject.onNext(list)
                return@let
            }

            disposable?.dispose()
            disposable = Observable.create(AppDetailsSource(context, packageName))
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.computation())
                    .toList()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { l, t ->
                        if (l != null) {
                            memoizedAppInfoList = l
                            dataSubject.onNext(l)
                        } else {
                            displayer?.setError(t)
                        }
                    }
        }
    }


    override fun itemSelected(item: AppInfoViewModel, transitionView: View?) {
        if (item is AppInfoHeader) {
            currentMask = currentMask xor item.mask
            collapseMaskSubject.onNext(currentMask)
        } else if (item is AppInfoSelectable) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val selectedData = item.getSelectedData()

            if (selectedData != null) {
                val clip = ClipData.newPlainText(item.getLabel(), selectedData)
                clipboard.primaryClip = clip
                Toast.makeText(context, "“${selectedData}” has been copied to your clipbaord", Toast.LENGTH_LONG).show()
            }
        }
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