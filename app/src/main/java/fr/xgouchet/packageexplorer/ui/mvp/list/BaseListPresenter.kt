package fr.xgouchet.packageexplorer.ui.mvp.list

import android.app.Activity
import android.app.Fragment
import android.support.v4.app.Fragment as FragmentV4
import fr.xgouchet.packageexplorer.ui.mvp.Displayer
import fr.xgouchet.packageexplorer.ui.mvp.Navigator
import io.reactivex.disposables.Disposable

/**
 * @author Xavier F. Gouchet
 */
abstract class BaseListPresenter<T>(val navigator: Navigator<T>?)
    : ListPresenter<T> {

    internal var disposable: Disposable? = null

    var displayer: ListDisplayer<T>? = null
    // TODO private set

    // region Presenter

    final override fun onDisplayerAttached(displayer: Displayer<List<T>>, restored: Boolean) {
        require(displayer is ListDisplayer<T>, { "ListPresenter requires a ListDisplayer" })

        this.displayer = displayer as ListDisplayer<T>
        displayer.setPresenter(this)

        navigator?.let {
            if (displayer is Fragment) {
                it.currentActivity = displayer.activity
            } else if (displayer is FragmentV4) {
                it.currentActivity = displayer.activity
            } else if (displayer is Activity) {
                it.currentActivity = displayer
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