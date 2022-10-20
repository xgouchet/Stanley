package fr.xgouchet.packageexplorer.launcher

import android.content.ComponentName
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import fr.xgouchet.packageexplorer.R
import fr.xgouchet.packageexplorer.core.utils.Cutelry.knife
import fr.xgouchet.packageexplorer.ui.adapter.BaseViewHolder
import io.reactivex.rxjava3.functions.BiConsumer
import timber.log.Timber
import java.util.Optional

class ResolveInfoViewHolder(
    view: View,
    listener: BiConsumer<ResolveInfo, Optional<View>>,
    val pm: PackageManager
) : BaseViewHolder<ResolveInfo>(itemView = view, selectedListener = listener) {

    val title: TextView by knife(R.id.text_title, view)
    val subtitle: TextView by knife(R.id.text_package_name, view)
    val icon: ImageView by knife(R.id.icon_app, view)

    init {
        view.setOnClickListener { fireSelected() }
    }

    override fun onBindItem(item: ResolveInfo) {
        val component = ComponentName(item.activityInfo.packageName, item.activityInfo.name)
        val info = pm.getActivityInfo(component, 0)
        var iconDrawable: Drawable
        try {
            iconDrawable = pm.getActivityIcon(component)
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.e("Can't find icon for package ${item.activityInfo.packageName}", e)
            iconDrawable = icon.context.resources.getDrawable(R.drawable.ic_no_launcher)
        }
        icon.setImageDrawable(iconDrawable)

        title.text = info.loadLabel(pm)
        subtitle.text = item.activityInfo.name
    }
}
