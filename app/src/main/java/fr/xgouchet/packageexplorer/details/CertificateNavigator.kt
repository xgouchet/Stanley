package fr.xgouchet.packageexplorer.details

import android.app.Activity
import fr.xgouchet.packageexplorer.applist.CertificateAppListActivity
import fr.xgouchet.packageexplorer.ui.mvp.Navigator
import javax.security.cert.X509Certificate
import kotlin.properties.Delegates.notNull

class CertificateNavigator() :
    Navigator<X509Certificate> {

    override var currentActivity: Activity by notNull()

    override fun goToItemDetails(item: X509Certificate) {
        val intent = CertificateAppListActivity.getIntent(item, currentActivity)
        currentActivity.startActivity(intent)
    }

    override fun goToItemEdition(item: X509Certificate) {
    }

    override fun goToItemCreation() {
    }

    override fun goBack() {
        currentActivity.finish()
    }
}
