package fr.xgouchet.packageexplorer.certificate

import android.content.Context
import fr.xgouchet.packageexplorer.applist.AppListNavigator
import fr.xgouchet.packageexplorer.applist.AppListSource
import fr.xgouchet.packageexplorer.applist.AppViewModel
import fr.xgouchet.packageexplorer.applist.sort.AppSort
import fr.xgouchet.packageexplorer.core.utils.ContextHolder
import fr.xgouchet.packageexplorer.core.utils.Notebook.page
import fr.xgouchet.packageexplorer.core.utils.getMainActivities
import fr.xgouchet.packageexplorer.core.utils.getResolvedIntent
import fr.xgouchet.packageexplorer.ui.mvp.list.BaseListPresenter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.security.cert.X509Certificate

class CertificateAppListPresenter(
    context: Context,
    private val certificate: X509Certificate
) :
    BaseListPresenter<AppViewModel, CertificateAppListFragment>(AppListNavigator()),
    ContextHolder {

    companion object {
        const val KEY_SORT = "sort"
    }

    override val context: Context = context.applicationContext

    private var currentSort: AppSort by page(KEY_SORT, AppSort.TITLE)

    private var memoizedAppList: List<AppViewModel>? = null

    private var dataSubject: BehaviorSubject<List<AppViewModel>> = BehaviorSubject.create()
    private var sortSubject: BehaviorSubject<Comparator<AppViewModel>> = BehaviorSubject.create()

    private var loadingDisposable: Disposable? = null

    init {
        val filteredList = dataSubject
            .map {
                it.filter {
                    val match = it.certificates.firstOrNull { cert ->
                        cert.encoded?.contentEquals(certificate.encoded) ?: false
                    }
                    return@filter match != null
                }
            }

        val sortedList = Observable.combineLatest(
            filteredList,
            sortSubject,
            BiFunction { list, comp ->
                return@BiFunction list.sortedWith(comp)
            }
        )

        loadingDisposable = sortedList
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onItemsLoaded(it) },
                { displayer?.setError(it) }
            )

        sortSubject.onNext(currentSort.comparator)
    }

    override fun load(force: Boolean) {
        displayer?.let { d ->
            d.setLoading(true)
            disposable?.dispose()

            val list = memoizedAppList
            if (list != null && !force) {
                dataSubject.onNext(list)
                return@let
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

    fun setSort(sort: AppSort) {
        currentSort = sort
        sortSubject.onNext(currentSort.comparator)
        load()
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
}
