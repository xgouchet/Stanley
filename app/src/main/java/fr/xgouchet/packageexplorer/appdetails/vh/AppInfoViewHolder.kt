package fr.xgouchet.packageexplorer.appdetails.vh

import android.view.View
import fr.xgouchet.packageexplorer.appdetails.*
import fr.xgouchet.packageexplorer.core.mvp.BaseViewHolder
import fr.xgouchet.packageexplorer.databinding.ItemInfoHeaderBinding
import fr.xgouchet.packageexplorer.databinding.ItemInfoSimpleBinding
import fr.xgouchet.packageexplorer.databinding.ItemInfoSubtitleBinding
import fr.xgouchet.packageexplorer.databinding.ItemInfoSubtitleIconBinding
import io.reactivex.functions.BiConsumer

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

class AppInfoSimpleViewHolder(val binding: ItemInfoSimpleBinding)
    : AppInfoViewHolder(null, binding.root) {

    override fun onBindItem(item: AppInfoViewModel) {
        binding.info = item as AppInfoSimple
        binding.executePendingBindings()
    }

}

class AppInfoWithSubtitleViewHolder(val binding: ItemInfoSubtitleBinding)
    : AppInfoViewHolder(null, binding.root) {

    override fun onBindItem(item: AppInfoViewModel) {
        binding.info = item as AppInfoWithSubtitle
        binding.executePendingBindings()
    }

}

class AppInfoWithSubtitleAndIconViewHolder(val binding: ItemInfoSubtitleIconBinding)
    : AppInfoViewHolder(null, binding.root) {

    override fun onBindItem(item: AppInfoViewModel) {
        binding.info = item as AppInfoWithSubtitleAndIcon
        binding.executePendingBindings()
    }

}