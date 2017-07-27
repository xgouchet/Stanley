package fr.xgouchet.packageexplorer.core.mvp

/**
 * @author Xavier F. Gouchet
 */
interface Presenter<T> {

    fun subscribe()

    fun unsubscribe()

    fun load(force: Boolean = false)
}