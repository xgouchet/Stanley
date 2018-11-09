package fr.xgouchet.packageexplorer.details.adapter

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import fr.xgouchet.packageexplorer.R

/**
 * @author Xavier F. Gouchet
 */
object AppInfoType {
    const val INFO_TYPE_GLOBAL = 0x1
    const val INFO_TYPE_FEATURES_REQUIRED = 0x2
    const val INFO_TYPE_CUSTOM_PERMISSIONS = 0x4
    const val INFO_TYPE_PERMISSIONS = 0x8
    const val INFO_TYPE_ACTIVITIES = 0x10
    const val INFO_TYPE_SERVICES = 0x20
    const val INFO_TYPE_PROVIDERS = 0x40
    const val INFO_TYPE_RECEIVERS = 0x80
    const val INFO_TYPE_SIGNATURE = 0x100
    const val INFO_TYPE_METADATA = 0x8000
}

sealed class AppInfoViewModel(val mask: Int,
                              val id: String)

data class AppInfoHeader(val type: Int,
                         val header: String,
                         @DrawableRes val icon: Int,
                         @DrawableRes val expandedIcon: Int = R.drawable.ic_expand_less)
    : AppInfoViewModel(type, "Header {$type} “$header”")

interface AppInfoSelectable {
    fun getLabel(): String
    fun getSelectedData(): String?
}

data class AppInfoSimple(val type: Int,
                         val title: String,
                         val raw: String? = null)
    : AppInfoViewModel(type, "Simple {$type} “$title”"),
        AppInfoSelectable {
    override fun getLabel(): String = title
    override fun getSelectedData(): String? = raw
}


data class AppInfoWithIcon(val type: Int,
                           val title: String,
                           val raw: String? = null,
                           @DrawableRes val icon: Int)
    : AppInfoViewModel(type, "Icon {$type} “$title”  $icon"),
        AppInfoSelectable {
    override fun getLabel(): String = title
    override fun getSelectedData(): String? = raw
}

data class AppInfoWithSubtitle(val type: Int,
                               val title: String,
                               val subtitle: String,
                               val raw: String? = null)
    : AppInfoViewModel(type, "Subtitle {$type} “$title” / $subtitle"),
        AppInfoSelectable {
    override fun getLabel(): String = title
    override fun getSelectedData(): String? = raw
}

data class AppInfoWithSubtitleAndIcon(val type: Int,
                                      val title: String,
                                      val subtitle: String,
                                      val raw: String,
                                      val icon: Drawable?)
    : AppInfoViewModel(type, "Subtitle+Icon {$type} “$title” / $subtitle $icon"),
        AppInfoSelectable {
    override fun getLabel(): String = title
    override fun getSelectedData(): String? = raw
}


data class AppInfoWithSubtitleAndAction(val type: Int,
                                        val title: String,
                                        val subtitle: String,
                                        val raw: String,
                                        val actionText: String,
                                        val actionData: Any?)
    : AppInfoViewModel(type, "Subtitle+Action {$type} “$title” / $subtitle $actionData"),
        AppInfoSelectable {
    override fun getLabel(): String = title
    override fun getSelectedData(): String? = raw
}