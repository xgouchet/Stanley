package fr.xgouchet.packageexplorer.details.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.ui.adapter.BaseViewHolder
import io.reactivex.rxjava3.functions.BiConsumer
import io.reactivex.rxjava3.functions.Consumer

/**
 * @author Xavier F. Gouchet
 */
abstract class AppInfoViewHolder<T : AppInfoViewModel>(
    itemView: View,
    listener: BiConsumer<AppInfoViewModel, View?>?,
    secondaryActionListener: Consumer<AppInfoViewModel>? = null
) : BaseViewHolder<AppInfoViewModel>(
    itemView = itemView,
    selectedListener = listener,
    secondaryActionListener = secondaryActionListener
) {

    init {
        itemView.setOnClickListener {
            fireSelected()
        }
    }

    final override fun onBindItem(item: AppInfoViewModel) {
        @Suppress("UNCHECKED_CAST")
        onBindAppInfoItem(item as T)
    }

    abstract fun onBindAppInfoItem(item: T)
}

class AppInfoHeaderViewHolder(
    itemView: View,
    listener: BiConsumer<AppInfoViewModel, View?>?
) : AppInfoViewHolder<AppInfoHeader>(itemView, listener) {

    private val iconView: ImageView = itemView.findViewById(R.id.icon)
    private val titleView: TextView = itemView.findViewById(R.id.title)
    private val expandedView: ImageView = itemView.findViewById(R.id.expandable)

    override fun onBindAppInfoItem(item: AppInfoHeader) {
        titleView.text = item.header
        iconView.setImageResource(item.icon)
        expandedView.setImageResource(item.expandedIcon)
    }
}

class AppInfoSubHeaderViewHolder(
    itemView: View,
    listener: BiConsumer<AppInfoViewModel, View?>?
) : AppInfoViewHolder<AppInfoSubHeader>(itemView, listener) {

    private val titleView: TextView = itemView.findViewById(R.id.title)

    override fun onBindAppInfoItem(item: AppInfoSubHeader) {
        titleView.text = item.header
    }
}

class AppInfoSimpleViewHolder(
    itemView: View,
    listener: BiConsumer<AppInfoViewModel, View?>?
) : AppInfoViewHolder<AppInfoSimple>(itemView, listener) {

    private val titleView: TextView = itemView.findViewById(R.id.title)

    override fun onBindAppInfoItem(item: AppInfoSimple) {
        titleView.text = item.title
    }
}

class AppInfoBulletViewHolder(
    itemView: View,
    listener: BiConsumer<AppInfoViewModel, View?>?
) : AppInfoViewHolder<AppInfoBullet>(itemView, listener) {

    private val iconView: ImageView = itemView.findViewById(R.id.icon)
    private val contentView: TextView = itemView.findViewById(R.id.content)

    override fun onBindAppInfoItem(item: AppInfoBullet) {
        contentView.text = item.content
        iconView.setImageResource(item.icon)
    }
}

class AppInfoWithIconViewHolder(
    itemView: View,
    listener: BiConsumer<AppInfoViewModel, View?>?
) : AppInfoViewHolder<AppInfoWithIcon>(itemView, listener) {

    private val iconView: ImageView = itemView.findViewById(R.id.icon)
    private val titleView: TextView = itemView.findViewById(R.id.title)

    override fun onBindAppInfoItem(item: AppInfoWithIcon) {
        titleView.text = item.title
        iconView.setImageResource(item.icon)
    }
}

class AppInfoWithSubtitleViewHolder(
    itemView: View,
    listener: BiConsumer<AppInfoViewModel, View?>?
) : AppInfoViewHolder<AppInfoWithSubtitle>(itemView, listener) {

    private val titleView: TextView = itemView.findViewById(R.id.title)
    private val subtitleView: TextView = itemView.findViewById(R.id.subtitle)

    override fun onBindAppInfoItem(item: AppInfoWithSubtitle) {
        titleView.text = item.title
        subtitleView.text = item.subtitle
    }
}

class AppInfoWithSubtitleAndIconViewHolder(
    itemView: View,
    listener: BiConsumer<AppInfoViewModel, View?>?
) : AppInfoViewHolder<AppInfoWithSubtitleAndIcon>(itemView, listener) {

    private val iconView: ImageView = itemView.findViewById(R.id.icon)
    private val titleView: TextView = itemView.findViewById(R.id.title)
    private val subtitleView: TextView = itemView.findViewById(R.id.subtitle)

    override fun onBindAppInfoItem(item: AppInfoWithSubtitleAndIcon) {
        titleView.text = item.title
        subtitleView.text = item.subtitle
        iconView.setImageDrawable(item.icon)
    }
}

class AppInfoWithSubtitleAndActionViewHolder(
    itemView: View,
    listener: BiConsumer<AppInfoViewModel, View?>?,
    actionListener: Consumer<AppInfoViewModel>?
) : AppInfoViewHolder<AppInfoWithSubtitleAndAction>(itemView, listener, actionListener) {

    private val titleView: TextView = itemView.findViewById(R.id.title)
    private val subtitleView: TextView = itemView.findViewById(R.id.subtitle)
    private val actionView: TextView = itemView.findViewById(R.id.action)

    init {
        actionView.setOnClickListener {
            fireSecondaryAction()
        }
    }

    override fun onBindAppInfoItem(item: AppInfoWithSubtitleAndAction) {
        titleView.text = item.title
        subtitleView.text = item.subtitle
        actionView.text = item.actionText
    }
}
