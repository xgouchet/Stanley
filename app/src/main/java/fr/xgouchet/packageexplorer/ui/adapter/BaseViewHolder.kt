package fr.xgouchet.packageexplorer.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
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

    var boundItem: T? = null

    fun bindItem(item: T) {
        this.boundItem = item
        onBindItem(item)
    }

    protected fun fireSelected() {
        boundItem?.let {
            selectedListener?.accept(it, getTransitionView())
        }
    }

    protected fun fireSecondaryAction() {
        boundItem?.let {
            secondaryActionListener?.accept(it)
        }
    }

    protected open fun getTransitionView(): View? = null

    protected abstract fun onBindItem(item: T)
}
