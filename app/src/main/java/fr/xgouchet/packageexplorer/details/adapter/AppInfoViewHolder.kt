package fr.xgouchet.packageexplorer.details.adapter

import android.view.View
import fr.xgouchet.packageexplorer.databinding.ItemInfoHeaderBinding
import fr.xgouchet.packageexplorer.databinding.ItemInfoIconBinding
import fr.xgouchet.packageexplorer.databinding.ItemInfoSimpleBinding
import fr.xgouchet.packageexplorer.databinding.ItemInfoSubtitleActionBinding
import fr.xgouchet.packageexplorer.databinding.ItemInfoSubtitleBinding
import fr.xgouchet.packageexplorer.databinding.ItemInfoSubtitleIconBinding
import fr.xgouchet.packageexplorer.ui.adapter.BaseViewHolder
import io.reactivex.functions.BiConsumer
import io.reactivex.functions.Consumer

/**
 * @author Xavier F. Gouchet
 */
abstract class AppInfoViewHolder(
        itemView: View,
        listener: BiConsumer<AppInfoViewModel, View?>?,
        secondaryActionListener: Consumer<AppInfoViewModel>? = null
) : BaseViewHolder<AppInfoViewModel>(
        itemView = itemView,
        selectedListener = listener,
        secondaryActionListener = secondaryActionListener
)


class AppInfoHeaderViewHolder(listener: BiConsumer<AppInfoViewModel, View?>?,
                              val binding: ItemInfoHeaderBinding)
    : AppInfoViewHolder(binding.root, listener) {

    init {
        binding.root.setOnClickListener {
            fireSelected()
        }
    }

    override fun onBindItem(item: AppInfoViewModel) {
        binding.info = item as AppInfoHeader
        binding.executePendingBindings()
    }

}

class AppInfoSimpleViewHolder(listener: BiConsumer<AppInfoViewModel, View?>?,
                              val binding: ItemInfoSimpleBinding)
    : AppInfoViewHolder(binding.root, listener) {

    init {
        binding.root.setOnClickListener {
            fireSelected()
        }
    }

    override fun onBindItem(item: AppInfoViewModel) {
        binding.info = item as AppInfoSimple
        binding.executePendingBindings()
    }

}

class AppInfoWithIconViewHolder(listener: BiConsumer<AppInfoViewModel, View?>?,
                                val binding: ItemInfoIconBinding)
    : AppInfoViewHolder(binding.root, listener) {

    init {
        binding.root.setOnClickListener {
            fireSelected()
        }
    }

    override fun onBindItem(item: AppInfoViewModel) {
        binding.info = item as AppInfoWithIcon
        binding.executePendingBindings()
    }

}

class AppInfoWithSubtitleViewHolder(listener: BiConsumer<AppInfoViewModel, View?>?,
                                    val binding: ItemInfoSubtitleBinding)
    : AppInfoViewHolder(binding.root, listener) {

    init {
        binding.root.setOnClickListener {
            fireSelected()
        }
    }

    override fun onBindItem(item: AppInfoViewModel) {
        binding.info = item as AppInfoWithSubtitle
        binding.executePendingBindings()
    }

}

class AppInfoWithSubtitleAndIconViewHolder(listener: BiConsumer<AppInfoViewModel, View?>?,
                                           val binding: ItemInfoSubtitleIconBinding)
    : AppInfoViewHolder(binding.root, listener) {

    init {
        binding.root.setOnClickListener {
            fireSelected()
        }
    }

    override fun onBindItem(item: AppInfoViewModel) {
        binding.info = item as AppInfoWithSubtitleAndIcon
        binding.executePendingBindings()
    }

}

class AppInfoWithSubtitleAndActionViewHolder(listener: BiConsumer<AppInfoViewModel, View?>?,
                                             actionListener: Consumer<AppInfoViewModel>?,
                                             val binding: ItemInfoSubtitleActionBinding)
    : AppInfoViewHolder(binding.root, listener, actionListener) {

    init {
        binding.root.setOnClickListener {
            fireSelected()
        }
        binding.action.setOnClickListener {
            fireSecondaryAction()
        }
    }

    override fun onBindItem(item: AppInfoViewModel) {
        binding.info = item as AppInfoWithSubtitleAndAction
        binding.executePendingBindings()
    }

}
