package fr.xgouchet.packageexplorer.applist

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.ui.adapter.BaseViewHolder
import io.reactivex.functions.BiConsumer
import io.reactivex.functions.Consumer

class AppViewHolder(
        itemView: View,
        listener: BiConsumer<AppViewModel, View?>?,
        secondaryActionListener: Consumer<AppViewModel>?
) : BaseViewHolder<AppViewModel>(
        itemView = itemView,
        selectedListener = listener,
        secondaryActionListener = secondaryActionListener
) {


    private val iconView: ImageView = itemView.findViewById(R.id.icon_app)
    private val debuggableView: View = itemView.findViewById(R.id.icon_debuggable_app)
    private val sytemAppView: View = itemView.findViewById(R.id.icon_system_app)
    private val largeHeapView: View = itemView.findViewById(R.id.icon_large_heap)
    private val titleView: TextView = itemView.findViewById(R.id.text_title)
    private val packageNameView: TextView = itemView.findViewById(R.id.text_package_name)
    private val installedView: TextView = itemView.findViewById(R.id.text_install)
    private val updatedView: TextView = itemView.findViewById(R.id.text_update)

    init {
        itemView.setOnClickListener { fireSelected() }
    }

    override fun onBindItem(item: AppViewModel) {
        iconView.setImageDrawable(item.icon)

        debuggableView.visibility = if (item.isDebuggable) View.VISIBLE else View.GONE
        sytemAppView.visibility = if (item.isSystemApp) View.VISIBLE else View.GONE
        largeHeapView.visibility = if (item.isLargeHeap) View.VISIBLE else View.GONE

        titleView.text = item.title
        packageNameView.text = item.packageName
        installedView.text = item.installTimeStr
        updatedView.text = item.updateTimeStr
    }

    override fun getTransitionView(): View? {
        return iconView
    }
}
