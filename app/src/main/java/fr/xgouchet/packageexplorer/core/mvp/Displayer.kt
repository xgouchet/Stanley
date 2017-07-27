package fr.xgouchet.packageexplorer.core.mvp

/**
 * @author Xavier F. Gouchet
 */
interface Displayer<in T> {

    fun setLoading(isLoading: Boolean = true)

    fun setError(throwable: Throwable)

    fun setEmpty()

    fun setContent(content: T)
}

