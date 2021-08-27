package fr.xgouchet.packageexplorer.applist

import android.Manifest
import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import fr.xgouchet.packageexplorer.applist.sort.AppSort
import fr.xgouchet.packageexplorer.core.utils.ContextHolder
import fr.xgouchet.packageexplorer.core.utils.Notebook.notebook
import fr.xgouchet.packageexplorer.core.utils.getMainActivities
import fr.xgouchet.packageexplorer.core.utils.getResolvedIntent
import fr.xgouchet.packageexplorer.ui.mvp.list.BaseListPresenter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject

/**
 * @author Xavier F. Gouchet
 */
class AppListPresenter(context: Context) :
        BaseListPresenter<AppViewModel, AppListFragment>(AppListNavigator()),
        ContextHolder {

    override val context: Context = context.applicationContext

    private var currentSort: AppSort by notebook(KEY_SORT, AppSort.TITLE)
    private var systemAppVisible: Boolean by notebook(KEY_SYSTEM_APPS_VISIBLE, false)

    private var memoizedAppList: List<AppViewModel>? = null

    private var dataSubject: BehaviorSubject<List<AppViewModel>> = BehaviorSubject.create()
    private var sortSubject: BehaviorSubject<Comparator<AppViewModel>> = BehaviorSubject.create()
    private var filterSubject: BehaviorSubject<String> = BehaviorSubject.create()
    private var systemAppVisibilitySubject: BehaviorSubject<Boolean> = BehaviorSubject.create()

    private var loadingDisposable: Disposable? = null

    init {
        val filteredList = Observable.combineLatest(
                dataSubject,
                filterSubject,
                BiFunction { list, filter ->
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
                BiFunction { list, comp ->
                    return@BiFunction list.sortedWith(comp)
                })

        loadingDisposable = sortedList
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ onItemsLoaded(it) }, { displayer?.setError(it) })

        sortSubject.onNext(currentSort.comparator)
        filterSubject.onNext("")
        systemAppVisibilitySubject.onNext(systemAppVisible)
    }

    override fun load(force: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!hasPermission()) {
                requestPermission()
                return
            }
        }

        loadWithPermission()
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

    override fun itemSelected(item: AppViewModel) {
        navigator?.goToItemDetails(item)
    }

    fun openApplication(packageName: String) {
        val resolvedInfos = getMainActivities(context, packageName)

        if (resolvedInfos.isEmpty()) {
            return
        } else if (resolvedInfos.size == 1) {
            val intent = getResolvedIntent(resolvedInfos[0])
            context.startActivity(intent)
        } else {
            displayer?.promptActivity(resolvedInfos)
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun hasPermission(): Boolean {
        val permissionStatus = ContextCompat.checkSelfPermission(context, PERMISSION_QUERY_PACKAGES)
        return permissionStatus == PermissionChecker.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun requestPermission() {
        displayer?.requestPermission(PERMISSION_QUERY_PACKAGES, PERMISSION_REQUEST_QUERY_PACKAGES)
    }

    override fun onPermissionGranted(requestCode: Int) {
        if (requestCode == PERMISSION_REQUEST_QUERY_PACKAGES) {
            loadWithPermission()
        }
    }

    private fun loadWithPermission() {
        displayer?.let { d ->
            d.setLoading(true)
            disposable?.dispose()

            val list = memoizedAppList
            if (list != null) {
                dataSubject.onNext(list)
            }

            disposable = Observable.create(AppListSource(context))
                    .subscribeOn(Schedulers.io())
                    .toList()
                    .subscribe(
                            {
                                memoizedAppList = it
                                dataSubject.onNext(it)
                            },
                            { displayer?.setError(it) }
                    )
        }
    }

    companion object {
        const val KEY_SORT = "sort"
        const val KEY_SYSTEM_APPS_VISIBLE = "system_app_visible"

        @RequiresApi(Build.VERSION_CODES.R)
        const val PERMISSION_QUERY_PACKAGES = Manifest.permission.QUERY_ALL_PACKAGES
        const val PERMISSION_REQUEST_QUERY_PACKAGES = 37
    }
}
