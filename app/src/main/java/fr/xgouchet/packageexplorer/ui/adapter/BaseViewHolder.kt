package fr.xgouchet.packageexplorer.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import io.reactivex.functions.BiConsumer
import io.reactivex.functions.Consumer

/**
 * @author Xavier Gouchet
 */
abstract class BaseViewHolder<T>(
        itemView: View,
        val selectedListener: BiConsumer<T, View?>? = null,
        val secondaryActionListener: Consumer<T>? = null
) : RecyclerView.ViewHolder(itemView) {

    var item: T? = null


    fun bindItem(item: T) {
        this.item = item
        onBindItem(item)
    }

    protected fun fireSelected() {
        item?.let {
            selectedListener?.accept(it, getTransitionView())
        }
    }

    protected fun fireSecondaryAction() {
        item?.let {
            secondaryActionListener?.accept(it)
        }
    }

    protected open fun getTransitionView(): View? = null

    protected abstract fun onBindItem(item: T)
}
