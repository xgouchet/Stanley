package fr.xgouchet.packageexplorer.ui.mvp

import android.app.Activity
import javax.security.cert.X509Certificate

/**
 * @author Xavier F. Gouchet
 */
interface Navigator<in T> {

    var currentActivity: Activity

    fun goToItemDetails(item: T)

    fun goToItemEdition(item: T)

    fun goToItemCreation()

    fun goBack()
}

