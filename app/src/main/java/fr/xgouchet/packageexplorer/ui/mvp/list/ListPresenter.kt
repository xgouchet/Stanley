package fr.xgouchet.packageexplorer.ui.mvp.list

import fr.xgouchet.packageexplorer.ui.mvp.Presenter

/**
 * @author Xavier F. Gouchet
 */
interface ListPresenter<T> : Presenter<List<T>> {

    fun itemSelected(item: T)

    fun onPermissionGranted(requestCode: Int) {}
}
