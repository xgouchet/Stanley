package fr.xgouchet.packageexplorer.core.utils

import android.content.Intent
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