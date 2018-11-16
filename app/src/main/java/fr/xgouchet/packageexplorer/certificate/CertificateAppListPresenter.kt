package fr.xgouchet.packageexplorer.applist

import android.content.Context
import fr.xgouchet.packageexplorer.applist.sort.AppSort
import fr.xgouchet.packageexplorer.certificate.CertificateAppListFragment
import fr.xgouchet.packageexplorer.core.utils.ContextHolder
import fr.xgouchet.packageexplorer.core.utils.Notebook.notebook
import fr.xgouchet.packageexplorer.ui.mvp.list.BaseListPresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.security.cert.X509Certificate

/**
 * @author Xavier F. Gouchet
 */
class CertificateAppListPresenter(context: Context,
                                  val certificate: X509Certificate)
    : BaseListPresenter<AppViewModel, CertificateAppListFragment>(AppListNavigator()),
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

    init {
        val filteredList = dataSubject
                .map {
                    it.filter {
                        val match = it.certificates.firstOrNull { cert -> cert.encoded.contentEquals(certificate.encoded) }
                        return@filter match != null
                    }
                }


        val sortedList = Observable.combineLatest(
                filteredList,
                sortSubject,
                BiFunction<List<AppViewModel>, Comparator<AppViewModel>, List<AppViewModel>> { list, comp ->
                    return@BiFunction list.sortedWith(comp)
                })

        val disposable = sortedList
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

}
