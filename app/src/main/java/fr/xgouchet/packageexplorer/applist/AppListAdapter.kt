package fr.xgouchet.packageexplorer.applist

import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.xgouchet.packageexplorer.ui.adapter.BaseBindingAdapter
import fr.xgouchet.packageexplorer.ui.adapter.BaseViewHolder
import fr.xgouchet.packageexplorer.databinding.ItemAppBinding
import io.reactivex.functions.BiConsumer


/**
 * @author Xavier F. Gouchet
 */
class AppAdapter(val listener: BiConsumer<AppViewModel, View?>?)
    : BaseBindingAdapter<AppViewModel, ItemAppBinding>() {

    override fun inflateDataBinding(layoutInflater: LayoutInflater, parent: ViewGroup, viewType: Int): ItemAppBinding {
        return ItemAppBinding.inflate(layoutInflater, parent, false)
    }

    override fun instantiateViewHolder(binding: ItemAppBinding, viewType: Int): BaseViewHolder<AppViewModel> {
        return AppViewHolder(binding, listener)
    }

    override fun getDiffHelper(oldContent: List<AppViewModel>, newContent: List<AppViewModel>): DiffUtil.Callback? {
        return AppDiffUtilCallback(oldContent, newContent)
    }
}
