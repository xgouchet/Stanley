package fr.xgouchet.packageexplorer.core.utils

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.provider.Settings

fun applicationPlayStoreIntent(packageName: String): Intent {
    val uri = "https://play.google.com/store/apps/details?id=$packageName"
    val packageUri = Uri.parse(uri)
    return Intent(Intent.ACTION_VIEW, packageUri)
}

fun applicationInfoIntent(packageName: String): Intent {
    val packageUri = Uri.parse("package:$packageName")
    return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageUri)
}

fun uninstallPackageIntent(packageName: String): Intent {
    val packageUri = Uri.parse("package:$packageName")
    return Intent(Intent.ACTION_DELETE, packageUri)
}

fun getMainActivities(
    context: Context,
    packageName: String
):
    List<ResolveInfo> {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)
    intent.`package` = packageName

    return context.packageManager.queryIntentActivities(intent, 0)
}

fun getResolvedIntent(info: ResolveInfo): Intent {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)
    intent.setClassName(info.activityInfo.packageName, info.activityInfo.name)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
    return intent
}
