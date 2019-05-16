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
import io.reactivex.functions.BiConsumer


class ResolveInfoViewHolder(view: View,
                            listener: BiConsumer<ResolveInfo, View?>,
                            val pm: PackageManager)
    : BaseViewHolder<ResolveInfo>(itemView = view, selectedListener = listener) {

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
            iconDrawable = icon.context.resources.getDrawable(R.drawable.ic_no_launcher)
        }
        icon.setImageDrawable(iconDrawable)

        title.text = info.loadLabel(pm)
        subtitle.text = item.activityInfo.name
    }
}
