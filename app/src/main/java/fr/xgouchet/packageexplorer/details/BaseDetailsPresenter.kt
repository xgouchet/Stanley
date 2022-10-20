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
import fr.xgouchet.packageexplorer.details.adapter.AppInfoWithSubtitleAndAction
import fr.xgouchet.packageexplorer.ui.mvp.Displayer
import fr.xgouchet.packageexplorer.ui.mvp.Navigator
import fr.xgouchet.packageexplorer.ui.mvp.list.BaseListPresenter
import fr.xgouchet.packageexplorer.ui.mvp.list.ListDisplayer
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.security.cert.X509Certificate
import androidx.fragment.app.Fragment as FragmentX

abstract class BaseDetailsPresenter<D>(
    navigator: Navigator<AppInfoViewModel>?,
    private val certificateNavigator: Navigator<X509Certificate>,
    val context: Context
) :
    BaseListPresenter<AppInfoViewModel, D>(navigator)
    where D : ListDisplayer<AppInfoViewModel> {

    private var memoizedAppInfoList: List<AppInfoViewModel>? = null
    private var currentMask: Int = 0
    private var dataSubject: BehaviorSubject<List<AppInfoViewModel>> = BehaviorSubject.create()
    private var collapseMaskSubject: BehaviorSubject<Int> =
        BehaviorSubject.createDefault(currentMask)

    init {
        val filteredList = Observable.combineLatest(
            dataSubject,
            collapseMaskSubject,
            BiFunction<List<AppInfoViewModel>, Int, List<AppInfoViewModel>> { list, filter ->
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
            })

        val disposable = filteredList
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onItemsLoaded(it) },
                { displayer?.setError(it) }
            )
    }

    override fun onDisplayerAttached(
        displayer: Displayer<List<AppInfoViewModel>>,
        restored: Boolean
    ) {

        val activity = when (displayer) {
            is Fragment -> displayer.activity
            is FragmentX -> displayer.activity
            is Activity -> displayer
            else -> null
        }
        if (activity != null) certificateNavigator.currentActivity = activity

        super.onDisplayerAttached(displayer, restored)
    }

    override fun load(force: Boolean) {
        displayer?.let { d ->
            d.setLoading(true)
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
                .subscribe(
                    {
                        memoizedAppInfoList = it
                        dataSubject.onNext(it)
                    },
                    { displayer?.setError(it) }
                )
        }
    }

    override fun itemSelected(item: AppInfoViewModel) {
        if (item is AppInfoHeader) {
            currentMask = currentMask xor item.mask
            collapseMaskSubject.onNext(currentMask)
        } else if (item is AppInfoSelectable) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val selectedData = item.getClipData()

            if (selectedData != null) {
                val clip = ClipData.newPlainText(item.getClipLabel(), selectedData)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(
                    context,
                    "“$selectedData” has been copied to your clipboard",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun actionTriggered(infoViewModel: AppInfoViewModel) {
        val data = (infoViewModel as? AppInfoWithSubtitleAndAction)?.actionData
        if (data is X509Certificate) {
            certificateNavigator.goToItemDetails(data)
        }
    }

    abstract fun getDetails(): Observable<AppInfoViewModel>
}
