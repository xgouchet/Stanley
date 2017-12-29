package fr.xgouchet.packageexplorer.applist

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


/**
 * @author Xavier Gouchet
 */
data class AppViewModel(val packageName: String = "",
                        val title: String = "",
                        val icon: Drawable = ColorDrawable(Color.TRANSPARENT),
                        val installTime: Long = 0,
                        val updateTime: Long = 0,
                        val flags: Int = 0) {

    val installTimeStr = DATE_FORMAT.format(Date(installTime))
    val updateTimeStr = DATE_FORMAT.format(Date(updateTime))
    val isSystemApp = (flags and ApplicationInfo.FLAG_SYSTEM != 0)
    val isDebuggable = (flags and ApplicationInfo.FLAG_DEBUGGABLE != 0)
    val isLargeHeap = (flags and ApplicationInfo.FLAG_LARGE_HEAP != 0)

    companion object {

        fun fromPackageName(context: Context, packageName: String): AppViewModel? {
            val pm = context.packageManager
            try {
                val pi = pm.getPackageInfo(packageName, 0)
                val ai = pm.getApplicationInfo(packageName, 0)
                return fromAppInfo(pm, pi, ai)
            } catch (e: PackageManager.NameNotFoundException) {
                Log.e("App", e.message, e)
                return null
            }

        }

        fun fromAppInfo(pm: PackageManager, pi: PackageInfo, ai: ApplicationInfo): AppViewModel {
            return AppViewModel(packageName = ai.packageName,
                    title = pm.getApplicationLabel(ai).toString(),
                    icon = pm.getApplicationIcon(ai),
                    installTime = pi.firstInstallTime,
                    updateTime = pi.lastUpdateTime,
                    flags = ai.flags
            )
        }

        val DATE_FORMAT: DateFormat = SimpleDateFormat.getDateInstance(DateFormat.LONG, Locale.US)
    }
}
