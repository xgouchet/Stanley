package fr.xgouchet.packageexplorer.core.mvp

import android.view.View


/**
 * @author Xavier F. Gouchet
 */
interface ListPresenter<T> : Presenter<List<T>> {

    fun itemSelected(item: T, transitionView: View? = null)

}