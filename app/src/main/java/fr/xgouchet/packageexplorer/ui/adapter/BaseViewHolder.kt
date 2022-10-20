package fr.xgouchet.packageexplorer.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.functions.BiConsumer
import io.reactivex.rxjava3.functions.Consumer
import java.util.Optional

/**
 * @author Xavier Gouchet
 */
abstract class BaseViewHolder<T : Any>(
    itemView: View,
    val selectedListener: BiConsumer<T, Optional<View>>? = null,
    val secondaryActionListener: Consumer<T>? = null
) : RecyclerView.ViewHolder(itemView) {

    var boundItem: T? = null

    fun bindItem(item: T) {
        this.boundItem = item
        onBindItem(item)
    }

    protected fun fireSelected() {
        boundItem?.let { item ->
            selectedListener?.accept(item, getTransitionView())
        }
    }

    protected fun fireSecondaryAction() {
        boundItem?.let {
            secondaryActionListener?.accept(it)
        }
    }

    protected open fun getTransitionView(): Optional<View> = Optional.empty()

    protected abstract fun onBindItem(item: T)
}
