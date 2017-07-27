package fr.xgouchet.packageexplorer.appdetails

import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes

/**
 * @author Xavier F. Gouchet
 */
object AppInfoType {
    val INFO_TYPE_GLOBAL = 0x1
    val INFO_TYPE_FEATURES_REQUIRED = 0x2
    val INFO_TYPE_CUSTOM_PERMISSIONS = 0x4
    val INFO_TYPE_PERMISSIONS = 0x8
    val INFO_TYPE_ACTIVITIES = 0x10
    val INFO_TYPE_SERVICES = 0x20
    val INFO_TYPE_PROVIDERS = 0x40
    val INFO_TYPE_RECEIVERS = 0x80
}

sealed class AppInfoViewModel(val mask: Int)

data class AppInfoHeader(val type: Int,
                         val header: String,
                         @DrawableRes val icon: Int) : AppInfoViewModel(type)

data class AppInfoSimple(val type: Int,
                         val title: String) : AppInfoViewModel(type)

data class AppInfoWithSubtitle(val type: Int,
                               val title: String,
                               val subtitle: String) : AppInfoViewModel(type)

data class AppInfoWithSubtitleAndIcon(val type: Int,
                                      val title: String,
                                      val subtitle: String,
                                      val icon: Drawable?) : AppInfoViewModel(type)