package fr.xgouchet.packageexplorer.details.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.details.AppDetailsDiffUtilCallback
import fr.xgouchet.packageexplorer.ui.adapter.BaseAdapter
import fr.xgouchet.packageexplorer.ui.adapter.BaseViewHolder
import io.reactivex.rxjava3.functions.BiConsumer
import io.reactivex.rxjava3.functions.Consumer
import java.util.Optional

class AppDetailsAdapter(
    val listener: BiConsumer<AppInfoViewModel, Optional<View>>?,
    val actionListener: Consumer<AppInfoViewModel>?
) : BaseAdapter<AppInfoViewModel>() {

    override fun getDiffHelper(
        oldContent: List<AppInfoViewModel>,
        newContent: List<AppInfoViewModel>
    ): DiffUtil.Callback? {
        return AppDetailsDiffUtilCallback(oldContent, newContent)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<AppInfoViewModel> {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            TYPE_HEADER -> {
                val root = layoutInflater.inflate(R.layout.item_info_header, parent, false)
                AppInfoHeaderViewHolder(root, listener)
            }

            TYPE_SIMPLE -> {
                val root = layoutInflater.inflate(R.layout.item_info_simple, parent, false)
                AppInfoSimpleViewHolder(root, listener)
            }

            TYPE_ICON -> {
                val root = layoutInflater.inflate(R.layout.item_info_icon, parent, false)
                AppInfoWithIconViewHolder(root, listener)
            }

            TYPE_SUBTITLE -> {
                val root = layoutInflater.inflate(R.layout.item_info_subtitle, parent, false)
                AppInfoWithSubtitleViewHolder(root, listener)
            }

            TYPE_SUBTITLE_ICON -> {
                val root = layoutInflater.inflate(R.layout.item_info_subtitle_icon, parent, false)
                AppInfoWithSubtitleAndIconViewHolder(root, listener)
            }

            TYPE_SUBTITLE_ACTION -> {
                val root = layoutInflater.inflate(R.layout.item_info_subtitle_action, parent, false)
                AppInfoWithSubtitleAndActionViewHolder(root, listener, actionListener)
            }

            TYPE_BULLET -> {
                val root = layoutInflater.inflate(R.layout.item_info_bullet, parent, false)
                AppInfoBulletViewHolder(root, listener)
            }

            TYPE_SUB_HEADER -> {
                val root = layoutInflater.inflate(R.layout.item_info_sub_header, parent, false)
                AppInfoSubHeaderViewHolder(root, listener)
            }

            else -> throw IllegalArgumentException("Unknown view type $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item) {
            is AppInfoHeader -> TYPE_HEADER
            is AppInfoSimple -> TYPE_SIMPLE
            is AppInfoWithIcon -> TYPE_ICON
            is AppInfoWithSubtitle -> TYPE_SUBTITLE
            is AppInfoWithSubtitleAndIcon -> TYPE_SUBTITLE_ICON
            is AppInfoWithSubtitleAndAction -> TYPE_SUBTITLE_ACTION
            is AppInfoBullet -> TYPE_BULLET
            is AppInfoSubHeader -> TYPE_SUB_HEADER
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
