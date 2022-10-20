package fr.xgouchet.packageexplorer.ui.mvp

interface Displayer<T> {

    fun setPresenter(presenter: Presenter<T>)

    fun setLoading(isLoading: Boolean = true)

    fun setError(throwable: Throwable)

    fun setEmpty()

    fun setContent(content: T)
}
