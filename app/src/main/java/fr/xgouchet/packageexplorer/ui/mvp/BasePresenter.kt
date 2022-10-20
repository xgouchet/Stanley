package fr.xgouchet.packageexplorer.ui.mvp

open class BasePresenter<T : Any> : Presenter<T> {

    var displayer: Displayer<T>? = null
        private set

    var data: T? = null

    override fun onDisplayerAttached(displayer: Displayer<T>, restored: Boolean) {
        this.displayer = displayer
        data?.let { displayer.setContent(it) }
    }

    override fun onDisplayerDetached() {
        displayer = null
    }

    override fun load(force: Boolean) {
        // nothing to load
    }
}
