package fr.xgouchet.packageexplorer.oss

import android.app.Activity
import android.app.Fragment
import android.content.Context
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.core.utils.ContextHolder
import fr.xgouchet.packageexplorer.details.adapter.AppInfoHeader
import fr.xgouchet.packageexplorer.details.adapter.AppInfoType
import fr.xgouchet.packageexplorer.details.adapter.AppInfoViewModel
import fr.xgouchet.packageexplorer.details.adapter.AppInfoWithSubtitleAndAction
import fr.xgouchet.packageexplorer.ui.mvp.Displayer
import fr.xgouchet.packageexplorer.ui.mvp.list.BaseListPresenter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject

class OSSPresenter(context: Context, val urlNavigator: UrlNavigator) :
    BaseListPresenter<AppInfoViewModel, OSSFragment>(null),
    ContextHolder {

    override val context: Context = context.applicationContext

    private var memoizedOSSList: List<AppInfoViewModel>? = null
    private var currentMask: Int = AppInfoType.INFO_TYPE_MISC
    private var dataSubject: BehaviorSubject<List<AppInfoViewModel>> = BehaviorSubject.create()
    private var collapseMaskSubject: BehaviorSubject<Int> =
        BehaviorSubject.createDefault(currentMask)

    init {
        val filteredList = Observable.combineLatest(
            dataSubject,
            collapseMaskSubject,
            BiFunction { list, filter ->
                return@BiFunction list
                    .filter { (it is AppInfoHeader) || (it.mask and filter) == it.mask }
                    .map {
                        if (it is AppInfoHeader) {
                            val expandedIcon = if ((it.mask and filter) == it.mask) {
                                R.drawable.ic_expand_less
                            } else {
                                R.drawable.ic_expand_more
                            }
                            return@map it.copy(expandedIcon = expandedIcon)
                        } else {
                            return@map it
                        }
                    }
            }
        )

        val disposable = filteredList
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onItemsLoaded(it) },
                { displayer?.setError(it) }
            )
    }

    override fun load(force: Boolean) {
        displayer?.let { d ->
            d.setLoading(true)
            disposable?.dispose()

            val list = memoizedOSSList
            if (list != null) {
                dataSubject.onNext(list)
            }

            disposable = Observable.create(OSSDependenciesSource(context))
                .subscribeOn(Schedulers.io())
                .toList()
                .subscribe(
                    {
                        memoizedOSSList = it
                        dataSubject.onNext(it)
                    },
                    { displayer?.setError(it) }
                )
        }
    }

    override fun onDisplayerAttached(
        displayer: Displayer<List<AppInfoViewModel>>,
        restored: Boolean
    ) {
        val activity = when (displayer) {
            is Fragment -> displayer.activity
            is androidx.fragment.app.Fragment -> displayer.activity
            is Activity -> displayer
            else -> null
        }
        if (activity != null) urlNavigator.currentActivity = activity

        super.onDisplayerAttached(displayer, restored)
    }

    override fun itemSelected(item: AppInfoViewModel) {
        if (item is AppInfoHeader) {
            currentMask = currentMask xor item.mask
            collapseMaskSubject.onNext(currentMask)
        }
    }

    fun actionTriggered(infoViewModel: AppInfoViewModel) {
        val data = (infoViewModel as? AppInfoWithSubtitleAndAction)?.actionData
        if (data is String) {
            urlNavigator.goToItemDetails(data)
        }
    }
}
