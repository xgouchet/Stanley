package fr.xgouchet.packageexplorer.core.mvp

import android.app.Activity
import android.view.View
import io.reactivex.disposables.Disposable

/**
 * @author Xavier F. Gouchet
 */
abstract class BaseListPresenter<T>(val navigator: Navigator<T>?,
                                    var activity: Activity) : ListPresenter<T> {


    internal var disposable: Disposable? = null

    var displayer: ListDisplayer<T>? = null


    override fun subscribe() {
        load(force = true)
    }

    override fun unsubscribe() {
        disposable?.dispose()
    }

    override fun itemSelected(item: T, transitionView: View?) {
        navigator?.goToItemDetails(item, activity, transitionView)
    }

    fun onError(e: Throwable?) {
        displayer?.let {
            it.setLoading(false)
            if (e != null) it.setError(e)
        }
    }

    fun onItemsLoaded(list: List<T>) {
        displayer?.let {
            it.setLoading(false)
            if (list.isEmpty()) {
                it.setEmpty()
            } else {
                it.setContent(list)
            }
        }
    }


}