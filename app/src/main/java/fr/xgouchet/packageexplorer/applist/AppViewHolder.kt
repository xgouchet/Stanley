package fr.xgouchet.packageexplorer.applist

import android.view.View
import fr.xgouchet.packageexplorer.ui.adapter.BaseViewHolder
import fr.xgouchet.packageexplorer.databinding.ItemAppBinding
import io.reactivex.functions.BiConsumer

class AppViewHolder(val binding: ItemAppBinding, listener: BiConsumer<AppViewModel, View?>?)
    : BaseViewHolder<AppViewModel>(listener, binding.root) {

    init {
        binding.root.setOnClickListener { fireSelected() }
    }

    override fun onBindItem(item: AppViewModel) {
        binding.app = item
        binding.executePendingBindings()
    }

    override fun getTransitionView(): View? {
        return binding.iconApp
    }
}