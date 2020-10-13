package fr.xgouchet.packageexplorer.details.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.details.AppDetailsDiffUtilCallback
import fr.xgouchet.packageexplorer.ui.adapter.BaseAdapter
import fr.xgouchet.packageexplorer.ui.adapter.BaseViewHolder
import io.reactivex.functions.BiConsumer
import io.reactivex.functions.Consumer

class AppDetailsAdapter(
    val listener: BiConsumer<AppInfoViewModel, View?>?,
    val actionListener: Consumer<AppInfoViewModel>?
) : BaseAdapter<AppInfoViewModel>() {

    override fun getDiffHelper(oldContent: List<AppInfoViewModel>, newContent: List<AppInfoViewModel>): DiffUtil.Callback? {
        return AppDetailsDiffUtilCallback(oldContent, newContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<AppInfoViewModel> {
        val layoutInflater = LayoutInflater.from(parent.context)

        when (viewType) {
            TYPE_HEADER -> {
                val root = layoutInflater.inflate(R.layout.item_info_header, parent, false)
                return AppInfoHeaderViewHolder(root, listener)
            }

            TYPE_SIMPLE -> {
                val root = layoutInflater.inflate(R.layout.item_info_simple, parent, false)
                return AppInfoSimpleViewHolder(root, listener)
            }

            TYPE_ICON -> {
                val root = layoutInflater.inflate(R.layout.item_info_icon, parent, false)
                return AppInfoWithIconViewHolder(root, listener)
            }

            TYPE_SUBTITLE -> {
                val root = layoutInflater.inflate(R.layout.item_info_subtitle, parent, false)
                return AppInfoWithSubtitleViewHolder(root, listener)
            }

            TYPE_SUBTITLE_ICON -> {
                val root = layoutInflater.inflate(R.layout.item_info_subtitle_icon, parent, false)
                return AppInfoWithSubtitleAndIconViewHolder(root, listener)
            }

            TYPE_SUBTITLE_ACTION -> {
                val root = layoutInflater.inflate(R.layout.item_info_subtitle_action, parent, false)
                return AppInfoWithSubtitleAndActionViewHolder(root, listener, actionListener)
            }

            TYPE_BULLET -> {
                val root = layoutInflater.inflate(R.layout.item_info_bullet, parent, false)
                return AppInfoBulletViewHolder(root, listener)
            }

            TYPE_SUB_HEADER -> {
                val root = layoutInflater.inflate(R.layout.item_info_sub_header, parent, false)
                return AppInfoSubHeaderViewHolder(root, listener)
            }

            else -> throw IllegalArgumentException("Unknown view type $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        when (item) {
            is AppInfoHeader -> return TYPE_HEADER
            is AppInfoSimple -> return TYPE_SIMPLE
            is AppInfoWithIcon -> return TYPE_ICON
            is AppInfoWithSubtitle -> return TYPE_SUBTITLE
            is AppInfoWithSubtitleAndIcon -> return TYPE_SUBTITLE_ICON
            is AppInfoWithSubtitleAndAction -> return TYPE_SUBTITLE_ACTION
            is AppInfoBullet -> return TYPE_BULLET
            is AppInfoSubHeader -> return TYPE_SUB_HEADER
            else -> throw IllegalArgumentException("Unknown type $item")
        }
    }

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_SIMPLE = 1
        private const val TYPE_ICON = 2
        private const val TYPE_SUBTITLE = 3
        private const val TYPE_SUBTITLE_ICON = 4
        private const val TYPE_SUBTITLE_ACTION = 5
        private const val TYPE_BULLET = 6
        private const val TYPE_SUB_HEADER = 7
    }
}
