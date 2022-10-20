package fr.xgouchet.packageexplorer.details.adapter

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import fr.xgouchet.packageexplorer.R

object AppInfoType {
    const val INFO_TYPE_GLOBAL: Int = 0x1
    const val INFO_TYPE_FEATURES_REQUIRED: Int = 0x2
    const val INFO_TYPE_CUSTOM_PERMISSIONS: Int = 0x4
    const val INFO_TYPE_PERMISSIONS: Int = 0x8
    const val INFO_TYPE_ACTIVITIES: Int = 0x10
    const val INFO_TYPE_SERVICES: Int = 0x20
    const val INFO_TYPE_PROVIDERS: Int = 0x40
    const val INFO_TYPE_RECEIVERS: Int = 0x80
    const val INFO_TYPE_SIGNATURE: Int = 0x100
    const val INFO_TYPE_NATIVE: Int = 0x200

    const val INFO_TYPE_ANDROID: Int = 0x1000
    const val INFO_TYPE_KOTLIN: Int = 0x2000
    const val INFO_TYPE_MISC: Int = 0x4000

    const val INFO_TYPE_METADATA: Int = 0x10000
}

sealed class AppInfoViewModel(
    val mask: Int,
    val id: String
)

data class AppInfoHeader(
    val type: Int,
    val header: String,
    @DrawableRes val icon: Int,
    @DrawableRes val expandedIcon: Int = R.drawable.ic_expand_less
) : AppInfoViewModel(type, "Header {$type} “$header”")

interface AppInfoSelectable {
    fun getClipLabel(): String
    fun getClipData(): String?
}

data class AppInfoSubHeader(
    val type: Int,
    val header: String
) : AppInfoViewModel(type, "SubHeader {$type} “$header”")

data class AppInfoSimple(
    val type: Int,
    val title: String,
    val raw: String? = null
) :
    AppInfoViewModel(type, "Simple {$type} “$title”"),
    AppInfoSelectable {
    override fun getClipLabel(): String = title
    override fun getClipData(): String? = raw
}

data class AppInfoBullet(
    val type: Int,
    val content: String,
    val raw: String? = null,
    @DrawableRes val icon: Int = R.drawable.ic_bullet

) :
    AppInfoViewModel(type, "Bullet {$type} “$content”"),
    AppInfoSelectable {
    override fun getClipLabel(): String = content
    override fun getClipData(): String? = raw
}

data class AppInfoWithIcon(
    val type: Int,
    val title: String,
    val raw: String? = null,
    @DrawableRes val icon: Int
) :
    AppInfoViewModel(type, "Icon {$type} “$title”  $icon"),
    AppInfoSelectable {
    override fun getClipLabel(): String = title
    override fun getClipData(): String? = raw
}

data class AppInfoWithSubtitle(
    val type: Int,
    val title: String,
    val subtitle: String,
    val raw: String? = null
) :
    AppInfoViewModel(type, "Subtitle {$type} “$title” / $subtitle"),
    AppInfoSelectable {
    override fun getClipLabel(): String = title
    override fun getClipData(): String? = raw
}

data class AppInfoWithSubtitleAndIcon(
    val type: Int,
    val title: String,
    val subtitle: String,
    val raw: String? = null,
    val icon: Drawable?
) :
    AppInfoViewModel(type, "Subtitle+Icon {$type} “$title” / $subtitle $icon"),
    AppInfoSelectable {
    override fun getClipLabel(): String = title
    override fun getClipData(): String? = raw
}

data class AppInfoWithSubtitleAndAction(
    val type: Int,
    val title: String,
    val subtitle: String,
    val raw: String? = null,
    val actionText: String,
    val actionData: Any?
) :
    AppInfoViewModel(type, "Subtitle+Action {$type} “$title” / $subtitle $actionData"),
    AppInfoSelectable {
    override fun getClipLabel(): String = title
    override fun getClipData(): String? = raw
}
