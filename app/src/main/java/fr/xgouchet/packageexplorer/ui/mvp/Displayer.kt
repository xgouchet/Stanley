package fr.xgouchet.packageexplorer.ui.mvp

import android.app.Activity

/**
 * @author Xavier F. Gouchet
 */
interface Displayer<T> {

    fun setPresenter(presenter : Presenter<T>)

    fun setLoading(isLoading: Boolean = true)

    fun setError(throwable: Throwable)

    fun setEmpty()

    fun setContent(content: T)
}

