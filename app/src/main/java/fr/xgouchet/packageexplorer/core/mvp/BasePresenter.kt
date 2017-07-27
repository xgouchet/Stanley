package fr.xgouchet.packageexplorer.core.mvp

import kotlin.properties.Delegates.notNull


/**
 * @author Xavier F. Gouchet
 */
open class BasePresenter<T : Any> : Presenter<T> {

    var displayer: Displayer<T> by notNull()

    var data: T? = null

    override fun subscribe() {
        data?.let { displayer.setContent(it) }
    }

    override fun unsubscribe() {
        // nothing to do
    }

    override fun load(force: Boolean) {
        // nothing to load
    }

}