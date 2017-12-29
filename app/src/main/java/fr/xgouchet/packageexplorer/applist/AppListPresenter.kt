package fr.xgouchet.packageexplorer.applist

import android.app.Activity
import android.content.Context
import android.content.pm.ApplicationInfo
import fr.xgouchet.packageexplorer.applist.sort.AppSort
import fr.xgouchet.packageexplorer.core.mvp.BaseListPresenter
import fr.xgouchet.packageexplorer.core.mvp.ListDisplayer
import fr.xgouchet.packageexplorer.core.utils.ContextHolder
import fr.xgouchet.packageexplorer.core.utils.Notebook.notebook
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import mu.KLogging

/**
 * @author Xavier F. Gouchet
 */
class AppListPresenter(initView: ListDisplayer<AppViewModel>?,
                       activity: Activity)
    : BaseListPresenter<AppViewModel>(AppListNavigator(), activity),
        ContextHolder {

    companion object : KLogging() {
        val KEY_SORT = "sort"
        val KEY_SYSTEM_APPS_VISIBLE = "system_app_visible"
    }

    override val context: Context = activity.applicationContext

    private var currentSort: AppSort by notebook(KEY_SORT, AppSort.TITLE)
    private var systemAppVisible: Boolean by notebook(KEY_SYSTEM_APPS_VISIBLE, false)

    private var memoizedAppList: List<AppViewModel>? = null

    private var dataSubject: BehaviorSubject<List<AppViewModel>> = BehaviorSubject.create()
    private var sortSubject: BehaviorSubject<Comparator<AppViewModel>> = BehaviorSubject.create()
    private var filterSubject: BehaviorSubject<String> = BehaviorSubject.create()
    private var systemAppVisibilitySubject: BehaviorSubject<Boolean> = BehaviorSubject.create()

    init {
        displayer = initView

        val filteredList = Observable.combineLatest(
                dataSubject,
                filterSubject,
                BiFunction <List<AppViewModel>, String, List<AppViewModel>> { list, filter ->
                    return@BiFunction list.filter {
                        if (filter.isEmpty()) {
                            return@filter true
                        } else {
                            return@filter it.title.contains(filter) || it.packageName.contains(filter)
                        }
                    }
                })

        val systemAppFilteredList = Observable.combineLatest(
                filteredList,
                systemAppVisibilitySubject,
                BiFunction<List<AppViewModel>, Boolean, List<AppViewModel>> { list, system ->
                    return@BiFunction list.filter {
                        if (system) {
                            return@filter true
                        } else {
                            return@filter (it.flags and ApplicationInfo.FLAG_SYSTEM) == 0
                        }
                    }
                }
        )

        val sortedList = Observable.combineLatest(
                systemAppFilteredList,
                sortSubject,
                BiFunction<List<AppViewModel>, Comparator<AppViewModel>, List<AppViewModel>> {
                    list, comp ->
                    return@BiFunction list.sortedWith(comp)
                })

        sortedList
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { list -> onItemsLoaded(list) }


        sortSubject.onNext(currentSort.comparator)
        filterSubject.onNext("")
        systemAppVisibilitySubject.onNext(systemAppVisible)
    }

    override fun load(force: Boolean) {
        displayer?.let {
            if (!force) {
                it.setLoading(false)
                return@let
            }

            it.setLoading(true)
            disposable?.dispose()

            val list = memoizedAppList
            if (list != null){
                dataSubject.onNext(list)
                return@let
            }

            disposable = Observable.create(AppListSource(context))
                    .subscribeOn(Schedulers.io())
                    .toList()
                    .subscribe { list, t ->
                        if (list != null) {
                            memoizedAppList = list
                            dataSubject.onNext(list)
                        } else {
                            logger.error { "Error fetching the list of apps : ${t.message}" }
                        }
                    }
        }

    }


    fun setSort(sort: AppSort) {
        currentSort = sort
        sortSubject.onNext(currentSort.comparator)
        load()
    }

    fun setFilter(filter: String) {
        filterSubject.onNext(filter)
    }

    fun setSystemAppsVisible(visible: Boolean) {
        systemAppVisible = visible
        systemAppVisibilitySubject.onNext(visible)
    }

    fun areSystemAppsVisible() : Boolean = systemAppVisible
}