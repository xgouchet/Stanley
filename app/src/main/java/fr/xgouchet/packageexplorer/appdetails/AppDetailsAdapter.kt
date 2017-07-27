package fr.xgouchet.packageexplorer.appdetails

import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.xgouchet.packageexplorer.appdetails.vh.AppInfoHeaderViewHolder
import fr.xgouchet.packageexplorer.appdetails.vh.AppInfoSimpleViewHolder
import fr.xgouchet.packageexplorer.appdetails.vh.AppInfoWithSubtitleAndIconViewHolder
import fr.xgouchet.packageexplorer.appdetails.vh.AppInfoWithSubtitleViewHolder
import fr.xgouchet.packageexplorer.core.mvp.BaseAdapter
import fr.xgouchet.packageexplorer.core.mvp.BaseViewHolder
import fr.xgouchet.packageexplorer.databinding.ItemInfoHeaderBinding
import fr.xgouchet.packageexplorer.databinding.ItemInfoSimpleBinding
import fr.xgouchet.packageexplorer.databinding.ItemInfoSubtitleBinding
import fr.xgouchet.packageexplorer.databinding.ItemInfoSubtitleIconBinding
import io.reactivex.functions.BiConsumer

class AppDetailsAdapter(val listener: BiConsumer<AppInfoViewModel, View?>?) : BaseAdapter<AppInfoViewModel>() {

    companion object {
        val TYPE_HEADER = 0
        val TYPE_SIMPLE = 1
        val TYPE_SUBTITLE = 2
        val TYPE_SUBTITLE_ICON = 3
    }

    override fun getDiffHelper(oldContent: List<AppInfoViewModel>, newContent: List<AppInfoViewModel>): DiffUtil.Callback? {
        return AppInfoDiffUtilCallback(oldContent, newContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<AppInfoViewModel> {
        val layoutInflater = LayoutInflater.from(parent.context)

        when (viewType) {
            TYPE_HEADER -> {
                val binding = ItemInfoHeaderBinding.inflate(layoutInflater, parent, false)
                return AppInfoHeaderViewHolder(listener, binding)
            }

            TYPE_SIMPLE -> {
                val binding = ItemInfoSimpleBinding.inflate(layoutInflater, parent, false)
                return AppInfoSimpleViewHolder(binding)
            }

            TYPE_SUBTITLE -> {
                val binding = ItemInfoSubtitleBinding.inflate(layoutInflater, parent, false)
                return AppInfoWithSubtitleViewHolder(binding)
            }

            TYPE_SUBTITLE_ICON -> {
                val binding = ItemInfoSubtitleIconBinding.inflate(layoutInflater, parent, false)
                return AppInfoWithSubtitleAndIconViewHolder(binding)
            }


            else -> throw IllegalArgumentException("Unknown view type $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        when (item) {
            is AppInfoHeader -> return TYPE_HEADER
            is AppInfoSimple -> return TYPE_SIMPLE
            is AppInfoWithSubtitle -> return TYPE_SUBTITLE
            is AppInfoWithSubtitleAndIcon -> return TYPE_SUBTITLE_ICON
            else -> throw IllegalArgumentException("Unknown type $item")
        }
    }

}