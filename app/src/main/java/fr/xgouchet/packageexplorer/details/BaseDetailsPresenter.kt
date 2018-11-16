package fr.xgouchet.packageexplorer.details

import android.app.Activity
import android.app.Fragment
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.details.adapter.AppInfoHeader
import fr.xgouchet.packageexplorer.details.adapter.AppInfoSelectable
import fr.xgouchet.packageexplorer.details.adapter.AppInfoViewModel
import fr.xgouchet.packageexplorer.ui.mvp.Displayer
import fr.xgouchet.packageexplorer.ui.mvp.Navigator
import fr.xgouchet.packageexplorer.ui.mvp.list.BaseListPresenter
import fr.xgouchet.packageexplorer.ui.mvp.list.ListDisplayer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.security.cert.X509Certificate
import android.support.v4.app.Fragment as FragmentV4

abstract class BaseDetailsPresenter<D>(navigator: Navigator<AppInfoViewModel>?,
                                       private val certificateNavigator: Navigator<X509Certificate>,
                                       val context: Context)
    : BaseListPresenter<AppInfoViewModel, D>(navigator)
        where D : ListDisplayer<AppInfoViewModel> {


    private var memoizedAppInfoList: List<AppInfoViewModel>? = null
    private var currentMask: Int = 0
    private var dataSubject: BehaviorSubject<List<AppInfoViewModel>> = BehaviorSubject.create()
    private var collapseMaskSubject: BehaviorSubject<Int> = BehaviorSubject.createDefault(currentMask)

    init {
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

    override fun onDisplayerAttached(displayer: Displayer<List<AppInfoViewModel>>, restored: Boolean) {

        val activity = when (displayer) {
            is Fragment -> displayer.activity
            is FragmentV4 -> displayer.activity
            is Activity -> displayer
            else -> null
        }
        if (activity != null) certificateNavigator.currentActivity = activity

        super.onDisplayerAttached(displayer, restored)
    }

    override fun load(force: Boolean) {
        displayer?.let {

            it.setLoading(true)
            disposable?.dispose()

            val list = memoizedAppInfoList
            if (list != null && !force) {
                dataSubject.onNext(list)
                return@let
            }

            disposable?.dispose()
            disposable = getDetails()
                    .subscribeOn(Schedulers.computation())
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

    override fun itemSelected(item: AppInfoViewModel) {
        if (item is AppInfoHeader) {
            currentMask = currentMask xor item.mask
            collapseMaskSubject.onNext(currentMask)
        } else if (item is AppInfoSelectable) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val selectedData = item.getSelectedData()

            if (selectedData != null) {
                val clip = ClipData.newPlainText(item.getLabel(), selectedData)
                clipboard.primaryClip = clip
                Toast.makeText(context, "“${selectedData}” has been copied to your clipboard", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun actionTriggerd(actionData: Any?) {
        if (actionData is X509Certificate) {
            certificateNavigator.goToItemDetails(actionData)
        }
    }

    abstract fun getDetails(): Observable<AppInfoViewModel>
}
