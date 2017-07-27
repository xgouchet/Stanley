package fr.xgouchet.packageexplorer.appdetails

import android.app.Activity
import android.content.Context
import android.view.View
import fr.xgouchet.packageexplorer.applist.AppListPresenter
import fr.xgouchet.packageexplorer.core.mvp.BaseListPresenter
import fr.xgouchet.packageexplorer.core.mvp.ListDisplayer
import fr.xgouchet.packageexplorer.core.utils.ContextHolder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import mu.KLogging

/**
 * @author Xavier F. Gouchet
 */
class AppDetailsPresenter(listDisplayer: ListDisplayer<AppInfoViewModel>?,
                          activity: Activity,
                          val packageName: String)
    : BaseListPresenter<AppInfoViewModel>(null, activity),
        ContextHolder {

    companion object : KLogging() {

    }

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
                BiFunction <List<AppInfoViewModel>, Int, List<AppInfoViewModel>> { list, filter ->
                    return@BiFunction list.filter {
                        if (it is AppInfoHeader) {
                            return@filter true
                        } else {
                            return@filter (it.mask and filter) == it.mask
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

            if (memoizedAppInfoList != null) {
                dataSubject.onNext(memoizedAppInfoList)
                return@let
            }

            disposable?.dispose()
            disposable = Observable.create(AppDetailsSource(context, packageName))
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.computation())
                    .toList()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { list, t ->
                        if (list != null) {
                            memoizedAppInfoList = list
                            dataSubject.onNext(list)
                        } else {
                            AppListPresenter.logger.error { "Error fetching the list of apps : ${t.message}" }
                        }
                    }
        }
    }



    override fun itemSelected(item: AppInfoViewModel, transitionView: View?) {
        if (item is AppInfoHeader) {
            currentMask = currentMask xor item.mask
            collapseMaskSubject.onNext(currentMask)
        }
    }
}