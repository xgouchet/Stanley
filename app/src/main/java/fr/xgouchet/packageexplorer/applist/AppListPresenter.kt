package fr.xgouchet.packageexplorer.applist

import android.content.Context
import android.content.pm.ApplicationInfo
import fr.xgouchet.packageexplorer.applist.sort.AppSort
import fr.xgouchet.packageexplorer.core.utils.ContextHolder
import fr.xgouchet.packageexplorer.core.utils.Notebook.notebook
import fr.xgouchet.packageexplorer.ui.mvp.list.BaseListPresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

/**
 * @author Xavier F. Gouchet
 */
class AppListPresenter(context: Context)
    : BaseListPresenter<AppViewModel, AppListFragment>(AppListNavigator()),
        ContextHolder {

    companion object {
        val KEY_SORT = "sort"
        val KEY_SYSTEM_APPS_VISIBLE = "system_app_visible"
    }

    override val context: Context = context.applicationContext

    private var currentSort: AppSort by notebook(KEY_SORT, AppSort.TITLE)
    private var systemAppVisible: Boolean by notebook(KEY_SYSTEM_APPS_VISIBLE, false)

    private var memoizedAppList: List<AppViewModel>? = null

    private var dataSubject: BehaviorSubject<List<AppViewModel>> = BehaviorSubject.create()
    private var sortSubject: BehaviorSubject<Comparator<AppViewModel>> = BehaviorSubject.create()
    private var filterSubject: BehaviorSubject<String> = BehaviorSubject.create()
    private var systemAppVisibilitySubject: BehaviorSubject<Boolean> = BehaviorSubject.create()

    init {
        val filteredList = Observable.combineLatest(
                dataSubject,
                filterSubject,
                BiFunction<List<AppViewModel>, String, List<AppViewModel>> { list, filter ->
                    val lowerCaseFilter = filter.toLowerCase()
                    return@BiFunction list.filter {
                        if (filter.isEmpty()) {
                            return@filter true
                        } else {
                            return@filter it.title.toLowerCase().contains(lowerCaseFilter) || it.packageName.toLowerCase().contains(lowerCaseFilter)
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
                BiFunction<List<AppViewModel>, Comparator<AppViewModel>, List<AppViewModel>> { list, comp ->
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
            it.setLoading(true)
            disposable?.dispose()

            val list = memoizedAppList
            if (list != null && !force) {
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
                            displayer?.setError(t)
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

    fun areSystemAppsVisible(): Boolean = systemAppVisible
}