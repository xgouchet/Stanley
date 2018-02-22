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
abstract class AppInfoViewHolder(listener: BiConsumer<AppInfoViewModel, View?>?,
                                 itemView: View)
    : BaseViewHolder<AppInfoViewModel>(listener, itemView)


class AppInfoHeaderViewHolder(listener: BiConsumer<AppInfoViewModel, View?>?,
                              val binding: ItemInfoHeaderBinding)
    : AppInfoViewHolder(listener, binding.root) {

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
    : AppInfoViewHolder(listener, binding.root) {

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
    : AppInfoViewHolder(listener, binding.root) {

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
    : AppInfoViewHolder(listener, binding.root) {

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
    : AppInfoViewHolder(listener, binding.root) {

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
                                             actionListener: Consumer<Any?>?,
                                             val binding: ItemInfoSubtitleActionBinding)
    : AppInfoViewHolder(listener, binding.root) {

    init {
        binding.root.setOnClickListener {
            fireSelected()
        }
        binding.action.setOnClickListener {
            actionListener?.accept(binding.info?.actionData)
        }
    }

    override fun onBindItem(item: AppInfoViewModel) {
        binding.info = item as AppInfoWithSubtitleAndAction
        binding.executePendingBindings()
    }

}