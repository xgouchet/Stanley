package fr.xgouchet.packageexplorer.ui.mvp

interface Presenter<T> {

    fun onDisplayerAttached(displayer: Displayer<T>, restored: Boolean)

    fun onDisplayerDetached()

    fun load(force: Boolean = false)
}
