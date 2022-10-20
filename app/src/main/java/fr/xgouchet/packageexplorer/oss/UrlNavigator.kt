package fr.xgouchet.packageexplorer.oss

import android.app.Activity
import android.content.Intent
import android.net.Uri
import fr.xgouchet.packageexplorer.ui.mvp.Navigator
import kotlin.properties.Delegates.notNull

class UrlNavigator : Navigator<String> {

    override var currentActivity: Activity by notNull()

    override fun goToItemDetails(item: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item))
        currentActivity.startActivity(intent)
    }

    override fun goBack() {
        currentActivity.finish()
    }
}
