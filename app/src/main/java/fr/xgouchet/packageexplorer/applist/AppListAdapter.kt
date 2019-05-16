package fr.xgouchet.packageexplorer.applist

import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.xgouchet.packageexplorer.databinding.ItemAppBinding
import fr.xgouchet.packageexplorer.ui.adapter.BaseBindingAdapter
import fr.xgouchet.packageexplorer.ui.adapter.BaseViewHolder
import io.reactivex.functions.BiConsumer
import io.reactivex.functions.Consumer


/**
 * @author Xavier F. Gouchet
 */
class AppAdapter(
        val listener: BiConsumer<AppViewModel, View?>?,
        val actionListener: Consumer<AppViewModel>?
) : BaseBindingAdapter<AppViewModel, ItemAppBinding>() {

    override fun inflateDataBinding(layoutInflater: LayoutInflater, parent: ViewGroup, viewType: Int): ItemAppBinding {
        return ItemAppBinding.inflate(layoutInflater, parent, false)
    }

    override fun instantiateViewHolder(binding: ItemAppBinding, viewType: Int): BaseViewHolder<AppViewModel> {
        return AppViewHolder(binding, listener, actionListener)
    }

    override fun getDiffHelper(oldContent: List<AppViewModel>, newContent: List<AppViewModel>): DiffUtil.Callback? {
        return AppDiffUtilCallback(oldContent, newContent)
    }
}
