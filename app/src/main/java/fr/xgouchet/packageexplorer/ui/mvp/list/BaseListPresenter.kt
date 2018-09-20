package fr.xgouchet.packageexplorer.ui.mvp.list

import android.app.Activity
import android.app.Fragment
import fr.xgouchet.packageexplorer.ui.mvp.Displayer
import fr.xgouchet.packageexplorer.ui.mvp.Navigator
import io.reactivex.disposables.Disposable
import android.support.v4.app.Fragment as FragmentV4

/**
 * @author Xavier F. Gouchet
 */
abstract class BaseListPresenter<T, D>(val navigator: Navigator<T>?)
    : ListPresenter<T>
        where D : ListDisplayer<T> {

    internal var disposable: Disposable? = null

    var displayer: D? = null
        private set

    // region Presenter

    final override fun onDisplayerAttached(displayer: Displayer<List<T>>, restored: Boolean) {
        require(displayer is ListDisplayer<T>) { "ListPresenter requires a ListDisplayer" }

        @Suppress("UNCHECKED_CAST")
        this.displayer = displayer as D
        displayer.setPresenter(this)

        navigator?.let {
            when (displayer) {
                is Fragment -> it.currentActivity = displayer.activity
                is FragmentV4 -> it.currentActivity = displayer.activity!!
                is Activity -> it.currentActivity = displayer
            }
        }

        load(force = !restored)
    }

    override fun onDisplayerDetached() {
        disposable?.dispose()
        displayer = null
    }

    // endregion

    // region ListPresenter

    override fun itemSelected(item: T) {
        navigator?.goToItemDetails(item)
    }

    // endregion

    // region Callbacks

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

    // endregion
}