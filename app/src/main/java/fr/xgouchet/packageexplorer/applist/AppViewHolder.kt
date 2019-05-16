package fr.xgouchet.packageexplorer.applist

import android.view.View
import fr.xgouchet.packageexplorer.databinding.ItemAppBinding
import fr.xgouchet.packageexplorer.ui.adapter.BaseViewHolder
import io.reactivex.functions.BiConsumer
import io.reactivex.functions.Consumer

class AppViewHolder(
        val binding: ItemAppBinding,
        listener: BiConsumer<AppViewModel, View?>?,
        secondaryActionListener: Consumer<AppViewModel>?
) : BaseViewHolder<AppViewModel>(
        itemView = binding.root,
        selectedListener = listener,
        secondaryActionListener = secondaryActionListener
) {

    init {
        binding.root.setOnClickListener { fireSelected() }
        binding.actionAppLaunch.setOnClickListener { fireSecondaryAction() }
    }

    override fun onBindItem(item: AppViewModel) {
        binding.app = item
        binding.executePendingBindings()
    }

    override fun getTransitionView(): View? {
        return binding.iconApp
    }
}
