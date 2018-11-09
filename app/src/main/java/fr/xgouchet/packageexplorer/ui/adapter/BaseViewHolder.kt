package fr.xgouchet.packageexplorer.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import io.reactivex.functions.BiConsumer

/**
 * @author Xavier Gouchet
 */
abstract class BaseViewHolder<T>(val listener: BiConsumer<T, View?>?, itemView: View) : RecyclerView.ViewHolder(itemView) {

    var item: T? = null


    fun bindItem(item: T) {
        this.item = item
        onBindItem(item)
    }

    protected fun fireSelected() {
        item?.let {
            listener?.accept(it, getTransitionView())
        }
    }

    protected open fun getTransitionView(): View? = null

    protected abstract fun onBindItem(item: T)
}
